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
        String sql = "SELECT hall_id, hall_name FROM cinema_halls";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CinemaHall hall = new CinemaHall(
                        rs.getInt("hall_id"),
                        rs.getString("hall_name")
                );
                list.add(hall);
            }
        }
        return list;
    }

    // 2. Lấy phòng chiếu theo ID
    public CinemaHall getHallById(int hallId) throws SQLException {
        String sql = "SELECT hall_id, hall_name FROM cinema_halls WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CinemaHall(
                        rs.getInt("hall_id"),
                        rs.getString("hall_name")
                );
            }
        }
        return null;
    }

    // 3. Thêm phòng chiếu
    public boolean insertHall(CinemaHall hall) throws SQLException {
        String sql = "INSERT INTO cinema_halls (hall_name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hall.getHallName());
            return ps.executeUpdate() > 0;
        }
    }

    // 4. Cập nhật phòng chiếu
    public boolean updateHall(CinemaHall hall) throws SQLException {
        String sql = "UPDATE cinema_halls SET hall_name = ? WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hall.getHallName());
            ps.setInt(2, hall.getHallId());
            return ps.executeUpdate() > 0;
        }
    }

    // 5. Xóa phòng chiếu
    public boolean deleteHall(int hallId) throws SQLException {
        String sql = "DELETE FROM cinema_halls WHERE hall_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            return ps.executeUpdate() > 0;
        }
    }
}
