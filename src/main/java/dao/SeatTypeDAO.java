package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SeatType;

public class SeatTypeDAO {

    /* =========================
       Lấy tất cả loại ghế
       ========================= */
    public List<SeatType> findAll(Connection conn) throws SQLException {
        String sql = "SELECT seat_type_id, type_name, extra_fee FROM seat_types ORDER BY seat_type_id";
        List<SeatType> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       Tìm loại ghế theo ID
       ========================= */
    public SeatType findById(Connection conn, int seatTypeId) throws SQLException {
        String sql = """
            SELECT seat_type_id, type_name, extra_fee
            FROM seat_types
            WHERE seat_type_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       Kiểm tra tồn tại (dùng cho Service validate)
       ========================= */
    public boolean exists(Connection conn, int seatTypeId) throws SQLException {
        String sql = "SELECT 1 FROM seat_types WHERE seat_type_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // ===== CÁC METHOD CẦN BỔ SUNG =====

    /* =========================
       Tìm theo tên loại ghế
       ========================= */
    public SeatType findByName(Connection conn, String typeName) throws SQLException {
        String sql = """
            SELECT seat_type_id, type_name, extra_fee
            FROM seat_types
            WHERE type_name = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       Thêm loại ghế mới (cho admin)
       ========================= */
    public int insert(Connection conn, String typeName, BigDecimal extraFee) throws SQLException {
        String sql = """
            INSERT INTO seat_types (type_name, extra_fee)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, typeName);
            ps.setBigDecimal(2, extraFee);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    /* =========================
       Cập nhật loại ghế
       ========================= */
    public boolean update(Connection conn, int seatTypeId, String typeName, BigDecimal extraFee) throws SQLException {
        String sql = """
            UPDATE seat_types 
            SET type_name = ?, extra_fee = ?
            WHERE seat_type_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            ps.setBigDecimal(2, extraFee);
            ps.setInt(3, seatTypeId);
            return ps.executeUpdate() > 0;
        }
    }

    /* =========================
       Xóa loại ghế (cẩn thận với FK)
       ========================= */
    public boolean delete(Connection conn, int seatTypeId) throws SQLException {
        String sql = "DELETE FROM seat_types WHERE seat_type_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            return ps.executeUpdate() > 0;
        }
    }

    /* =========================
       Kiểm tra loại ghế có đang được sử dụng không
       ========================= */
    public boolean isInUse(Connection conn, int seatTypeId) throws SQLException {
        String sql = "SELECT 1 FROM seats WHERE seat_type_id = ? LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /* =========================
       Đếm số ghế đang dùng loại ghế này
       ========================= */
    public int countSeatsUsing(Connection conn, int seatTypeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seats WHERE seat_type_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /* =========================
       Map ResultSet → SeatType
       ========================= */
    private SeatType mapRow(ResultSet rs) throws SQLException {
        SeatType st = new SeatType();
        st.setSeatTypeId(rs.getInt("seat_type_id"));
        st.setTypeName(rs.getString("type_name"));
        st.setExtraFee(rs.getBigDecimal("extra_fee"));
        return st;
    }
}