package controller;

import ai.CineAgentProvider;
import model.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChatServletTest extends BaseControllerTest {

    private ChatServlet servlet;

    @Mock
    private CineAgentProvider.CineAgent mockAgent;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new ChatServlet();
        servlet.init(servletConfig);
    }

    @Test
    public void testHandleChatSuccess() throws Exception {
        // Setup
        when(request.getParameter("message")).thenReturn("Hello AI");
        when(session.getId()).thenReturn("test-session-id");
        
        // Pre-load the mocked agent into the session to avoid CineAgentProvider calls
        when(session.getAttribute("aiAgent")).thenReturn(mockAgent);
        
        when(mockAgent.chat("test-session-id", "Hello AI")).thenReturn("AI Reply");

        // Execute
        servlet.doPost(request, response);

        // Verify JSON response
        String output = responseWriter.toString();
        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"reply\":\"AI Reply\""));
    }

    @Test
    public void testHandleChatEmptyMessage() throws Exception {
        // Setup
        when(request.getParameter("message")).thenReturn("");

        // Execute
        servlet.doPost(request, response);

        // Verify error response
        String output = responseWriter.toString();
        assertTrue(output.contains("\"success\":false"));
        assertTrue(output.contains("Message không được để trống"));
    }

    @Test
    public void testHandleChatFailure() throws Exception {
        // Setup
        when(request.getParameter("message")).thenReturn("Error message");
        when(session.getAttribute("aiAgent")).thenReturn(mockAgent);
        when(mockAgent.chat(anyString(), anyString())).thenThrow(new RuntimeException("AI Crashed"));

        // Execute
        servlet.doPost(request, response);

        // Verify error response
        String output = responseWriter.toString();
        assertTrue(output.contains("\"success\":false"));
        assertTrue(output.contains("Đã xảy ra lỗi hệ thống AI"));
    }
}
