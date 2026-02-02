        package dao;

import model.Seat;
import model.SeatType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SeatSelectionDTO;


public class SeatDAO {

    /* =========================
       1. KIỂM TRA GHẾ TỒN TẠI
       ========================= */
    public boolean exists(int seatId) {
        String sql = "SELECT 1 FROM seats WHERE seat_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }

    /* =========================
       2. TÌMGHẾ THEO ID
       ========================= */
    public Seat findById(int seatId) throws SQLException {
        String sql = """
            SELECT s.*, st.type_name, st.extra_fee
            FROM seats s
            JOIN seat_types st ON s.seat_type_id = st.seat_type_id
            WHERE s.seat_id = ?
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    /* =========================
       3. DANH SÁCH GHẾ THEO PHÒNG
       ========================= */
    public List<Seat> findByHall(int hallId) throws SQLException {
        String sql = """
            SELECT s.*, st.type_name, st.extra_fee
            FROM seats s
            JOIN seat_types st ON s.seat_type_id = st.seat_type_id
            WHERE s.hall_id = ?
            ORDER BY s.row_index, s.column_index
        """;

        List<Seat> list = new ArrayList<>();

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       4. THÊM 1 GHẾ
       ========================= */
    public void insert(
            int hallId,
            String seatCode,
            int rowIndex,
            int columnIndex,
            int seatTypeId,
            boolean active
    ) throws SQLException {

        String sql = """
            INSERT INTO seats
            (hall_id, seat_code, row_index, column_index, seat_type_id, is_active)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setString(2, seatCode);
            ps.setInt(3, rowIndex);
            ps.setInt(4, columnIndex);
            ps.setInt(5, seatTypeId);
            ps.setBoolean(6, active);

            ps.executeUpdate();
        }
    }

