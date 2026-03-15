package ai.skills.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for InfoBotSkills using a real database connection.
 */
public class InfoBotSkillsTest {

    private final InfoBotSkills infoBotSkills = new InfoBotSkills();

    @Test
    public void testGetAllMovies() {
        System.out.println("--- Testing getAllMovies ---");
        String result = infoBotSkills.getAllMovies();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Danh sách phim") || result.contains("Hiện tại rạp chưa có phim nào."));
    }

    @Test
    public void testSearchMovies() {
        System.out.println("--- Testing searchMovies (Query: 'Spider') ---");
        String result = infoBotSkills.searchMovies("Spider");
        System.out.println(result);
        assertNotNull(result);
        // Even if not found, it should return a message not an exception
        assertTrue(result.contains("Tìm thấy") || result.contains("Không tìm thấy"));
    }

    @Test
    public void testGetShowtimesForMovie() {
        System.out.println("--- Testing getShowtimesForMovie (MovieID: 1) ---");
        String result = infoBotSkills.getShowtimesForMovie(1);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Lịch chiếu") || result.contains("Hiện chưa có lịch chiếu"));
    }

    @Test
    public void testGetComboProducts() {
        System.out.println("--- Testing getComboProducts ---");
        String result = infoBotSkills.getComboProducts();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Danh sách bắp nước") || result.contains("Hiện không có sản phẩm"));
    }
}
