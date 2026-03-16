package ai.skills.user;

import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.ProductDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
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

    private final MovieDAO movieDAO;
    private final ShowtimeDAO showtimeDAO;
    private final ProductDAO productDAO;
    private final int userId;

    public InfoBotSkills(int userId) {
        this.movieDAO = new MovieDAO();
        this.showtimeDAO = new ShowtimeDAO();
        this.productDAO = new ProductDAO();
        this.userId = userId;
    }

    public InfoBotSkills(MovieDAO movieDAO, ShowtimeDAO showtimeDAO, ProductDAO productDAO, int userId) {
        this.movieDAO = movieDAO;
        this.showtimeDAO = showtimeDAO;
        this.productDAO = productDAO;
        this.userId = userId;
    }

    @Tool("Tìm kiếm phim theo tên hoặc từ khóa liên quan")
    public String searchMovies(@P("Tên phim hoặc từ khóa tìm kiếm") String query) {
        ai.ToolLogger.log(userId, "{\"tool\": \"Tìm kiếm phim: " + query + "\"}");
        System.out.println("[AI-DEBUG] Tool searchMovies called with query: " + query);
        try (Connection conn = DBConnect.getConnection()) {
            List<Movie> movies = movieDAO.searchByTitle(conn, query);
            System.out.println("[AI-DEBUG] Found " + movies.size() + " movies for query: " + query);
            if (movies.isEmpty()) return "Không tìm thấy phim nào khớp với từ khóa '" + query + "'.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d): %s", m.getTitle(), m.getMovieId(), m.getDescription()))
                .collect(Collectors.joining("\n", "Tìm thấy các phim sau:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] searchMovies error: " + e.getMessage());
            return "Lỗi khi tìm kiếm phim: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách phim đang có tại rạp")
    public String getAllMovies() {
        ai.ToolLogger.log(userId, "{\"tool\": \"Tải danh sách phim\"}");
        System.out.println("[AI-DEBUG] Tool getAllMovies called");
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            System.out.println("[AI-DEBUG] Found " + movies.size() + " movies in theater");
            if (movies.isEmpty()) return "Hiện tại rạp chưa có phim nào.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d)", m.getTitle(), m.getMovieId()))
                .collect(Collectors.joining("\n", "Danh sách phim tại rạp:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getAllMovies error: " + e.getMessage());
            return "Lỗi khi lấy danh sách phim: " + e.getMessage();
        }
    }

    @Tool("Lấy lịch chiếu (Showtimes) của một bộ phim dựa trên ID phim")
    public String getShowtimesForMovie(@P("ID của bộ phim") int movieId) {
        ai.ToolLogger.log(userId, "{\"tool\": \"Tra cứu lịch chiếu phim ID: " + movieId + "\"}");
        System.out.println("[AI-DEBUG] Tool getShowtimesForMovie called with ID: " + movieId);
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> showtimes = showtimeDAO.findByMovie(conn, movieId);
            System.out.println("[AI-DEBUG] Found " + showtimes.size() + " showtimes for ID: " + movieId);
            if (showtimes.isEmpty()) return "Hiện chưa có lịch chiếu cho phim này.";

            return showtimes.stream()
                .map(s -> String.format("- Suất chiếu ID: %d | Ngày %s | Slot ID: %s", s.getShowtimeId(), s.getShowDate(), s.getSlotId()))
                .collect(Collectors.joining("\n", "Lịch chiếu cho phim (ID " + movieId + "):\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getShowtimesForMovie error: " + e.getMessage());
            return "Lỗi khi lấy lịch chiếu: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách combo bắp nước và giá cả")
    public String getComboProducts() {
        ai.ToolLogger.log(userId, "{\"tool\": \"Tra cứu giá combo bắp nước\"}");
        System.out.println("[AI-DEBUG] Tool getComboProducts called");
        try {
            List<Product> products = productDAO.findAll();
            System.out.println("[AI-DEBUG] Found " + products.size() + " combos");
            if (products.isEmpty()) return "Hiện không có sản phẩm combo nào.";

            return products.stream()
                .map(p -> String.format("- %s (ID: %d): %,.0f VND", p.getItemName(), p.getItemId(), p.getPrice()))
                .collect(Collectors.joining("\n", "Danh sách bắp nước & combo:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getComboProducts error: " + e.getMessage());
            return "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage();
        }
    }
}
