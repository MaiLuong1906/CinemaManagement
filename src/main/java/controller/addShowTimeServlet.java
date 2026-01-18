/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CinemaHallDAO;
import dao.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import dao.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.CinemaHall;
import model.Movie;

/**
 *
 * @author nguye
 */
public class addShowTimeServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addShowTimeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addShowTimeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> movieList = movieDAO.getAllMovies();
        request.setAttribute("movieList", movieList);
        // chon phong
        CinemaHallDAO cinemaHallDAO = new CinemaHallDAO(DBConnect.getConnection());
        List<CinemaHall> hallList = new ArrayList<>();
        try {
            hallList = cinemaHallDAO.getAllHalls();
        } catch (SQLException ex) {
            System.out.println("Khong co phong nao");
        }
        request.setAttribute("hallList", hallList);
        // gio chieu
        List<LocalTime> listChieu = new ArrayList<>();
        listChieu.add(LocalTime.parse("08:00"));
        listChieu.add(LocalTime.parse("12:00"));
        listChieu.add(LocalTime.parse("15:00"));
        listChieu.add(LocalTime.parse("18:00"));
        listChieu.add(LocalTime.parse("21:00"));
        listChieu.add(LocalTime.parse("23:00"));
        request.setAttribute("listChieu", listChieu);
        request.getRequestDispatcher("/views/admin/movies/addShowTime.jsp")
               .forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
