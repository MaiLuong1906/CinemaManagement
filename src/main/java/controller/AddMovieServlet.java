/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DBConnect;
import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import model.Movie;
import model.MovieGenre;
import dao.MovieGenreDAO;
import java.util.List;
import dao.MovieGenreRelDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
/**
 *
 * @author nguye
 */
public class





AddMovieServlet extends HttpServlet {

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
            out.println("<title>Servlet AddMovieServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddMovieServlet at " + request.getContextPath() + "</h1>");
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
        // lay gia tri ban dau
        MovieGenreDAO movieGenreDAO = new MovieGenreDAO();
        List<MovieGenre> movieGenre = movieGenreDAO.getAllGenres();
        request.setAttribute("movieGenreList", movieGenre);
        // chuyen tiep toi addFilm.jsp
        request.getRequestDispatcher("views/admin/movies/addFilm.jsp")
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
    // dung session de luu 
        HttpSession session = request.getSession();
    // xu li logic them phim
    // lay ra cac thuoc tinh da duoc submit tren form
    String name = request.getParameter("title"); 
    int duration = Integer.parseInt(request.getParameter("duration"));
    LocalDate date = LocalDate.parse(request.getParameter("release_date"));
    String age = request.getParameter("age_rating");
    String description = request.getParameter("description");
    int movieGenreId = Integer.parseInt(request.getParameter("movieGenreId"));
    MovieDAO movieDao = new MovieDAO();
    // bien flag
    boolean success = false;
    String baseImageDir = "E:/imgForCinema";
    String posterUrl = "E:/imgForCinema/default.jpg"; // cho mot anh mac dinh o day

    try {
    Part posterPart = request.getPart("poster");
    String fileName = "";

    if (posterPart != null && posterPart.getSize() > 0) {
        fileName = System.currentTimeMillis() + "_" +
                   Paths.get(posterPart.getSubmittedFileName())
                        .getFileName().toString();
        posterUrl = fileName;
    }
    // insert db truoc moi luu file
    Movie movie = new Movie(name, duration, description, date, age, posterUrl);
    int movieId = movieDao.insertAndReturnId(DBConnect.getConnection(), movie);
    // luu them vao bang MovieGenreRel
    MovieGenreRelDAO movieGenreRelDAO = new MovieGenreRelDAO();
    movieGenreRelDAO.insert(DBConnect.getConnection(), movieId, movieGenreId);
    // insert thanh cong moi luu file ve server
     if (posterPart != null && posterPart.getSize() > 0) {
        File dir = new File(baseImageDir);
        if (!dir.exists()) {
            dir.mkdirs(); // tao thu muc luu anh neu chua co
        }
        posterPart.write(baseImageDir + File.separator + fileName);
    }
    
    success = true;
    session.setAttribute("message", "Thêm phim thành công!");
    session.setAttribute("dbError", "");
    } catch (SQLException e) {
        session.setAttribute("dbError", e.getMessage());
        session.setAttribute("message", "Thêm phim thất bại!");
    }
    // forward sang view de hien thi thong tin
    session.setAttribute("success", success);
    response.sendRedirect(request.getContextPath() + "/AddMovieServlet");
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
