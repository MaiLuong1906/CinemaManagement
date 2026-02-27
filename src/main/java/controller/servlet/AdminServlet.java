package controller.servlet;

import controller.handle.ControllerHandle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import controller.handle.movie.*;
import java.io.IOException;

/**
 * Admin Movie Management Servlet
 * Routes requests to appropriate handlers based on action parameter
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            handleRequest(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            handleRequest(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error: " + e.getMessage());
        }
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String command = request.getParameter("command");
        if (command == null || command.isEmpty()) {
            command = "list";
        }
        
        // ← Dispatcher gọi handler class
        ControllerHandle handler = switch(command) {
                // command cho showtime
            case "add-showtime" -> new controller.handle.showtime.AddShowtime();
            case "update-showtime" -> new controller.handle.showtime.UpdateShowtime();
            case "delete-showtime" -> new controller.handle.showtime.DeleteShowtime();
            case "list-showtime" -> new controller.handle.showtime.ListShowtime();
                // command cho movie
            case "update-movie" -> new UpdateMovieHandle();
            default             -> null;
        };
        
        if (handler == null) {
            throw new Exception("Handler not found for command: " + command);
        }
        
        handler.excute(request, response);
    }
}
