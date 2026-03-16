package ai.skills.admin;

import dao.UserProfileDAO;
import dao.DBConnect;
import dev.langchain4j.agent.tool.Tool;
import model.UserDTO;
import service.LogService;
import service.CinemaHallService;
import model.CinemaHall;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for ModerateBot sub-agent to handle system monitoring and moderation tasks.
 */
public class ModerateBotSkills {

    private final UserProfileDAO userProfileDAO;

    public ModerateBotSkills() {
        this.userProfileDAO = new UserProfileDAO();
    }

    public ModerateBotSkills(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    @Tool("Lấy danh sách người dùng trong hệ thống (Tên, Email, Vai trò)")
    public String getUserList() {
        try {
            List<UserDTO> users = userProfileDAO.getAllUsers();
            if (users.isEmpty()) return "Không có người dùng nào.";
            return users.stream()
                .limit(10)
                .map(u -> String.format("- %s (%s) | Role: %s", u.getFullName(), u.getEmail(), u.getRoleId()))
                .collect(Collectors.joining("\n", "Danh sách 10 khách hàng gần nhất:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Xem lịch sử log hệ thống gần nhất (audit logs)")
    public String getSystemLogs() {
        return LogService.getLatestLogs();
    }

    @Tool("Xem trạng thái các phòng chiếu (Hall status)")
    public String getHallStatus() {
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallService hallService = new CinemaHallService(conn);
            List<CinemaHall> halls = hallService.getAllHalls();
            return halls.stream()
                .map(h -> String.format("- Phòng %s (ID: %d): %dx%d, Trạng thái: %s", 
                    h.getHallName(), h.getHallId(), h.getTotal_rows(), h.getTotal_cols(), h.isStatus() ? "Hoạt động" : "Bảo trì"))
                .collect(Collectors.joining("\n", "Trạng thái các phòng chiếu:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}
