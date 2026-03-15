package controller;

import dao.MovieDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Movie;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        log("HomeServlet: Handling request...");
        
        if ("true".equals(request.getParameter("debug"))) {
            response.getWriter().println("<h1>Debug Mode: Servlet reached</h1>");
            log("HomeServlet: Debug mode exit");
            return;
        }

        MovieDAO movie = new MovieDAO();
        log("HomeServlet: Fetching movies started...");
        List<Movie> allMovies = movie.getAllMovies();
        log("HomeServlet: Fetching movies completed. Size: " + (allMovies != null ? allMovies.size() : "null"));

        request.setAttribute("latestMovies", allMovies);
        request.setAttribute("topRatedMovies", allMovies);

        log("HomeServlet: Forwarding to home.jsp...");
        forward(request, response, "/views/user/home.jsp");
        log("HomeServlet: Request processing finished.");
    }
}