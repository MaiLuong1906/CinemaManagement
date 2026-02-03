package controller;

import dao.MovieDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;
import java.sql.Connection;
import dao.DBConnect;
import java.util.List;
import model.MovieDetailDTO;
import service.ShowtimeService;
import exception.ValidationException;

@WebServlet("/movie-detail")
public class MovieDetailServlet extends BaseServlet {
    private MovieDAO movieDAO = new MovieDAO();
    private ShowtimeService showtimeService = new ShowtimeService();

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int movieId = getIntParam(request, "id");

        Connection conn = DBConnect.getConnection();
        Movie movie = movieDAO.findById(conn, movieId);
        List<MovieDetailDTO> showtimes = showtimeService.getMovieDetailByMovieId(movieId);

        if (movie != null) {
            request.setAttribute("movie", movie);
            request.setAttribute("showtimes", showtimes);
            forward(request, response, "/views/user/movie-detail.jsp");
        } else {
            throw new ValidationException("Movie not found with ID: " + movieId);
        }
    }
}
