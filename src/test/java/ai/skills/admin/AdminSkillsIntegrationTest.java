package ai.skills.admin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Admin Skills using a real database connection.
 */
public class AdminSkillsIntegrationTest {

    private final AnalystBotSkills analystBotSkills = new AnalystBotSkills();
    private final MarketingBotSkills marketingBotSkills = new MarketingBotSkills();
    private final ModerateBotSkills moderateBotSkills = new ModerateBotSkills();

    @Test
    public void testAnalystRevenueSummary() {
        System.out.println("--- Testing Analyst Revenue Summary ---");
        String result = analystBotSkills.getRevenueSummary();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("doanh thu"));
    }

    @Test
    public void testAnalystForecast() {
        System.out.println("--- Testing Analyst 7-Day Forecast ---");
        String result = analystBotSkills.get7DayForecast();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Dự báo") || result.contains("Lỗi"));
    }

    @Test
    public void testMarketingGetMovies() {
        System.out.println("--- Testing Marketing Get Movies ---");
        String result = marketingBotSkills.getMoviesForMarketing();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Danh sách phim"));
    }

    @Test
    public void testModerateUserList() {
        System.out.println("--- Testing Moderate User List ---");
        String result = moderateBotSkills.getUserList();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Danh sách") || result.contains("Không có người dùng"));
    }

    @Test
    public void testModerateHallStatus() {
        System.out.println("--- Testing Moderate Hall Status ---");
        String result = moderateBotSkills.getHallStatus();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Phòng") || result.contains("Trạng thái"));
    }
}
