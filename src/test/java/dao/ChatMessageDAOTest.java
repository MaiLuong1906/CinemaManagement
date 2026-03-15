package dao;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class ChatMessageDAOTest {

    @Test
    public void testToolMessagePersistence() {
        ChatMessageDAO dao = new ChatMessageDAO();
        String sessionId = "test-session-tool-call-" + System.currentTimeMillis();
        
        // 1. Prepare messages including tool calls and results
        UserMessage userMsg = new UserMessage("Lấy lịch sử của tôi");
        
        ToolExecutionRequest toolRequest = ToolExecutionRequest.builder()
                .id("123")
                .name("getMyBookingHistory")
                .arguments("{}")
                .build();
        AiMessage aiMsg = AiMessage.from(toolRequest);
        
        ToolExecutionResultMessage toolResult = ToolExecutionResultMessage.from(toolRequest, "Lịch sử: Phim A, Phim B");
        
        List<ChatMessage> originalMessages = Arrays.asList(userMsg, aiMsg, toolResult);
        
        System.out.println("--- Saving messages ---");
        dao.updateMessages(sessionId, originalMessages);
        
        System.out.println("--- Retrieving messages ---");
        List<ChatMessage> retrievedMessages = dao.getMessages(sessionId);
        
        System.out.println("Original size: " + originalMessages.size());
        System.out.println("Retrieved size: " + retrievedMessages.size());
        
        assertEquals(3, retrievedMessages.size(), "Should retrieve all 3 messages");
        assertEquals("USER", retrievedMessages.get(0).type().name(), "First message should be USER");
        assertEquals("AI", retrievedMessages.get(1).type().name(), "Second message should be AI");
        assertEquals("TOOL_EXECUTION_RESULT", retrievedMessages.get(2).type().name(), "Third message should be TOOL_EXECUTION_RESULT");
        
        // dao.deleteMessages(sessionId);
    }
}
