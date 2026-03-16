package controller;

import ai.CineAgentProvider;
import model.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet xử lý Chatbot sử dụng kiến trúc AI Agent mới.
 */
@WebServlet(urlPatterns = "/ChatServlet/*", asyncSupported = true)
public class ChatServlet extends HttpServlet {

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            objectMapper = new ObjectMapper();
            log("ChatServlet (Agentic) initialized successfully");
        } catch (Exception e) {
            log("Error initializing ChatServlet", e);
            throw new ServletException("Failed to initialize ChatServlet", e);
        }
    }

    /**
     * Lấy hoặc tạo AI Agent cho session hiện tại.
     * Phân biệt AdminAgent và UserAgent dựa trên role của user.
     * Tự động làm mới Agent nếu User ID trong session thay đổi (ví dụ: người dùng vừa đăng nhập).
     */
    private CineAgentProvider.CineAgent getOrCreateAgent(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        int currentUid = (user != null) ? user.getAccountId() : 0;
        
        CineAgentProvider.CineAgent agent = (CineAgentProvider.CineAgent) session.getAttribute("aiAgent");
        Integer agentUid = (Integer) session.getAttribute("aiAgentUserId");

        // Làm mới agent nếu chưa có hoặc UID đã thay đổi
        if (agent == null || agentUid == null || agentUid != currentUid) {
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createAdminAgent();
            } else {
                agent = CineAgentProvider.createUserAgent(currentUid);
            }
            session.setAttribute("aiAgent", agent);
            session.setAttribute("aiAgentUserId", currentUid);
            log("Created/Refreshed AI Agent (UID: " + currentUid + ", Role: " + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AI-DEBUG] ChatServlet POST request received");
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathInfo = request.getPathInfo();
        
        System.out.println("[AI-DEBUG] RequestURI: " + requestURI + ", PathInfo: " + pathInfo);
        log("ChatServlet POST: requestURI=" + requestURI + ", pathInfo=" + pathInfo);

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/chat")) {
            handleChat(request, response);
        } else if (pathInfo.startsWith("/reset")) {
            handleReset(request, response);
        } else if (pathInfo.startsWith("/stream")) {
            handleStreamChat(request, response);
        } else {
            handleChat(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private void handleChat(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String userMessage = request.getParameter("message");

            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(out, "Message không được để trống");
                return;
            }

            HttpSession session = request.getSession(true);
            CineAgentProvider.CineAgent agent = getOrCreateAgent(session);
            
            // Format memoryId: sessionId-uId để ChatMessageDAO có thể trích xuất userId
            int uid = (Integer) session.getAttribute("aiAgentUserId");
            String memoryId = session.getId() + "-u" + uid;

            long startTime = System.currentTimeMillis();
            // Gọi AI Agent xử lý (AI sẽ tự động gọi Tool nếu cần)
            String reply = agent.chat(memoryId, userMessage);
            long endTime = System.currentTimeMillis();

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            
            // Hỗ trợ Structured Response (JSON trong reply)
            if (reply.trim().startsWith("{") && reply.trim().endsWith("}")) {
                try {
                    JsonNode structured = objectMapper.readTree(reply);
                    if (structured.has("actionType")) {
                        jsonResponse.set("action", structured);
                        jsonResponse.put("reply", structured.has("message") ? structured.get("message").asText() : "Yêu cầu hành động từ hệ thống.");
                    } else {
                        jsonResponse.put("reply", reply);
                    }
                } catch (Exception e) {
                    jsonResponse.put("reply", reply);
                }
            } else {
                jsonResponse.put("reply", reply);
            }

            jsonResponse.put("processingTime", endTime - startTime);
            jsonResponse.put("sessionId", session.getId());

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

            log("Processed Agent message in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            log("Error processing chat message", e);
            sendErrorResponse(out, "Đã xảy ra lỗi hệ thống AI: " + e.getMessage());
        }
    }

    private CineAgentProvider.StreamingCineAgent getOrCreateStreamingAgent(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        int currentUid = (user != null) ? user.getAccountId() : 0;
        
        CineAgentProvider.StreamingCineAgent agent = (CineAgentProvider.StreamingCineAgent) session.getAttribute("aiStreamingAgent");
        Integer agentUid = (Integer) session.getAttribute("aiStreamingAgentUserId");

        if (agent == null || agentUid == null || agentUid != currentUid) {
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createStreamingAdminAgent();
            } else {
                agent = CineAgentProvider.createStreamingUserAgent(currentUid);
            }
            session.setAttribute("aiStreamingAgent", agent);
            session.setAttribute("aiStreamingAgentUserId", currentUid);
            log("Created/Refreshed Streaming AI Agent (UID: " + currentUid + ", Role: " + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    private void handleStreamChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter out = response.getWriter();
        String userMessage = request.getParameter("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            out.print("event: error\ndata: Message cannot be empty\n\n");
            out.flush();
            return;
        }

        HttpSession session = request.getSession(true);
        CineAgentProvider.StreamingCineAgent agent = getOrCreateStreamingAgent(session);

        // Format memoryId: sessionId-uId
        int uid = (Integer) session.getAttribute("aiStreamingAgentUserId");
        String memoryId = session.getId() + "-u" + uid;

        // Bật AsyncContext để không block thread của Tomcat/Servlet
        jakarta.servlet.AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(120000); // 2 minutes timeout for slow AI
        
        // Fix Bug #8: Add AsyncListener
        asyncContext.addListener(new jakarta.servlet.AsyncListener() {
            @Override public void onComplete(jakarta.servlet.AsyncEvent e) {}
            @Override public void onTimeout(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onError(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onStartAsync(jakarta.servlet.AsyncEvent e) {}
        });

        // Cần lấy writer từ asyncContext để đảm bảo an toàn trong môi trường async
        PrintWriter asyncOut = asyncContext.getResponse().getWriter();

        try {
            log("[STREAM-DEBUG] Starting chat for memoryId: " + memoryId);
            dev.langchain4j.service.TokenStream tokenStream = agent.chat(memoryId, userMessage);

            tokenStream
                .onNext(token -> {
                    try {
                        log("[STREAM] Received token: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
                        // Fix Bug #10: Use Jackson for JSON escaping
                        String json = objectMapper.writeValueAsString(java.util.Map.of("token", token));
                        asyncOut.print("data: " + json + "\n\n");
                        asyncOut.flush();
                    } catch (Exception e) {
                        log("Error sending token", e);
                    }
                })
                .onComplete(aiResponse -> {
                    try {
                        AiMessage aiMessage = aiResponse.content();
                        log("[STREAM] Completed successfully. Response length: " + (aiMessage != null ? aiMessage.text().length() : 0));
                        asyncOut.print("data: {\"status\": \"complete\"}\n\n");
                        asyncOut.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error completing stream", e);
                    }
                })
                .onError(error -> {
                    try {
                        String errMsg = error.getMessage() != null ? error.getMessage() : "Unknown AI Error";
                        log("[STREAM-ERROR] AI Stream Error: " + errMsg);
                        error.printStackTrace();
                        
                        String userFriendlyMsg = "Hệ thống AI đang bận (Rate Limit). Vui lòng thử lại sau vài giây.";
                        if (errMsg.contains("rate_limit_exceeded")) {
                            userFriendlyMsg = "Hệ thống đang quá tải. Tôi đang tự động thử lại với tài nguyên khác, vui lòng gửi lại tin nhắn sau 5-10 giây.";
                        }

                        if (!asyncContext.getResponse().isCommitted()) {
                             asyncOut.print("data: {\"error\": \"" + userFriendlyMsg + "\"}\n\n");
                             asyncOut.flush();
                        }
                    } catch (Exception e) {
                        log("Error sending error stream", e);
                    } finally {
                        try { asyncContext.complete(); } catch(Exception e) {}
                    }
                })
                .start();
            log("[STREAM-DEBUG] tokenStream.start() called.");
        } catch (Exception e) {
            log("Error starting stream", e);
            asyncOut.print("data: {\"error\": \"Internal Server Error: " + e.getMessage().replace("\"", "'") + "\"}\n\n");
            asyncOut.flush();
            asyncContext.complete();
        }
    }

    private void handleReset(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute("aiAgent");
                session.removeAttribute("aiAgentUserId");
                session.removeAttribute("aiStreamingAgent");
                session.removeAttribute("aiStreamingAgentUserId");
                log("Reset AI Agent for session: " + session.getId());
            }

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Phiên thảo luận AI đã được làm mới");

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

        } catch (Exception e) {
            log("Error resetting agent", e);
            sendErrorResponse(out, "Lỗi khi reset: " + e.getMessage());
        }
    }

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        try {
            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", false);
            jsonResponse.put("error", errorMessage);

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
        } catch (Exception e) {
            log("Error sending error response", e);
        }
    }

    @Override
    public void destroy() {
        log("ChatServlet destroyed");
        super.destroy();
    }
}