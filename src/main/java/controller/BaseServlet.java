package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

import exception.ValidationException;
import exception.BusinessException;
import model.UserDTO;

/**
 * Base servlet providing common functionality for all application servlets.
 * Uses template method pattern to eliminate code duplication.
 *
 * Subclasses must implement handleRequest() with their business logic.
 */
public abstract class BaseServlet extends HttpServlet {

    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Template method that handles common cross-cutting concerns:
     * 1. UTF-8 encoding
     * 2. Authentication check (if required)
     * 3. Delegate to subclass handleRequest()
     * 4. Unified exception handling
     */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1. Set UTF-8 encoding
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            // 2. Optional authentication check
            if (requiresAuthentication() && !isAuthenticated(req)) {
                resp.sendRedirect(req.getContextPath() + "/views/auth/login.jsp");
                return;
            }

            // 3. Delegate to subclass
            handleRequest(req, resp);

        } catch (ValidationException e) {
            log("Validation error: " + e.getMessage(), e);
            handleValidationError(req, resp, e);
        } catch (BusinessException e) {
            log("Business error: " + e.getMessage(), e);
            handleBusinessError(req, resp, e);
        } catch (Exception e) {
            log("Unexpected error processing request", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                          "An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * Subclasses implement this method with their business logic.
     * Exceptions are automatically caught and handled by processRequest().
     */
    protected abstract void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws Exception;

    /**
     * Override to enable authentication check.
     * @return true if this servlet requires user to be logged in
     */
    protected boolean requiresAuthentication() {
        return false;
    }

    // ==================== Parameter Extraction Utilities ====================

    /**
     * Get required integer parameter.
     * @throws ValidationException if parameter is missing or not a valid integer
     */
    protected int getIntParam(HttpServletRequest req, String name) throws ValidationException {
        String value = req.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Missing required parameter: " + name);
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid integer for parameter: " + name);
        }
    }

    /**
     * Get optional integer parameter with default value.
     */
    protected int getIntParam(HttpServletRequest req, String name, int defaultValue) {
        String value = req.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get optional boolean parameter with default value.
     */
    protected boolean getBooleanParam(HttpServletRequest req, String name, boolean defaultValue) {
        String value = req.getParameter(name);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get required date parameter in ISO format (yyyy-MM-dd).
     * @throws ValidationException if parameter is missing or invalid format
     */
    protected LocalDate getDateParam(HttpServletRequest req, String name) throws ValidationException {
        String value = req.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Missing required date parameter: " + name);
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date format for: " + name + " (expected yyyy-MM-dd)");
        }
    }

    /**
     * Get optional date parameter with default value.
     */
    protected LocalDate getDateParam(HttpServletRequest req, String name, LocalDate defaultValue) {
        String value = req.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    /**
     * Get required string parameter.
     * @throws ValidationException if parameter is missing or empty
     */
    protected String getStringParam(HttpServletRequest req, String name) throws ValidationException {
        String value = req.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Missing required parameter: " + name);
        }
        return value.trim();
    }

    /**
     * Get optional string parameter with default value.
     */
    protected String getStringParam(HttpServletRequest req, String name, String defaultValue) {
        String value = req.getParameter(name);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }

    // ==================== Response Helpers ====================

    /**
     * Forward request to JSP page.
     */
    protected void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    /**
     * Send JSON response.
     */
    protected void sendJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(data));
    }

    /**
     * Send redirect response.
     */
    protected void redirect(HttpServletResponse resp, String url) throws IOException {
        resp.sendRedirect(url);
    }

    // ==================== Error Handling ====================

    /**
     * Handle validation errors (user input errors).
     * Override to customize error page or response.
     */
    protected void handleValidationError(HttpServletRequest req, HttpServletResponse resp,
                                         ValidationException e) throws ServletException, IOException {
        req.setAttribute("error", e.getMessage());
        forward(req, resp, "/views/error.jsp");
    }

    /**
     * Handle business logic errors.
     * Override to customize error page or response.
     */
    protected void handleBusinessError(HttpServletRequest req, HttpServletResponse resp,
                                        BusinessException e) throws ServletException, IOException {
        req.setAttribute("error", e.getMessage());
        forward(req, resp, "/views/error.jsp");
    }

    // ==================== Authentication Helpers ====================

    /**
     * Check if user is authenticated (has valid session with user object).
     */
    protected boolean isAuthenticated(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    /**
     * Get current logged-in user from session.
     * @return UserDTO or null if not authenticated
     */
    protected UserDTO getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (UserDTO) session.getAttribute("user") : null;
    }

    /**
     * Get current user's account ID.
     * @throws ValidationException if user is not authenticated
     */
    protected int getCurrentUserId(HttpServletRequest req) throws ValidationException {
        UserDTO user = getCurrentUser(req);
        if (user == null) {
            throw new ValidationException("User not authenticated");
        }
        return user.getAccountId();
    }
}
