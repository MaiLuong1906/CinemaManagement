package controller;

import dao.DBConnect;
import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
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
 * @author nguye
 */
@WebServlet("/UpdateMovieServlet")
@MultipartConfig // Thêm annotation này để xử lý multipart/form-data
public class UpdateMovieServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
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

        // Title - nếu không có hoặc rỗng thì giữ nguyên
        String title = request.getParameter("title");
        if (title == null || title.trim().isEmpty()) {
            title = oldMovie.getTitle();
        } else {
            title = title.trim();
        }

        // Duration - nếu không có hoặc rỗng thì giữ nguyên
        String durationRaw = request.getParameter("duration");
        int duration = durationRaw == null || durationRaw.trim().isEmpty()
                ? oldMovie.getDuration()
                : Integer.parseInt(durationRaw.trim());

        // Release date - nếu không có hoặc rỗng thì giữ nguyên
        String dateRaw = request.getParameter("release_date");
        LocalDate releaseDate = dateRaw == null || dateRaw.trim().isEmpty()
                ? oldMovie.getReleaseDate()
                : LocalDate.parse(dateRaw.trim());

        // Age rating - nếu không có hoặc rỗng thì giữ nguyên
        String age = request.getParameter("age_rating");
        if (age == null || age.trim().isEmpty()) {
            age = oldMovie.getAgeRating();
        } else {
            age = age.trim();
        }

        // Description - nếu không có hoặc rỗng thì giữ nguyên
        String desc = request.getParameter("description");
        if (desc == null || desc.trim().isEmpty()) {
            desc = oldMovie.getDescription();
        } else {
            desc = desc.trim();
        }

        // Genre - nếu không có hoặc rỗng thì giữ nguyên
        String genreRaw = request.getParameter("movieGenreId");
        int genreId = genreRaw == null || genreRaw.trim().isEmpty()
                ? movieDAO.getGenreIdByMovieId(movieId)
                : Integer.parseInt(genreRaw.trim());

        // Poster - chỉ cập nhật khi có file mới được upload
        Part posterPart = request.getPart("poster");
        String posterUrl = oldMovie.getPosterUrl(); // Giữ poster cũ mặc định

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
            posterUrl = fileName; // Chỉ cập nhật khi có file mới
        }

        // Tạo object Movie với dữ liệu đã được xử lý
        Movie updated = new Movie(
                movieId, title, duration, desc, releaseDate, age, posterUrl
        );

        // Thực hiện update
        try {
            movieDAO.update(DBConnect.getConnection(), updated);
            relDAO.updateGenre(movieId, genreId);

            // Redirect về trang view sau khi update thành công
            response.sendRedirect("UpdateMovieServlet?movieId=" + movieId + "&mode=view&success=true");

        } catch (SQLException ex) {
            System.out.println("===== UPDATE MOVIE ERROR =====");
            System.out.println("Message : " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("ErrorCode: " + ex.getErrorCode());
            request.setAttribute("errorMessage",
                    "SQL Error: " + ex.getMessage());
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for updating movie information";
    }
}
