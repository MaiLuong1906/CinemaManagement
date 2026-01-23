package controller;

import dao.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/movies")
public class MovieListServlet extends HttpServlet {
    
    private MovieDAO movieDAO = new MovieDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Lấy tham số từ URL
        String[] selectedGenres = request.getParameterValues("genres");
        String keyword = request.getParameter("search");
        
        List<Movie> movies;
        
        // Xử lý filter
        if (selectedGenres != null && selectedGenres.length > 0) {
            // Lọc theo nhiều thể loại
            movies = movieDAO.getMoviesByMultipleGenres(selectedGenres);
        } else {
            // Lấy tất cả phim
            movies = movieDAO.getAllMovies();
        }
        
        // Áp dụng search nếu có keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            movies = movieDAO.searchMovies(movies, keyword.trim());
        }
        
        // Xác định page title
        String pageTitle = buildPageTitle(selectedGenres, keyword);
        
        // Gửi data sang JSP
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("movies", movies);
        request.setAttribute("totalMovies", movies.size());
        request.setAttribute("selectedGenres", selectedGenres);
        
        // Forward sang JSP
        request.getRequestDispatcher("/views/user/movies.jsp").forward(request, response);
    }
    
    /**
     * Tạo page title dựa trên filters
     */
    private String buildPageTitle(String[] genres, String keyword) {
        List<String> titleParts = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            titleParts.add("Tìm kiếm: \"" + keyword + "\"");
        }
        
        if (genres != null && genres.length > 0) {
            String genreText = String.join(", ", genres);
            titleParts.add("Thể loại: " + genreText);
        }
        
        if (titleParts.isEmpty()) {
            return "Tất cả phim";
        }
        
        return String.join(" - ", titleParts);
    }
}