    /* =========================
       5. CẬP NHẬT LOẠI GHẾ
       ========================= */
    public void updateSeatType(int seatId, int seatTypeId) throws SQLException {
        String sql = "UPDATE seats SET seat_type_id = ? WHERE seat_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatTypeId);
            ps.setInt(2, seatId);
            ps.executeUpdate();
        }
    }

    /* =========================
       6. BẬT / TẮT GHẾ
       ========================= */
    public void updateActive(int seatId, boolean active) throws SQLException {
        String sql = "UPDATE seats SET is_active = ? WHERE seat_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setInt(2, seatId);
            ps.executeUpdate();
        }
    }

    /* =========================
       7. XOÁ GHẾ THEO PHÒNG
       ========================= */
    public void deleteByHall(int hallId) throws SQLException {
        String sql = "DELETE FROM seats WHERE hall_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.executeUpdate();
        }
    }

    // ===== CÁC METHOD CẦN BỔ SUNG =====

    /* =========================
       8. ĐẾM TỔNG SỐ GHẾ TRONG PHÒNG
       ========================= */
    public int countSeatsByHall(int hallId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seats WHERE hall_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /* =========================
       9. ĐẾM SỐ GHẾ HOẠT ĐỘNG TRONG PHÒNG
       ========================= */
    public int countActiveSeats(int hallId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seats WHERE hall_id = ? AND is_active = 1";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /* =========================
       10. KIỂM TRA MÃ GHẾ ĐÃ TỒN TẠI TRONG PHÒNG
       ========================= */
    public boolean isSeatCodeExists(int hallId, String seatCode) throws SQLException {
        String sql = "SELECT 1 FROM seats WHERE hall_id = ? AND seat_code = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setString(2, seatCode);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /* =========================
       11. KIỂM TRA TỌA ĐỘ ĐÃ TỒN TẠI TRONG PHÒNG
       ========================= */
    public boolean isCoordinateExists(int hallId, int rowIndex, int colIndex) throws SQLException {
        String sql = "SELECT 1 FROM seats WHERE hall_id = ? AND row_index = ? AND column_index = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setInt(2, rowIndex);
            ps.setInt(3, colIndex);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /* =========================
       12. XOÁ 1 GHẾ CỤ THỂ
       ========================= */
    public boolean deleteSeat(int seatId) throws SQLException {
        String sql = "DELETE FROM seats WHERE seat_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatId);
            return ps.executeUpdate() > 0;
        }
    }

    /* =========================
       13. LẤY GHẾ THEO TỌA ĐỘ
       ========================= */
    public Seat findByCoordinate(int hallId, int rowIndex, int colIndex) throws SQLException {
        String sql = """
            SELECT s.*, st.type_name, st.extra_fee
            FROM seats s
            JOIN seat_types st ON s.seat_type_id = st.seat_type_id
            WHERE s.hall_id = ? AND s.row_index = ? AND s.column_index = ?
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setInt(2, rowIndex);
            ps.setInt(3, colIndex);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    /* =========================
       14. CẬP NHẬT HÀNG LOẠT LOẠI GHẾ (dùng khi chọn nhiều ghế cùng lúc)
       ========================= */
    public void batchUpdateSeatType(List<Integer> seatIds, int seatTypeId) throws SQLException {
        String sql = "UPDATE seats SET seat_type_id = ? WHERE seat_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            for (Integer seatId : seatIds) {
                ps.setInt(1, seatTypeId);
                ps.setInt(2, seatId);
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();

        } catch (SQLException e) {
            throw e;
        }
    }

    /* =========================
       15. LẤY DANH SÁCH GHẾ THEO LOẠI
       ========================= */
    public List<Seat> findBySeatType(int hallId, int seatTypeId) throws SQLException {
        String sql = """
            SELECT s.*, st.type_name, st.extra_fee
            FROM seats s
            JOIN seat_types st ON s.seat_type_id = st.seat_type_id
            WHERE s.hall_id = ? AND s.seat_type_id = ?
            ORDER BY s.row_index, s.column_index
        """;

        List<Seat> list = new ArrayList<>();

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);
            ps.setInt(2, seatTypeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
    /* ========
    16. CẬP NHẬT HÀNG LOẠT TRẠNG THÁI GHẾ (THÊM MỚI)
       ========================= */
    public void batchUpdateSeatStatus(List<Integer> seatIds, boolean active) throws SQLException {
        String sql = "UPDATE seats SET is_active = ? WHERE seat_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            for (Integer seatId : seatIds) {
                ps.setBoolean(1, active);
                ps.setInt(2, seatId);
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            throw e;
        }
    }
    public List<SeatSelectionDTO> getSeatsByShowtime(int showtimeId) {
    List<SeatSelectionDTO> list = new ArrayList<>();

    String sql = """
        SELECT
            s.seat_id,
            s.seat_code,
            s.row_index,
            s.column_index,
            s.seat_type_id,
            (ts.slot_price + st.extra_fee) AS price,
            CASE
                WHEN td.seat_id IS NULL THEN 'AVAILABLE'
                ELSE 'BOOKED'
            END AS seat_status
        FROM showtimes sh
        JOIN cinema_halls h ON sh.hall_id = h.hall_id
        JOIN seats s ON h.hall_id = s.hall_id
        JOIN seat_types st ON s.seat_type_id = st.seat_type_id
        JOIN time_slots ts ON sh.slot_id = ts.slot_id
        LEFT JOIN ticket_details td
               ON td.seat_id = s.seat_id
              AND td.showtime_id = sh.showtime_id
        WHERE sh.showtime_id = ?
          AND s.is_active = 1
        ORDER BY s.row_index, s.column_index
    """;

    try (Connection con = DBConnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, showtimeId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new SeatSelectionDTO(
                rs.getInt("seat_id"),
                rs.getString("seat_code"),
                rs.getInt("row_index"),
                rs.getInt("column_index"),
                rs.getInt("seat_type_id"), 
                rs.getDouble("price"),
                rs.getString("seat_status")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


    
    /* =========================
       8. MAP RESULTSET → OBJECT
       ========================= */
    private Seat mapRow(ResultSet rs) throws SQLException {

        Seat seat = new Seat();
        seat.setSeatId(rs.getInt("seat_id"));
        seat.setHallId(rs.getInt("hall_id"));
        seat.setSeatCode(rs.getString("seat_code"));
        seat.setRowIndex(rs.getInt("row_index"));
        seat.setColumnIndex(rs.getInt("column_index"));
        seat.setActive(rs.getBoolean("is_active"));

        SeatType type = new SeatType();
        type.setSeatTypeId(rs.getInt("seat_type_id"));
        type.setTypeName(rs.getString("type_name"));
        type.setExtraFee(rs.getBigDecimal("extra_fee"));

        seat.setSeatType(type);

        return seat;
    }
}