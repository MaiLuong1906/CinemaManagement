package controller;

import dao.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;


import java.io.IOException;
import java.util.List;

@WebServlet("/movies")
public class MovieListServlet extends HttpServlet {
    
    private MovieDAO movieDAO = new MovieDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Lấy tham số từ URL (nếu có)
//        String genre = request.getParameter("genre");
//        String keyword = request.getParameter("search");
//        
        List<Movie> movies = movieDAO.getAllMovies();
//        
//        // Xử lý theo tham số
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            // Tìm kiếm phim
//            movies = movieDAO.getAllMovies();
//            request.setAttribute("pageTitle", "Kết quả tìm kiếm: " + keyword);
//        } else if (genre != null && !genre.trim().isEmpty()) {
//            // Lọc theo thể loại
//            movies = movieDAO.getAllMovies();
//            request.setAttribute("pageTitle", "Phim " + genre);
//        } else {
//            // Hiển thị tất cả phim
//            movies = movieDAO.getAllMovies();
//            request.setAttribute("pageTitle", "Tất cả phim");
//        }
        
        // Gửi danh sách phim sang JSP
        request.setAttribute("pageTitle", "Tất cả phim");
        request.setAttribute("movies", movies);
        request.setAttribute("totalMovies", movies.size());
        
        // Forward sang JSP
        request.getRequestDispatcher("/views/user/movies.jsp").forward(request, response);
    }
}
