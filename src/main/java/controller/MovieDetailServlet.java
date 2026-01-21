package controller;

import dao.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import dao.DBConnect;

@WebServlet("/movie-detail")
public class MovieDetailServlet extends HttpServlet {
    private MovieDAO movieDAO = new MovieDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int movieId = Integer.parseInt(request.getParameter("id"));
            
            // Lấy connection từ DBConnect
            Connection conn = DBConnect.getConnection();
            Movie movie = movieDAO.findById(conn, movieId);
            conn.close();
            
            if (movie != null) {
                request.setAttribute("movie", movie);
                request.getRequestDispatcher("/views/user/movie-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/movies");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid movie ID: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/movies");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/movies");
        }
    }
}