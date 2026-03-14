package ai.skills.user;

import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.ProductDAO;
import dev.langchain4j.agent.tool.Tool;
import model.Movie;
import model.Showtime;
import model.Product;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for InfoBot sub-agent to handle inquiries about movies, showtimes, and products.
 */
public class InfoBotSkills {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Tool("Tìm kiếm phim theo tên hoặc từ khóa liên quan")
    public String searchMovies(String query) {
        try (Connection conn = DBConnect.getConnection()) {
            List<Movie> movies = movieDAO.searchByTitle(conn, query);
            if (movies.isEmpty()) return "Không tìm thấy phim nào khớp với từ khóa '" + query + "'.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d): %s", m.getTitle(), m.getMovieId(), m.getDescription()))
                .collect(Collectors.joining("\n", "Tìm thấy các phim sau:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi tìm kiếm phim: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách phim đang có tại rạp")
    public String getAllMovies() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            if (movies.isEmpty()) return "Hiện tại rạp chưa có phim nào.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d)", m.getTitle(), m.getMovieId()))
                .collect(Collectors.joining("\n", "Danh sách phim tại rạp:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy danh sách phim: " + e.getMessage();
        }
    }

    @Tool("Lấy lịch chiếu (Showtimes) của một bộ phim dựa trên ID phim")
    public String getShowtimesForMovie(int movieId) {
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> showtimes = showtimeDAO.findByMovie(conn, movieId);
            if (showtimes.isEmpty()) return "Hiện chưa có lịch chiếu cho phim này.";

            return showtimes.stream()
                .map(s -> String.format("- Suất chiếu ID: %d | Ngày %s | Slot ID: %s", s.getShowtimeId(), s.getShowDate(), s.getSlotId()))
                .collect(Collectors.joining("\n", "Lịch chiếu cho phim (ID " + movieId + "):\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy lịch chiếu: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách combo bắp nước và giá cả")
    public String getComboProducts() {
        try {
            List<Product> products = productDAO.findAll();
            if (products.isEmpty()) return "Hiện không có sản phẩm combo nào.";

            return products.stream()
                .map(p -> String.format("- %s (ID: %d): %,.0f VND", p.getItemName(), p.getItemId(), p.getPrice()))
                .collect(Collectors.joining("\n", "Danh sách bắp nước & combo:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage();
        }
    }
}
