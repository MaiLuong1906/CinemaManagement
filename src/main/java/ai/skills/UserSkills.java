package ai.skills;

import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dev.langchain4j.agent.tool.Tool;
import model.Movie;
import model.Showtime;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Các công cụ dành cho CineGuide để hỗ trợ người dùng tìm kiếm phim và lịch chiếu.
 */
public class UserSkills {

    private final MovieDAO movieDAO;
    private final ShowtimeDAO showtimeDAO;
    private final ConnectionProvider connectionProvider;

    @FunctionalInterface
    public interface ConnectionProvider {
        Connection getConnection() throws Exception;
    }

    public UserSkills() {
        this.movieDAO = new MovieDAO();
        this.showtimeDAO = new ShowtimeDAO();
        this.connectionProvider = DBConnect::getConnection;
    }

    // For testing
    public UserSkills(MovieDAO movieDAO, ShowtimeDAO showtimeDAO, ConnectionProvider connectionProvider) {
        this.movieDAO = movieDAO;
        this.showtimeDAO = showtimeDAO;
        this.connectionProvider = connectionProvider;
    }

    @Tool("Tìm kiếm phim theo tên hoặc từ khóa liên quan")
    public String searchMovies(String query) {
        try (Connection conn = connectionProvider.getConnection()) {
            List<Movie> movies = movieDAO.searchByTitle(conn, query);
            if (movies.isEmpty()) return "Không tìm thấy phim nào khớp với từ khóa '" + query + "'.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d): %s", m.getTitle(), m.getMovieId(), m.getDescription()))
                .collect(Collectors.joining("\n", "Tìm thấy các phim sau:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi tìm kiếm phim: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách tất cả các phim đang có tại rạp")
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
        try (Connection conn = connectionProvider.getConnection()) {
            List<Showtime> showtimes = showtimeDAO.findByMovie(conn, movieId);
            if (showtimes.isEmpty()) return "Hiện chưa có lịch chiếu cho phim này.";

            return showtimes.stream()
                .map(s -> String.format("- Ngày %s, Slot ID: %s", s.getShowDate(), s.getSlotId()))
                .collect(Collectors.joining("\n", "Lịch chiếu cho phim (ID " + movieId + "):\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy lịch chiếu: " + e.getMessage();
        }
    }

    @Tool("Lấy thông tin chi tiết của một bộ phim dựa trên ID phim")
    public String getMovieDetails(int movieId) {
        try (Connection conn = connectionProvider.getConnection()) {
            Movie movie = movieDAO.findById(conn, movieId);
            if (movie == null) return "Không tìm thấy phim với ID " + movieId;

            return String.format(
                "Chi tiết phim: %s\n- Thời lượng: %d phút\n- Đánh giá tuổi: %s\n- Mô tả: %s",
                movie.getTitle(), movie.getDuration(), movie.getAgeRating(), movie.getDescription()
            );
        } catch (Exception e) {
            return "Lỗi khi lấy chi tiết phim: " + e.getMessage();
        }
    }
}
