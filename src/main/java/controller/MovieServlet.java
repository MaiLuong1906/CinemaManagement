package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.*;
import model.*;
import exception.ValidationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Consolidated movie servlet handling:
 * - Admin: CRUD operations (add, update, delete, admin-list)
 * - User: Browse and view movies (list, detail, view)
 */
@WebServlet("/movie")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class MovieServlet extends BaseServlet {

    private MovieDAO movieDAO;
    private MovieGenreDAO movieGenreDAO;
    private MovieGenreRelDAO movieGenreRelDAO;
    private MovieShowtimeDAO movieShowtimeDAO;

    @Override
    public void init() {
        movieDAO = new MovieDAO();
        movieGenreDAO = new MovieGenreDAO();
        movieGenreRelDAO = new MovieGenreRelDAO();
        movieShowtimeDAO = new MovieShowtimeDAO();
    }

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String action = getStringParam(req, "action", "list");

        switch (action) {
            // Admin actions
            case "admin-list":
                listMoviesForAdmin(req, resp);
                break;
            case "add":
                if ("POST".equals(req.getMethod())) {
                    addMovie(req, resp);
                } else {
                    showAddForm(req, resp);
                }
                break;
            case "update":
                if ("POST".equals(req.getMethod())) {
                    updateMovie(req, resp);
                } else {
                    showUpdateForm(req, resp);
                }
                break;
            case "delete":
                deleteMovie(req, resp);
                break;

            // User actions
            case "list":
            default:
                listMovies(req, resp);
                break;
            case "detail":
                showMovieDetail(req, resp);
                break;
            case "view":
                showViewFilm(req, resp);
                break;
        }
    }

    // ==================== Admin Actions ====================

    private void listMoviesForAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Movie> movies = movieDAO.getAllMovies();
        req.setAttribute("movieList", movies);
        forward(req, resp, "/views/admin/movies/list.jsp");
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<MovieGenre> genres = movieGenreDAO.getAllGenres();
        req.setAttribute("movieGenreList", genres);
        forward(req, resp, "/views/admin/movies/addFilm.jsp");
    }

    private void addMovie(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String title = getStringParam(req, "title");
        int duration = getIntParam(req, "duration");
        LocalDate releaseDate = getDateParam(req, "release_date");
        String ageRating = getStringParam(req, "age_rating");
        String description = getStringParam(req, "description");
        int movieGenreId = getIntParam(req, "movieGenreId");

        String posterUrl = handleFileUpload(req, "poster", "C:/imgForCinema");
        if (posterUrl == null) {
            posterUrl = "C:/imgForCinema/default.jpg";
        }

        Movie movie = new Movie(title, duration, description, releaseDate, ageRating, posterUrl);
        int movieId = movieDAO.insertAndReturnId(DBConnect.getConnection(), movie);
        movieGenreRelDAO.insert(DBConnect.getConnection(), movieId, movieGenreId);

        req.getSession().setAttribute("message", "Thêm phim thành công!");
        resp.sendRedirect(req.getContextPath() + "/movie?action=add");
    }

    private void showUpdateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        List<Movie> movieList = movieDAO.getAllMovies();
        List<MovieGenre> genreList = movieGenreDAO.getAllGenres();
        req.setAttribute("movieList", movieList);
        req.setAttribute("movieGenreList", genreList);

        int movieId = getIntParam(req, "movieId", 0);
        String mode = getStringParam(req, "mode", "view");

        if (movieId > 0) {
            Movie movie = movieDAO.findById(DBConnect.getConnection(), movieId);
            int genreId = movieDAO.getGenreIdByMovieId(movieId);
            req.setAttribute("selectedMovie", movie);
            req.setAttribute("selectedGenreId", genreId);
        }

        req.setAttribute("mode", mode);
        forward(req, resp, "/views/admin/movies/updateFilm.jsp");
    }

    private void updateMovie(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int movieId = getIntParam(req, "movieId");
        Movie oldMovie = movieDAO.findById(DBConnect.getConnection(), movieId);
        if (oldMovie == null) {
            throw new ValidationException("Movie not found");
        }

        String title = getStringParam(req, "title", oldMovie.getTitle());
        int duration = getIntParam(req, "duration", oldMovie.getDuration());
        LocalDate releaseDate = getDateParam(req, "release_date", oldMovie.getReleaseDate());
        String ageRating = getStringParam(req, "age_rating", oldMovie.getAgeRating());
        String description = getStringParam(req, "description", oldMovie.getDescription());

        String posterUrl = handleFileUpload(req, "poster", "C:/imgForCinema");
        if (posterUrl == null) {
            posterUrl = oldMovie.getPosterUrl();
        }

        Movie movie = new Movie(movieId, title, duration, description, releaseDate, ageRating, posterUrl);
        movieDAO.update(DBConnect.getConnection(), movie);

        int movieGenreId = getIntParam(req, "movieGenreId", 0);
        if (movieGenreId > 0) {
            movieGenreRelDAO.updateGenre(movieId, movieGenreId);
        }

        resp.sendRedirect(req.getContextPath() + "/movie?action=update&movieId=" + movieId);
    }

    private void deleteMovie(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int movieId = getIntParam(req, "movieId");
        movieDAO.delete(DBConnect.getConnection(), movieId);
        resp.sendRedirect(req.getContextPath() + "/movie?action=admin-list");
    }

    // ==================== User Actions ====================

    private void listMovies(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String[] selectedGenres = req.getParameterValues("genres");
        String keyword = req.getParameter("search");

        List<Movie> movies;
        if (selectedGenres != null && selectedGenres.length > 0) {
            movies = movieDAO.getMoviesByMultipleGenres(selectedGenres);
        } else {
            movies = movieDAO.getAllMovies();
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            movies = movieDAO.searchMovies(movies, keyword.trim());
        }

        String pageTitle = buildPageTitle(selectedGenres, keyword);
        req.setAttribute("pageTitle", pageTitle);
        req.setAttribute("movies", movies);
        req.setAttribute("totalMovies", movies.size());
        req.setAttribute("selectedGenres", selectedGenres);

        forward(req, resp, "/views/user/movies.jsp");
    }

    private void showMovieDetail(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int movieId = getIntParam(req, "movieId");
        Movie movie = movieDAO.findById(DBConnect.getConnection(), movieId);
        if (movie == null) {
            throw new ValidationException("Movie not found");
        }
        req.setAttribute("movie", movie);
        forward(req, resp, "/views/user/movie-detail.jsp");
    }

    private void showViewFilm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<MovieShowtimeDTO> nowShowing = movieShowtimeDAO.getNowShowing();
        List<MovieShowtimeDTO> comingSoon = movieShowtimeDAO.getComingSoon();
        List<MovieShowtimeDTO> imaxMovies = movieShowtimeDAO.getByHallNameLike("imax");

        req.setAttribute("listNowShowing", nowShowing);
        req.setAttribute("listCommingShowing", comingSoon);
        req.setAttribute("listImax", imaxMovies);

        forward(req, resp, "/view_film.jsp");
    }

    // ==================== Utility Methods ====================

    private String handleFileUpload(HttpServletRequest req, String paramName, String uploadDir)
            throws IOException, ServletException {
        Part filePart = req.getPart(paramName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String fileName = System.currentTimeMillis() + "_"
            + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        filePart.write(uploadDir + File.separator + fileName);
        return fileName;
    }

    private String buildPageTitle(String[] genres, String keyword) {
        java.util.List<String> titleParts = new java.util.ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            titleParts.add("Tìm kiếm: \"" + keyword + "\"");
        }

        if (genres != null && genres.length > 0) {
            String genreText = String.join(", ", genres);
            titleParts.add("Thể loại: " + genreText);
        }

        return titleParts.isEmpty() ? "Tất cả phim" : String.join(" - ", titleParts);
    }
}