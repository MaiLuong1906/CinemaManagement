package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.sql.*;
import java.util.*;

/**
 * Custom ChatMemoryStore that persists messages to SQL Server database.
 * Auto-called by LangChain4j when managing chat memory.
 */
public class ChatMessageDAO implements ChatMemoryStore {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        List<ChatMessage> history = new ArrayList<>();
        
        String sql = "SELECT role, content FROM (" +
                     "   SELECT TOP 30 role, content, created_at FROM chat_messages " +
                     "   WHERE session_id = ? ORDER BY created_at DESC" +
                     ") AS recent_msgs ORDER BY created_at ASC";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sessionId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String role = rs.getString("role");
                    String content = rs.getString("content");
                    
                    try {
                        ChatMessage message = deserialize(role, content);
                        if (message != null) {
                            history.add(message);
                        }
                    } catch (Exception e) {
        } catch (SQLException e) {
            System.err.println("Error loading chat memory from DB: " + e.getMessage());
        }
        
        return history;
    }

    private ChatMessage deserialize(String role, String content) throws JsonProcessingException {
        if (content == null || content.isEmpty()) return null;
        
        if (content.trim().startsWith("{")) {
            Map<String, Object> map = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});
            String text = (String) map.get("text");
            
            return switch (role.toLowerCase()) {
                case "user" -> new UserMessage(text);
                case "system" -> new SystemMessage(text);
                case "assistant" -> {
                    List<ToolExecutionRequest> toolRequests = new ArrayList<>();
                    if (map.containsKey("toolExecutionRequests")) {
                        List<Map<String, Object>> requests = (List<Map<String, Object>>) map.get("toolExecutionRequests");
                        for (Map<String, Object> req : requests) {
                            toolRequests.add(ToolExecutionRequest.builder()
                                .id((String) req.get("id"))
                                .name((String) req.get("name"))
                                .arguments((String) req.get("arguments"))
                                .build());
                        }
                    }
                    if (!toolRequests.isEmpty()) {
                        yield AiMessage.from(toolRequests);
                    } else {
                        yield AiMessage.from(text);
                    }
                }
                case "tool_execution_result" -> {
                    String id = (String) map.get("toolExecutionRequestId");
                    String toolName = (String) map.get("toolName");
                    yield ToolExecutionResultMessage.from(id, toolName, text);
                }
                default -> null;
            };
        }
        
        // Fallback for legacy plain text messages
        return switch (role.toLowerCase()) {
            case "user" -> new UserMessage(content);
            case "assistant" -> new AiMessage(content);
            case "system" -> new SystemMessage(content);
            default -> null;
        };
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String sessionId = memoryId.toString();
        Integer userId = extractUserIdFromSessionId(sessionId);
        
        String deleteSql = "DELETE FROM chat_messages WHERE session_id = ?";
        String insertSql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            
            try {
                try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                    psDel.setString(1, sessionId);
                    psDel.executeUpdate();
                }
                
                try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                    for (ChatMessage msg : messages) {
                        psIns.setString(1, sessionId);
                        if (userId != null && userId > 0) {
                            psIns.setInt(2, userId);
                        } else {
                            psIns.setNull(2, java.sql.Types.INTEGER);
                        }
                        
                        psIns.setString(3, toRoleString(msg));
                        
                        try {
                            String json = serialize(msg);
                            psIns.setNString(4, json);
                        } catch (Exception e) {
                            String text = (msg.text() != null) ? msg.text() : "";
                            psIns.setNString(4, text);
                        }
                        
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error syncing chat memory to DB: " + e.getMessage());
        }
    }

    /**
     * Efficiently appends a single message to the chat history.
     * Can be used to optimize high-frequency chat updates.
     */
    public void appendMessage(String sessionId, ChatMessage msg) {
        Integer userId = extractUserIdFromSessionId(sessionId);
        String sql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sessionId);
            if (userId != null && userId > 0) {
                ps.setInt(2, userId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            
            ps.setString(3, toRoleString(msg));
            ps.setNString(4, (msg.text() != null) ? msg.text() : "");
            
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error appending chat message to DB: " + e.getMessage());
        }
    }

    private String serialize(ChatMessage msg) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        if (msg instanceof UserMessage) {
            map.put("text", ((UserMessage) msg).text());
        } else if (msg instanceof AiMessage) {
            AiMessage ai = (AiMessage) msg;
            map.put("text", ai.text());
            if (ai.hasToolExecutionRequests()) {
                List<Map<String, String>> requests = new ArrayList<>();
                for (ToolExecutionRequest req : ai.toolExecutionRequests()) {
                    Map<String, String> reqMap = new HashMap<>();
                    reqMap.put("id", req.id());
                    reqMap.put("name", req.name());
                    reqMap.put("arguments", req.arguments());
                    requests.add(reqMap);
                }
                map.put("toolExecutionRequests", requests);
            }
        } else if (msg instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage tr = (ToolExecutionResultMessage) msg;
            map.put("toolExecutionRequestId", tr.id());
            map.put("toolName", tr.toolName());
            map.put("text", tr.text());
        } else if (msg instanceof SystemMessage) {
            map.put("text", ((SystemMessage) msg).text());
        }
        return objectMapper.writeValueAsString(map);
    }

    private String toRoleString(ChatMessage msg) {
        return switch (msg.type()) {
            case USER   -> "user";
            case AI     -> "assistant";
            case SYSTEM -> "system";
            default     -> msg.type().name().toLowerCase();
        };
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        String sql = "DELETE FROM chat_messages WHERE session_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting chat memory from DB: " + e.getMessage());
        }
    }
    
    // Utility to map a session pattern to a user ID if needed, though typically session_id alone is fine
    private Integer extractUserIdFromSessionId(String sessionId) {
        if (sessionId.contains("_User_")) {
            try {
                return Integer.parseInt(sessionId.split("_User_")[1]);
            } catch (Exception e) {}
        }
        return null;
    }
}
