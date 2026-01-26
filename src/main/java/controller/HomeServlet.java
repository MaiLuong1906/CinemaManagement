/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Movie;

/**
 *
 * @author LENOVO
 * 
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MovieDAO movie = new MovieDAO();
        List<Movie> latestMovies = movie.getAllMovies();    // phim mới
        List<Movie> topRatedMovies = movie.getAllMovies();  // phim rating cao
        
        request.setAttribute("latestMovies", latestMovies);
        request.setAttribute("topRatedMovies", topRatedMovies);
        
        request.getRequestDispatcher("/views/user/home.jsp").forward(request, response);
    }

}
