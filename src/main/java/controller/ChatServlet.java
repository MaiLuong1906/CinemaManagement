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
     */
    private CineAgentProvider.CineAgent getOrCreateAgent(HttpSession session) {
        CineAgentProvider.CineAgent agent = (CineAgentProvider.CineAgent) session.getAttribute("aiAgent");

        if (agent == null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            // Mặc định là UserAgent, nếu role là Admin thì dùng AdminAgent
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createAdminAgent();
            } else {
                agent = CineAgentProvider.createUserAgent();
            }
            session.setAttribute("aiAgent", agent);
            log("Created new AI Agent (" + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getServletPath();
        String pathWithinServlet = request.getPathInfo();

        if ("/reset".equals(pathWithinServlet)) {
            handleReset(request, response);
        } else if ("/stream".equals(pathWithinServlet)) {
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

            long startTime = System.currentTimeMillis();
            // Gọi AI Agent xử lý (AI sẽ tự động gọi Tool nếu cần)
            String reply = agent.chat(session.getId(), userMessage);
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
        CineAgentProvider.StreamingCineAgent agent = (CineAgentProvider.StreamingCineAgent) session.getAttribute("aiStreamingAgent");

        if (agent == null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createStreamingAdminAgent();
            } else {
                agent = CineAgentProvider.createStreamingUserAgent();
            }
            session.setAttribute("aiStreamingAgent", agent);
            log("Created new Streaming AI Agent (" + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
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

        // Bật AsyncContext để không block thread của Tomcat/Servlet
        jakarta.servlet.AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0); // Không timeout cho stream

        try {
            dev.langchain4j.service.TokenStream tokenStream = agent.chat(session.getId(), userMessage);

            tokenStream
                .onNext(token -> {
                    try {
                        // Escape newline and quotes for JSON safety
                        String cleanToken = token.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"");
                        out.print("data: {\"token\": \"" + cleanToken + "\"}\n\n");
                        out.flush();
                    } catch (Exception e) {
                        log("Error sending token", e);
                    }
                })
                .onComplete(responseMsg -> {
                    try {
                        out.print("event: end\ndata: {\"status\": \"complete\"}\n\n");
                        out.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error completing stream", e);
                    }
                })
                .onError(error -> {
                    try {
                        out.print("event: error\ndata: " + error.getMessage().replace("\n", " ") + "\n\n");
                        out.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error sending error stream", e);
                    }
                })
                .start();
        } catch (Exception e) {
            log("Error starting stream", e);
            out.print("event: error\ndata: System Error\n\n");
            out.flush();
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
                session.removeAttribute("aiStreamingAgent");
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