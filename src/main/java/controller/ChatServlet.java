package controller;

import ai.Agent;
import ai.LLM;
import ai.memory.Memory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.SeatFillRate_ViewService;

import java.io.IOException;
import java.io.PrintWriter;

public class ChatServlet extends HttpServlet {

    private LLM llm;
    private SeatFillRate_ViewService seatService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            llm          = new LLM();
            seatService  = new SeatFillRate_ViewService();
            objectMapper = new ObjectMapper();
            log("ChatServlet initialized successfully");
        } catch (Exception e) {
            log("Error initializing ChatServlet", e);
            throw new ServletException("Failed to initialize ChatServlet", e);
        }
    }

    private Agent getOrCreateAgent(HttpSession session) {
        Agent agent = (Agent) session.getAttribute("agent");

        if (agent == null) {
            Memory memory = new Memory();
            agent = new Agent(memory, llm, seatService); // bỏ IntentClassifier và FillRateEnvironment
            session.setAttribute("agent", agent);
            log("Created new agent for session: " + session.getId());
        }

        return agent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getServletPath();

        if ("/ChatServlet/reset".equals(pathInfo)) {
            handleReset(request, response);
        } else {
            handleChat(request, response);
        }
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
            Agent agent = getOrCreateAgent(session);

            long startTime = System.currentTimeMillis();
            String reply = agent.handle(userMessage);
            long endTime = System.currentTimeMillis();

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.put("reply", reply);
            jsonResponse.put("processingTime", endTime - startTime);
            jsonResponse.put("sessionId", session.getId());

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

            log("Processed message in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            log("Error processing chat message", e);
            sendErrorResponse(out, "Đã xảy ra lỗi: " + e.getMessage());
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
                Agent agent = (Agent) session.getAttribute("agent");
                if (agent != null) {
                    agent.clearMemory();
                }
                session.removeAttribute("agent");
                log("Reset agent for session: " + session.getId());
            }

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Memory đã được reset");

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