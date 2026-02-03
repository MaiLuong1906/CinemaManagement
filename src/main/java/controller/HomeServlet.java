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
        MovieDAO movie = new MovieDAO();
        List<Movie> latestMovies = movie.getAllMovies();
        List<Movie> topRatedMovies = movie.getAllMovies();

        request.setAttribute("latestMovies", latestMovies);
        request.setAttribute("topRatedMovies", topRatedMovies);

        forward(request, response, "/views/user/home.jsp");
    }
}
