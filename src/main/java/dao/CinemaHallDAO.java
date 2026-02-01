package dao;

import model.CinemaHall;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaHallDAO {

    private Connection conn;

    public CinemaHallDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. Lấy tất cả phòng chiếu
    public List<CinemaHall> getAllHalls() throws SQLException {
        List<CinemaHall> list = new ArrayList<>();
        String sql = "SELECT hall_id, hall_name, total_rows, total_cols, status, created_at FROM cinema_halls";

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CinemaHall hall = new CinemaHall(
                        rs.getInt("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("total_rows"),
                        rs.getInt("total_cols"),
                        rs.getBoolean("status"),
                        rs.getDate("created_at").toLocalDate());
                list.add(hall);
            }
        }
        return list;
    }

    // 2. Lấy phòng chiếu theo ID
    public CinemaHall getHallById(int hallId) throws SQLException {
        String sql = "SELECT hall_id, hall_name, total_rows, total_cols, status, created_at FROM cinema_halls WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CinemaHall(
                        rs.getInt("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("total_rows"),
                        rs.getInt("total_cols"),
                        rs.getBoolean("status"),
                        rs.getDate("created_at").toLocalDate());
            }
        }
        return null;
    }

    // 3. Thêm phòng chiếu - GIỮ NGUYÊN NHƯNG SỬA LẠI
    public int insert(CinemaHall hall) throws SQLException {
        String sql = """
                    INSERT INTO cinema_halls (hall_name, total_rows, total_cols, status, created_at)
                    VALUES (?, ?, ?, ?, GETDATE())
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hall.getHallName());
            ps.setInt(2, hall.getTotal_rows());
            ps.setInt(3, hall.getTotal_cols());
            ps.setBoolean(4, hall.isStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // hall_id
            }
        }
        return -1;
    }

    // 4. Cập nhật trạng thái - GIỮ NGUYÊN NHƯNG SỬA LẠI
    public void updateStatus(int hallId, boolean status) throws SQLException {
        String sql = "UPDATE cinema_halls SET status = ? WHERE hall_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, hallId);
            ps.executeUpdate();
        }
    }

    // 5. Cập nhật phòng chiếu
    public boolean updateHall(CinemaHall hall) throws SQLException {
        String sql = "UPDATE cinema_halls SET hall_name = ? WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hall.getHallName());
            ps.setInt(2, hall.getHallId());
            return ps.executeUpdate() > 0;
        }
    }

    // 6. Xóa phòng chiếu
    public boolean deleteHall(int hallId) throws SQLException {
        String sql = "DELETE FROM cinema_halls WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            return ps.executeUpdate() > 0;
        }
    }

    // ===== CÁC METHOD CẦN BỔ SUNG =====

    // 7. Kiểm tra tên phòng đã tồn tại chưa (để validate khi thêm mới)
    public boolean isHallNameExists(String hallName) throws SQLException {
        String sql = "SELECT 1 FROM cinema_halls WHERE hall_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hallName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // 8. Kiểm tra tên phòng đã tồn tại chưa (trừ phòng hiện tại - dùng cho update)
    public boolean isHallNameExistsExcept(String hallName, int exceptHallId) throws SQLException {
        String sql = "SELECT 1 FROM cinema_halls WHERE hall_name = ? AND hall_id != ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hallName);
            ps.setInt(2, exceptHallId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // 9. Kiểm tra phòng có đang được sử dụng không (có suất chiếu)
    public boolean hasActiveShowtimes(int hallId) throws SQLException {
        String sql = """
                SELECT 1 FROM showtimes
                WHERE hall_id = ? AND show_date >= CAST(GETDATE() AS DATE)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // 10. Đếm tổng số phòng
    public int countTotalHalls() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cinema_halls";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // 11. Đếm số phòng đang hoạt động
    public int countActiveHalls() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cinema_halls WHERE status = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // 12. Lấy danh sách phòng đang hoạt động (cho dropdown khi tạo suất chiếu)
    public List<CinemaHall> getActiveHalls() throws SQLException {
        List<CinemaHall> list = new ArrayList<>();
        String sql = """
                SELECT hall_id, hall_name, total_rows, total_cols, status, created_at
                FROM cinema_halls
                WHERE status = 1
                ORDER BY hall_name
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CinemaHall hall = new CinemaHall(
                        rs.getInt("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("total_rows"),
                        rs.getInt("total_cols"),
                        rs.getBoolean("status"),
                        rs.getDate("created_at").toLocalDate());
                list.add(hall);
            }
        }
        return list;
    }

    // 13. Kiểm tra phòng có tồn tại không
    public boolean exists(int hallId) throws SQLException {
        String sql = "SELECT 1 FROM cinema_halls WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}