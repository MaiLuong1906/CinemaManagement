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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;
import dao.MovieGenreDAO;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import dao.MovieGenreRelDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nguye
 */
@WebServlet("/UpdateMovieServlet")
public class UpdateMovieServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateMovie</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateMovie at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MovieDAO movieDAO = new MovieDAO();
        MovieGenreDAO genreDAO = new MovieGenreDAO();

        request.setAttribute("movieList", movieDAO.getAllMovies());
        request.setAttribute("movieGenreList", genreDAO.getAllGenres());

        String movieIdRaw = request.getParameter("movieId");
        String mode = request.getParameter("mode");
        if (mode == null) {
            mode = "view";
        }

        if (movieIdRaw != null && !movieIdRaw.isEmpty()) {
            int movieId = Integer.parseInt(movieIdRaw);
            Movie movie = null;
            try {
                movie = movieDAO.findById(DBConnect.getConnection(), movieId);
            } catch (SQLException ex) {
                Logger.getLogger(UpdateMovieServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            int genreId = movieDAO.getGenreIdByMovieId(movieId);
            request.setAttribute("selectedMovie", movie);
            request.setAttribute("selectedGenreId", genreId);
        }
        request.setAttribute("mode", mode);
        request.getRequestDispatcher("views/admin/movies/updateFilm.jsp")
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

        int movieId = Integer.parseInt(request.getParameter("movieId"));

// Lấy movie cũ từ DB
        MovieDAO movieDAO = new MovieDAO();
        MovieGenreRelDAO relDAO = new MovieGenreRelDAO();
        Movie oldMovie = null;
        try {
            oldMovie = movieDAO.findById(DBConnect.getConnection(), movieId);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateMovieServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
// title
        String title = request.getParameter("title");
        if (title == null || title.isEmpty()) {
            title = oldMovie.getTitle();
        }
// duration
        String durationRaw = request.getParameter("duration");
        int duration = durationRaw == null || durationRaw.isEmpty()
                ? oldMovie.getDuration()
                : Integer.parseInt(durationRaw);

 // release date
        String dateRaw = request.getParameter("release_date");
        LocalDate releaseDate = dateRaw == null || dateRaw.isEmpty()
                ? oldMovie.getReleaseDate()
                : LocalDate.parse(dateRaw);
        // age rating
        String age = request.getParameter("age_rating");
        if (age == null || age.isEmpty()) {
            age = oldMovie.getAgeRating();
        }
        // description
        String desc = request.getParameter("description");
        if (desc == null || desc.isEmpty()) {
            desc = oldMovie.getDescription();
        }
        // genre
        String genreRaw = request.getParameter("movieGenreId");
        int genreId = genreRaw == null || genreRaw.isEmpty()
                ? movieDAO.getGenreIdByMovieId(movieId)
                : Integer.parseInt(genreRaw);
        // poster
        Part posterPart = request.getPart("poster");
        // Giữ poster cũ mặc định
        String posterUrl = oldMovie.getPosterUrl();
        if (posterPart != null && posterPart.getSize() > 0) {
            String uploadDir = "E:/imgForCinema";

            String fileName = System.currentTimeMillis() + "_"
                    + Paths.get(posterPart.getSubmittedFileName())
                            .getFileName().toString();

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            posterPart.write(uploadDir + File.separator + fileName);
            posterUrl = fileName; // chỉ cập nhật khi có file mới
        }

        Movie updated = new Movie(
                movieId, title, duration, desc, releaseDate, age, posterUrl
        );

        try {
            movieDAO.update(DBConnect.getConnection(), updated);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateMovieServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            relDAO.updateGenre(movieId, genreId);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateMovieServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
