package ai.skills.admin;

import dao.DBConnect;
import dao.MovieDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
import model.Movie;
import service.TicketManagementService;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for MarketingBot sub-agent to retrieve context for content generation.
 */
public class MarketingBotSkills {

    private final MovieDAO movieDAO = new MovieDAO();

    @Tool("Lấy thông tin chi tiết phim để viết bài quảng cáo (ID phim)")
    public String getMovieDetailsForMarketing(@P("ID phim") int movieId) {
        try (Connection conn = DBConnect.getConnection()) {
            Movie movie = movieDAO.findById(conn, movieId);
            if (movie == null) return "Không tìm thấy phim.";
            return String.format("Phim: %s\nRating: %s\nMô tả: %s", 
                movie.getTitle(), movie.getAgeRating(), movie.getDescription());
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách phim để chọn làm mục tiêu marketing")
    public String getMoviesForMarketing() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            return movies.stream()
                .limit(10)
                .map(m -> String.format("- %s (ID: %d)", m.getTitle(), m.getMovieId()))
                .collect(Collectors.joining("\n", "Danh sách phim:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}
