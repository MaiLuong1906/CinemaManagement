package dao;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom ChatMemoryStore that persists messages to SQL Server database.
 * Auto-called by LangChain4j when managing chat memory.
 */
public class ChatMessageDAO implements ChatMemoryStore {

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        List<ChatMessage> history = new ArrayList<>();
        
        // Retrieve the last 20 messages, ordered oldest to newest for LangChain context window
        String sql = "SELECT role, content FROM (" +
                     "   SELECT TOP 20 role, content, created_at FROM chat_messages " +
                     "   WHERE session_id = ? ORDER BY created_at DESC" +
                     ") AS recent_msgs ORDER BY created_at ASC";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sessionId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String role = rs.getString("role");
                    String content = rs.getString("content");
                    
                    if ("user".equalsIgnoreCase(role)) {
                        history.add(new UserMessage(content));
                    } else if ("assistant".equalsIgnoreCase(role)) {
                        history.add(new AiMessage(content));
                    } else if ("system".equalsIgnoreCase(role)) {
                        history.add(new SystemMessage(content));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading chat memory from DB: " + e.getMessage());
        }
        
        return history;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String sessionId = memoryId.toString();
        Integer userId = extractUserIdFromSessionId(sessionId); // Or null if unavailable here
        
        // Logic: The most efficient way is to append ONLY the newest message. 
        // Langchain provides the FULL list. We need to persist only what's missing, OR just the last message.
        // For simplicity and to avoid duplicates, we can clear the session and re-insert the list, 
        // OR better: we handle insert manually in Servlet, but wait, ChatMemoryStore is meant to sync.
        
        // Actually, deleting old and re-inserting is safest for window sliding (it keeps only 20 in DB too).
        // Let's implement full sync to keep the window perfectly mirrored in DB.
        
        String deleteSql = "DELETE FROM chat_messages WHERE session_id = ?";
        String insertSql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false); // Transaction
            
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
                        
                        psIns.setString(3, msg.type().name().toLowerCase());
                        psIns.setNString(4, msg.text());
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
