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
import dev.langchain4j.model.output.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                agent = CineAgentProvider.createUserAgent(currentUid, session);
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
        String requestURI = request.getRequestURI();
        String pathInfo = request.getPathInfo();
        
        log("ChatServlet POST: requestURI=" + requestURI + ", pathInfo=" + pathInfo);

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/chat")) {
            handleChat(request, response);
        } else if (pathInfo.startsWith("/reset")) {
            handleReset(request, response);
        } else if (pathInfo.startsWith("/stream")) {
            handleStreamChat(request, response);
        } else if (pathInfo.startsWith("/history")) {
            handleHistory(request, response);
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

            // Sau khi chat, kiểm tra xem tool Auth có thay đổi user trong session không
            UserDTO newUser = (UserDTO) session.getAttribute("user");
            if (newUser != null && newUser.getAccountId() != uid) {
                log("[AI-AUTH] User logged in via AI. Refreshing agent for next turn...");
                getOrCreateAgent(session); // Refresh agent with new UID
            }

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

            // Check for pending widget
            String pendingWidget = (String) session.getAttribute("pendingWidget");
            if (pendingWidget != null) {
                try {
                    jsonResponse.set("widget", objectMapper.readTree(pendingWidget));
                    session.removeAttribute("pendingWidget");
                } catch (Exception e) {
                    log("Error parsing pending widget JSON", e);
                }
            }

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
                agent = CineAgentProvider.createStreamingUserAgent(currentUid, session);
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
        jakarta.servlet.AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(120000); 
        
        asyncContext.addListener(new jakarta.servlet.AsyncListener() {
            @Override public void onComplete(jakarta.servlet.AsyncEvent e) {}
            @Override public void onTimeout(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onError(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onStartAsync(jakarta.servlet.AsyncEvent e) {}
        });

        PrintWriter asyncOut = asyncContext.getResponse().getWriter();

        try {
            CineAgentProvider.StreamingCineAgent agent = getOrCreateStreamingAgent(session);
            
            Integer uidObj = (Integer) session.getAttribute("aiStreamingAgentUserId");
            int uid = uidObj != null ? uidObj : 0;
            String memoryId = session.getId() + "-u" + uid;
            
            log("[STREAM] Starting chat for memoryId: " + memoryId);

            // Register ToolLogger to send tool execution events back to the same SSE stream
            ai.ToolLogger.register(uid, (logJson) -> {
                try {
                    if (!asyncContext.getResponse().isCommitted()) {
                        asyncOut.print(logJson);
                        asyncOut.flush();
                    }
                } catch (Exception e) {
                    log("Error sending ToolLogger log", e);
                }
            });

            dev.langchain4j.service.TokenStream tokenStream = agent.chat(memoryId, userMessage);

            tokenStream
                .onNext(token -> {
                    try {
                        String json = objectMapper.writeValueAsString(java.util.Map.of("token", token));
                        asyncOut.print("data: " + json + "\n\n");
                        asyncOut.flush();
                    } catch (Exception e) {
                        log("Error sending token", e);
                    }
                })
                .onComplete(aiResponse -> {
                    try {
                        // Check for pending widget in side-channel
                        String pendingWidget = (String) session.getAttribute("pendingWidget");
                        if (pendingWidget != null) {
                            asyncOut.print("data: {\"widget\": " + pendingWidget + "}\n\n");
                            session.removeAttribute("pendingWidget");
                        }
                        
                        asyncOut.print("data: {\"status\": \"complete\"}\n\n");
                        asyncOut.flush();
                        ai.ToolLogger.unregister(uid);
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error completing stream", e);
                    }
                })
                .onError(error -> {
                    try {
                        String errMsg = error.getMessage() != null ? error.getMessage() : "Unknown AI Error";
                        String userFriendlyMsg = "Hệ thống AI đang gặp sự cố. Vui lòng gửi lại tin nhắn.";
                        if (errMsg.contains("rate_limit_exceeded")) {
                            userFriendlyMsg = "Hệ thống (Groq API) đang quá tải. Vui lòng thử lại sau vài giây.";
                        }

                        if (!asyncContext.getResponse().isCommitted()) {
                             asyncOut.print("data: {\"error\": \"" + userFriendlyMsg + "\", \"details\": \"" + errMsg.replace("\"", "\\\"") + "\"}\n\n");
                             asyncOut.flush();
                        }
                    } catch (Exception e) {
                        log("Error sending error stream", e);
                    } finally {
                        ai.ToolLogger.unregister(uid);
                        try { asyncContext.complete(); } catch(Exception e) {}
                    }
                })
                .start();
        } catch (Exception e) {
            log("Error starting stream", e);
            asyncOut.print("data: {\"error\": \"Internal Server Error: " + e.getMessage() + "\"}\n\n");
            asyncOut.flush();
            asyncContext.complete();
        }
    }

    private void handleHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(true);
            UserDTO user = (UserDTO) session.getAttribute("user");
            int uid = (user != null) ? user.getAccountId() : 0;
            String memoryId = session.getId() + "-u" + uid;

            log("[HISTORY] Loading history for memoryId: " + memoryId);
            dao.ChatMessageDAO chatDAO = new dao.ChatMessageDAO();
            List<Map<String, String>> history = chatDAO.getRecentMessages(memoryId, 20);

            // Nếu user vừa đăng nhập, hãy lấy thêm lịch sử từ khi họ còn là khách (UID=0)
            Integer preLoginUid = (Integer) session.getAttribute("preLoginUserId");
            if (preLoginUid != null && uid != preLoginUid) {
                String preLoginMemoryId = session.getId() + "-u" + preLoginUid;
                log("[HISTORY] Merging history from pre-login: " + preLoginMemoryId);
                List<Map<String, String>> guestHistory = chatDAO.getRecentMessages(preLoginMemoryId, 10);
                if (!guestHistory.isEmpty()) {
                    // Gộp history (guest trước, user sau)
                    List<Map<String, String>> combined = new ArrayList<>(guestHistory);
                    combined.addAll(history);
                    history = combined;
                }
                // Xóa flag sau khi đã phục hồi thành công
                session.removeAttribute("preLoginUserId");
            }

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.set("history", objectMapper.valueToTree(history));

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
        } catch (Exception e) {
            log("Error loading history", e);
            sendErrorResponse(out, "Lỗi khi tải lịch sử: " + e.getMessage());
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