This file is a merged representation of a subset of the codebase, containing specifically included files, combined into a single document by Repomix.

# File Summary

## Purpose
This file contains a packed representation of a subset of the repository's contents that is considered the most important context.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Only files matching these patterns are included: src/main/java/ai/CineAgentProvider.java, src/main/java/ai/skills/user/InfoBotSkills.java, src/main/java/ai/skills/user/BookBotSkills.java, src/main/java/ai/skills/admin/AnalystBotSkills.java, src/main/java/ai/skills/admin/MarketingBotSkills.java, src/main/java/ai/skills/admin/ModerateBotSkills.java, src/main/java/controller/ChatServlet.java, src/main/java/dao/ChatMessageDAO.java, src/main/java/dao/DBConnect.java, src/main/java/dao/MovieDAO.java, src/main/java/dao/ShowtimeDAO.java, src/main/java/dao/SeatDAO.java, src/main/java/dao/InvoiceDAO.java, src/main/java/dao/ProductDAO.java, src/main/java/dao/TicketDetailDAO.java, src/main/java/util/ConfigLoader.java, src/main/webapp/views/common/ai-chat-widget.jsp
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Files are sorted by Git change count (files with more changes are at the bottom)

# Directory Structure
```
src/main/java/ai/CineAgentProvider.java
src/main/java/ai/skills/admin/AnalystBotSkills.java
src/main/java/ai/skills/admin/MarketingBotSkills.java
src/main/java/ai/skills/admin/ModerateBotSkills.java
src/main/java/ai/skills/user/BookBotSkills.java
src/main/java/ai/skills/user/InfoBotSkills.java
src/main/java/controller/ChatServlet.java
src/main/java/dao/ChatMessageDAO.java
src/main/java/dao/DBConnect.java
src/main/java/dao/InvoiceDAO.java
src/main/java/dao/MovieDAO.java
src/main/java/dao/ProductDAO.java
src/main/java/dao/SeatDAO.java
src/main/java/dao/ShowtimeDAO.java
src/main/java/dao/TicketDetailDAO.java
src/main/java/util/ConfigLoader.java
src/main/webapp/views/common/ai-chat-widget.jsp
```

# Files

## File: src/main/java/dao/ProductDAO.java
```java
package dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String INSERT_SQL
            = "INSERT INTO products (item_name, price, stock_quantity, img_user_url) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_SQL
        = "UPDATE products SET item_name = ?, price = ?, stock_quantity = ?, img_user_url = ? WHERE item_id = ?";


    private static final String DELETE_SQL
            = "DELETE FROM products WHERE item_id = ?";

    private static final String SELECT_BY_ID_SQL
            = "SELECT * FROM products WHERE item_id = ?";

    private static final String SELECT_ALL_SQL
            = "SELECT * FROM products";

    private static final String UPDATE_STOCK_SQL
            = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
    private static final String DELETE_PRODUCT_DETAILS_SQL
            = "DELETE FROM products_details WHERE item_id = ?";

    // =========================
    // 1. Thêm sản phẩm (Admin)
    // =========================
    public void insert(Product p) throws SQLException {
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, p.getItemName());
            ps.setBigDecimal(2, p.getPrice());
            ps.setInt(3, p.getStockQuantity());
            ps.setString(4, p.getProductImgUrl()); // 
            ps.executeUpdate();
        }
    }

    // =========================
    // 2. Cập nhật sản phẩm
    // =========================
    public void update(Product p) throws SQLException {
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

        ps.setString(1, p.getItemName());
        ps.setBigDecimal(2, p.getPrice());
        ps.setInt(3, p.getStockQuantity());
        ps.setString(4, p.getProductImgUrl()); // tên file ảnh
        ps.setInt(5, p.getItemId());

        ps.executeUpdate();
    }
}

    // =========================
    // 3. Xóa sản phẩm
    // =========================
    public void delete(int itemId) throws SQLException {

        Connection conn = null;
        PreparedStatement psDetail = null;
        PreparedStatement psProduct = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

            // 1. Xóa bảng con trước
            psDetail = conn.prepareStatement(DELETE_PRODUCT_DETAILS_SQL);
            psDetail.setInt(1, itemId);
            psDetail.executeUpdate();

            // 2. Xóa bảng products
            psProduct = conn.prepareStatement(DELETE_SQL);
            psProduct.setInt(1, itemId);
            psProduct.executeUpdate();

            conn.commit(); // OK → LƯU

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // LỖI → QUAY LẠI
            }
            throw e;
        } finally {
            if (psDetail != null) {
                psDetail.close();
            }
            if (psProduct != null) {
                psProduct.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // =========================
    // 4. Tìm theo ID
    // =========================
    public Product findById(int itemId) throws SQLException {
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // =========================
    // 5. Lấy tất cả sản phẩm
    // =========================
    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // =========================
    // 6. Cập nhật tồn kho (+ / -)
    // =========================
    public void updateStock(Connection conn, int itemId, int quantity) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_STOCK_SQL)) {
            ps.setInt(1, quantity); // âm = trừ kho, dương = cộng kho
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    // =========================
    // mapRow
    // =========================
    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setItemId(rs.getInt("item_id"));
        p.setItemName(rs.getString("item_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setProductImgUrl(rs.getString("img_user_url")); // ⭐ QUYẾT ĐỊNH
        return p;
    }

}
```

## File: src/main/java/dao/SeatDAO.java
```java
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
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
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
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
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
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
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
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
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
            try {
                for (Integer seatId : seatIds) {
                    ps.setInt(1, seatTypeId);
                    ps.setInt(2, seatId);
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
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
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
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
            try {
                for (Integer seatId : seatIds) {
                    ps.setBoolean(1, active);
                    ps.setInt(2, seatId);
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

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
        try (ResultSet rs = ps.executeQuery()) {
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
```

## File: src/main/java/dao/ShowtimeDAO.java
```java
package dao;

import model.Showtime;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public Showtime findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM showtimes WHERE showtime_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /* =========================
       FIND BY MOVIE
       ========================= */
    public List<Showtime> findByMovie(Connection conn, int movieId) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE movie_id = ?
            ORDER BY show_date, slot_id
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /* =========================
       FIND BY DATE
       ========================= */
    public List<Showtime> findByDate(Connection conn, LocalDate date) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE show_date = ?
            ORDER BY slot_id
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /* =========================
       FIND UPCOMING
       ========================= */
    public List<Showtime> findUpcoming(Connection conn) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE show_date >= CAST(GETDATE() AS DATE)
            ORDER BY show_date, slot_id
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /* =========================
       INSERT
       ========================= */
    public void insert(Connection conn, Showtime st) throws SQLException {
        String sql = """
            INSERT INTO showtimes (movie_id, hall_id, show_date, slot_id)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, st.getMovieId());
            ps.setInt(2, st.getHallId());
            ps.setDate(3, Date.valueOf(st.getShowDate()));
            ps.setInt(4, st.getSlotId());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE (ADMIN)
       ========================= */
    public void update(Connection conn, Showtime st) throws SQLException {
        String sql = """
            UPDATE showtimes
            SET movie_id = ?, hall_id = ?, show_date = ?, slot_id = ?
            WHERE showtime_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, st.getMovieId());
            ps.setInt(2, st.getHallId());
            ps.setDate(3, Date.valueOf(st.getShowDate()));
            ps.setInt(4, st.getSlotId());
            ps.setInt(5, st.getShowtimeId());
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE (ADMIN)
       ========================= */
    public boolean delete(Connection conn, int showtimeId) throws SQLException {
    String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, showtimeId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}


    /* =========================
       MAP RESULTSET
       ========================= */
    private Showtime mapRow(ResultSet rs) throws SQLException {
        Showtime st = new Showtime();
        st.setShowtimeId(rs.getInt("showtime_id"));
        st.setMovieId(rs.getInt("movie_id"));
        st.setHallId(rs.getInt("hall_id"));
        st.setShowDate(rs.getDate("show_date").toLocalDate());
        st.setSlotId(rs.getInt("slot_id"));
        return st;
    }
}
```

## File: src/main/java/dao/TicketDetailDAO.java
```java
package dao;

import model.TicketDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDetailDAO {

    private static final String INSERT_SQL = "INSERT INTO ticket_details (invoice_id, seat_id, showtime_id, actual_price) "
            +
            "VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_INVOICE_SQL = "SELECT * FROM ticket_details WHERE invoice_id = ?";

    private static final String DELETE_BY_INVOICE_SQL = "DELETE FROM ticket_details WHERE invoice_id = ?";

    private static final String CHECK_SEAT_SQL = "SELECT 1 FROM ticket_details WHERE showtime_id = ? AND seat_id = ?";

    // =========================
    // 1. Đặt 1 ghế
    // =========================
    public void insert(Connection conn, TicketDetail t) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, t.getInvoiceId());
            ps.setInt(2, t.getSeatId());
            ps.setInt(3, t.getShowtimeId());
            ps.setBigDecimal(4, t.getActualPrice());
            ps.executeUpdate(); // nếu trùng ghế → SQLException
        }
    }

    // =========================
    // 2. Đặt nhiều ghế 1 lần (batch)
    // =========================
    public void insertBatch(Connection conn, List<TicketDetail> list) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            for (TicketDetail t : list) {
                ps.setInt(1, t.getInvoiceId());
                ps.setInt(2, t.getSeatId());
                ps.setInt(3, t.getShowtimeId());
                ps.setBigDecimal(4, t.getActualPrice());
                ps.addBatch();
            }
            ps.executeBatch(); // ❗ SQLException nếu có ghế trùng
        }
    }

    // =========================
    // 3. Kiểm tra ghế đã đặt chưa
    // =========================
    public boolean existsSeat(Connection conn, int showtimeId, int seatId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(CHECK_SEAT_SQL)) {
            ps.setInt(1, showtimeId);
            ps.setInt(2, seatId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // =========================
    // 4. Lấy danh sách ghế theo hóa đơn
    // =========================
    public List<TicketDetail> getByInvoice(int invoiceId) throws SQLException {
        List<TicketDetail> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_INVOICE_SQL)) {

            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // =========================
    // 5. Xóa ghế theo hóa đơn (rollback)
    // =========================
    public void deleteByInvoice(Connection conn, int invoiceId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(DELETE_BY_INVOICE_SQL)) {
            ps.setInt(1, invoiceId);
            ps.executeUpdate();
        }
    }

    // =========================
    // mapRow
    // =========================
    private TicketDetail mapRow(ResultSet rs) throws SQLException {
        TicketDetail t = new TicketDetail();
        t.setInvoiceId(rs.getInt("invoice_id"));
        t.setSeatId(rs.getInt("seat_id"));
        t.setShowtimeId(rs.getInt("showtime_id"));
        t.setActualPrice(rs.getBigDecimal("actual_price"));
        return t;
    }
}
```

## File: src/main/java/util/ConfigLoader.java
```java
package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load application configuration from properties file.
 */
public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find db.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
```

## File: src/main/java/ai/skills/admin/ModerateBotSkills.java
```java
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
```

## File: src/main/webapp/views/common/ai-chat-widget.jsp
```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="chat-widget-container" id="chatWidgetContainer">
    <!-- Chat Toggle Button -->
    <button class="chat-toggle-btn" id="chatToggleBtn" onclick="toggleChat()">
        <i class="fa-solid fa-robot forced-fa6"></i>
        <span class="chat-badge" id="chatBadge" style="display: none;">1</span>
    </button>

    <!-- Chat Box -->
    <div class="chat-box" id="chatBox">
        <div class="chat-header">
            <div class="d-flex align-items-center gap-2">
                <div class="bot-avatar">
                    <i class="fa-solid fa-robot text-white forced-fa6"></i>
                </div>
                <div>
                    <h6 class="mb-0 fw-bold text-white">Cine AI Assistant</h6>
                    <small class="text-white-50">Online</small>
                </div>
            </div>
            <div class="d-flex gap-2">
                <button class="btn btn-sm text-white-50 hover-white" onclick="resetChat()" title="Reset session">
                    <i class="fa-solid fa-rotate-right"></i>
                </button>
                <button class="btn btn-sm text-white-50 hover-white" onclick="toggleChat()">
                    <i class="fa-solid fa-times"></i>
                </button>
            </div>
        </div>

        <div class="chat-messages" id="chatMessages">
            <div class="message bot-message">
                <div class="message-avatar">
                    <i class="fa-solid fa-robot"></i>
                </div>
                <div class="message-content">
                    Xin chào! Tôi là trợ lý AI của rạp chiếu phim. Bạn cần hỗ trợ gì hôm nay (tìm phim, lịch chiếu, đặt vé...)?
                </div>
            </div>
        </div>

        <div class="chat-input-area">
            <form id="chatForm" onsubmit="handleChatSubmit(event)">
                <div class="input-group">
                    <input type="text" id="chatInput" class="form-control" placeholder="Nhập tin nhắn..." autocomplete="off">
                    <button class="btn btn-primary" type="submit" id="sendBtn">
                        <i class="fa-solid fa-paper-plane"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<style>
/* Chat Widget Styles */
.chat-widget-container {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 9999;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.chat-toggle-btn {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    color: white;
    font-size: 24px;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    cursor: pointer;
    transition: transform 0.3s ease;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-toggle-btn:hover {
    transform: scale(1.1);
}

.chat-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background: #ff4757;
    color: white;
    font-size: 12px;
    font-weight: bold;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #0a0a0a;
}

.chat-box {
    position: absolute;
    bottom: 80px;
    right: 0;
    width: 350px;
    height: 500px;
    background: #1a1a2e;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.5);
    display: none;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid rgba(255,255,255,0.1);
    transform-origin: bottom right;
    animation: scaleIn 0.3s ease;
}

.chat-box.active {
    display: flex;
}

@keyframes scaleIn {
    from { transform: scale(0); opacity: 0; }
    to { transform: scale(1); opacity: 1; }
}

.chat-header {
    background: linear-gradient(135deg, #16222A 0%, #3A6073 100%);
    padding: 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid rgba(255,255,255,0.1);
}

.bot-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-messages {
    flex: 1;
    padding: 15px;
    overflow-y: auto;
    background: #0f0f1a;
    display: flex;
    flex-direction: column;
    gap: 15px;
}

/* Custom scrollbar */
.chat-messages::-webkit-scrollbar { width: 6px; }
.chat-messages::-webkit-scrollbar-track { background: transparent; }
.chat-messages::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.2); border-radius: 10px; }

.message {
    max-width: 85%;
    display: flex;
    flex-direction: column;
}

.user-message {
    align-self: flex-end;
}

.bot-message {
    align-self: flex-start;
}

.message-content {
    padding: 10px 14px;
    border-radius: 15px;
    font-size: 14px;
    line-height: 1.4;
    word-wrap: break-word;
}

.user-message .message-content {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-bottom-right-radius: 4px;
}

.bot-message .message-content {
    background: #2a2a40;
    color: #e0e0e0;
    border-bottom-left-radius: 4px;
    border: 1px solid rgba(255,255,255,0.05);
}

/* Typing indicator */
.typing-indicator {
    display: flex;
    gap: 4px;
    padding: 14px 18px;
    background: #2a2a40;
    border-radius: 15px;
    border-bottom-left-radius: 4px;
    width: fit-content;
}

.typing-dot {
    width: 6px;
    height: 6px;
    background: #8892b0;
    border-radius: 50%;
    animation: typing 1.4s infinite ease-in-out both;
}

.typing-dot:nth-child(1) { animation-delay: -0.32s; }
.typing-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
    0%, 80%, 100% { transform: scale(0); }
    40% { transform: scale(1); }
}

/* Message Avatar */
.message-avatar {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    color: white;
    flex-shrink: 0;
    margin-bottom: 4px;
}

/* Force FA6 rendering */
.forced-fa6 {
    font-family: "Font Awesome 6 Free" !important;
    font-weight: 900 !important;
    display: inline-block !important;
    font-style: normal !important;
    font-variant: normal !important;
    text-rendering: auto !important;
    -webkit-font-smoothing: antialiased !important;
}

.bot-message {
    flex-direction: row !important;
    gap: 8px;
}

.user-message {
    align-items: flex-end;
}

/* Interactive Actions (JSON payload) */
.action-card {
    background: rgba(102, 126, 234, 0.1);
    border: 1px solid #667eea;
    border-radius: 10px;
    padding: 12px;
    margin-top: 10px;
}

.action-btn {
    width: 100%;
    margin-top: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    padding: 8px;
    color: white;
    border-radius: 6px;
    font-weight: 600;
    font-size: 13px;
    transition: opacity 0.3s;
}

.action-btn:hover { opacity: 0.9; }

.chat-input-area {
    padding: 15px;
    background: #1a1a2e;
    border-top: 1px solid rgba(255,255,255,0.1);
}

.chat-input-area .form-control {
    background: rgba(255,255,255,0.05);
    border: 1px solid rgba(255,255,255,0.1);
    color: white;
}

.chat-input-area .form-control:focus {
    background: rgba(255,255,255,0.08);
    border-color: #667eea;
    box-shadow: none;
    color: white;
}

.chat-input-area .btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
}

.hover-white:hover {
    color: white !important;
}

@media (max-width: 480px) {
    .chat-box {
        width: calc(100vw - 40px);
        bottom: 90px;
        right: 20px;
        height: 60vh;
    }
}
</style>

<script>
    // Context Path from global or default logic
    const chatContextPath = '${pageContext.request.contextPath}';
    console.log("Cine AI Widget initialized with context path:", chatContextPath);

    function toggleChat() {
        const chatBox = document.getElementById('chatBox');
        chatBox.classList.toggle('active');
        if(chatBox.classList.contains('active')) {
            document.getElementById('chatBadge').style.display = 'none';
            document.getElementById('chatInput').focus();
        }
    }

    function appendMessage(sender, content, isHtml = false) {
        const chatMessages = document.getElementById('chatMessages');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + (sender === 'user' ? 'user-message' : 'bot-message');
        
        if (sender === 'bot') {
            const avatarDiv = document.createElement('div');
            avatarDiv.className = 'message-avatar';
            avatarDiv.innerHTML = '<i class="fa-solid fa-robot forced-fa6"></i>';
            msgDiv.appendChild(avatarDiv);
        }

        const contentDiv = document.createElement('div');
        contentDiv.className = 'message-content';
        
        if (isHtml) {
            contentDiv.innerHTML = content;
        } else {
            contentDiv.textContent = content;
        }
        
        msgDiv.appendChild(contentDiv);
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        return contentDiv; 
    }

    function showTyping() {
        const chatMessages = document.getElementById('chatMessages');
        const typingDiv = document.createElement('div');
        typingDiv.className = 'message bot-message typing-indicator-wrapper';
        typingDiv.id = 'typingIndicator';
        typingDiv.innerHTML = `
            <div class="typing-indicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        `;
        chatMessages.appendChild(typingDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function removeTyping() {
        const typingItem = document.getElementById('typingIndicator');
        if (typingItem) typingItem.remove();
    }

    async function handleChatSubmit(e) {
        e.preventDefault();
        const input = document.getElementById('chatInput');
        const message = input.value.trim();
        if (!message) return;

        input.value = '';
        appendMessage('user', message);
        document.getElementById('sendBtn').disabled = true;
        
        // Start streaming logic
        showTyping();
        
        try {
            // Initiate SSE connection
            const response = await fetch(chatContextPath + '/ChatServlet/stream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ 'message': message })
            });

            if (!response.ok) {
                const errorText = await response.text().catch(() => "Unknown error");
                throw new Error(`Server returned status \${response.status}: \${errorText}`);
            }

            removeTyping();
            
            // Render a placeholder bot message for streaming text
            const botMessageContainer = appendMessage('bot', '');
            let accumulatedJson = ""; // For structured JSON fallback

            // Read SSE stream
            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let done = false;
            let buffer = ""; // Buffer for partial lines

            while (!done) {
                const { value, done: readerDone } = await reader.read();
                done = readerDone;
                if (value) {
                    buffer += decoder.decode(value, { stream: !done });
                    const lines = buffer.split('\n');
                    
                    // Keep the last partial line in the buffer
                    buffer = lines.pop();

                    for (const line of lines) {
                        const trimmedLine = line.trim();
                        if (!trimmedLine || !trimmedLine.startsWith('data: ')) continue;
                        
                        const dataStr = trimmedLine.substring(6);
                        try {
                            const parsed = JSON.parse(dataStr);
                            if (parsed.status === 'complete') {
                                checkStructuredJson(accumulatedJson, botMessageContainer);
                            } else if (parsed.token) {
                                accumulatedJson += parsed.token;
                                // Fix Bug #5: Safe streaming render
                                const rawToken = parsed.token;
                                if (rawToken.includes('\n')) {
                                    // Handle newlines safely
                                    const parts = rawToken.split('\n');
                                    for (let i = 0; i < parts.length; i++) {
                                        botMessageContainer.appendChild(document.createTextNode(parts[i]));
                                        if (i < parts.length - 1) {
                                            botMessageContainer.appendChild(document.createElement('br'));
                                        }
                                    }
                                } else {
                                    botMessageContainer.appendChild(document.createTextNode(rawToken));
                                }
                                document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
                            } else if (parsed.error) {
                                botMessageContainer.innerHTML += `<div class="alert alert-danger p-2 small mt-2">AI Error: ${parsed.error}</div>`;
                            }
                        } catch (err) {
                            console.warn("SSE JSON parse error", err, dataStr);
                        }
                    }
                }
            }
            
            // Process any remaining data in buffer
            if (buffer.trim().startsWith('data: ')) {
                 const dataStr = buffer.trim().substring(6);
                 try {
                     const parsed = JSON.parse(dataStr);
                     if (parsed.token) {
                         const rawToken = parsed.token.replace(/\\n/g, '\n');
                         if (rawToken.includes('\n')) {
                             const parts = rawToken.split('\n');
                             for (let i = 0; i < parts.length; i++) {
                                 botMessageContainer.appendChild(document.createTextNode(parts[i]));
                                 if (i < parts.length - 1) botMessageContainer.appendChild(document.createElement('br'));
                             }
                         } else {
                             botMessageContainer.appendChild(document.createTextNode(rawToken));
                         }
                     }
                 } catch(e) {}
            }
            
        } catch (error) {
            removeTyping();
            appendMessage('bot', 'Xin lỗi, đã có lỗi kết nối tới Server. Vui lòng thử lại sau.', false);
            console.error('Lỗi khi gửi chat:', error);
        } finally {
            document.getElementById('sendBtn').disabled = false;
            document.getElementById('chatInput').focus();
        }
    }

    // Function to handle interactive JSON actions
    function checkStructuredJson(fullText, containerNode) {
        fullText = fullText.trim();
        if (fullText.startsWith('{') && fullText.endsWith('}')) {
            try {
                const data = JSON.parse(fullText);
                if (data.actionType === 'BOOKING_CONFIRM') {
                    // Fix Bug #4: Sync with backend payload keys
                    const details = data.data || {}; 
                    const html = `
                        <div class="mb-2">Đây là thông tin xác nhận đặt vé của bạn:</div>
                        <div class="action-card">
                            <div class="small mb-1"><strong>Phim:</strong> \${details.movieName || 'N/A'}</div>
                            <div class="small mb-1"><strong>Ghế:</strong> \${details.seats || 'N/A'}</div>
                            <div class="small mb-1"><strong>Tổng tiền:</strong> \${Number(details.total).toLocaleString()} VNĐ</div>
                            
                            <button class="action-btn" onclick="executeSilentAction('Tôi xác nhận đặt vé phim \${details.movieName.replace(/'/g, "\\'")} ghế \${details.seats}')">
                                Xác Nhận & Thanh Toán <i class="fa-solid fa-check-circle forced-fa6 ms-1"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-secondary w-100 mt-2" onclick="executeSilentAction('Hủy quá trình đặt vé')">
                                Hủy Bỏ
                            </button>
                        </div>
                    `;
                    containerNode.innerHTML = html;
                }
            } catch (e) {
                // Not valid JSON, leave as text
            }
        }
    }

    async function executeSilentAction(message) {
        const input = document.getElementById('chatInput');
        input.value = message;
        document.getElementById('chatForm').dispatchEvent(new Event('submit'));
    }

    async function resetChat() {
        try {
            const resp = await fetch(chatContextPath + '/ChatServlet/reset', { method: 'POST' });
            if (resp.ok) {
                const msgList = document.getElementById('chatMessages');
                msgList.innerHTML = `
                    <div class="message bot-message">
                        <div class="message-content">
                            Phiên hội thoại đã được làm mới. Bạn cần hỗ trợ gì tiếp theo?
                        </div>
                    </div>`;
            }
        } catch(e) {
            console.error('Reset failed', e);
        }
    }
</script>
```

## File: src/main/java/ai/skills/admin/AnalystBotSkills.java
```java
package ai.skills.admin;

import dev.langchain4j.agent.tool.Tool;
import service.IncomeStatictisService;
import service.TicketManagementService;
import service.SeatFillRate_ViewService;
import service.ForecastService;
import model.Movie_Ticket_ViewDTO;
import model.ForecastResult;
import model.ForecastDTO;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Tools for AnalystBot sub-agent to handle statistics, performance metrics, and forecasting.
 * Now encapsulates the intelligence logic previously in ForecastService.
 */
public class AnalystBotSkills {

    private final IncomeStatictisService incomeService;
    private final TicketManagementService ticketService;
    private final SeatFillRate_ViewService seatService;
    private final ForecastService forecastService;

    public AnalystBotSkills() {
        this.incomeService = new IncomeStatictisService();
        this.ticketService = new TicketManagementService();
        this.seatService = new SeatFillRate_ViewService();
        this.forecastService = new ForecastService();
    }

    public AnalystBotSkills(IncomeStatictisService incomeService, TicketManagementService ticketService, SeatFillRate_ViewService seatService, ForecastService forecastService) {
        this.incomeService = incomeService;
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.forecastService = forecastService;
    }

    @Tool("Lấy báo cáo doanh thu tổng quan bao gồm doanh thu ngày, tháng, năm")
    public String getRevenueSummary() {
        double daily = incomeService.getDaylyRevenue();
        double monthly = incomeService.calculateTotalRevenue();
        double yearly = incomeService.getYearlyRevenue();

        return String.format(
            Locale.GERMAN,
            "Báo cáo doanh thu:\n- Hôm nay: %,.0f VND\n- Tháng này: %,.0f VND\n- Năm nay: %,.0f VND",
            daily, monthly, yearly
        );
    }

    @Tool("Lấy danh sách các phim bán chạy nhất (Top Movies)")
    public String getTopPerformingMovies() {
        try {
            List<Movie_Ticket_ViewDTO> topMovies = ticketService.getAllOfPageNumber(1);
            if (topMovies == null || topMovies.isEmpty()) return "Chưa có dữ liệu phim bán chạy.";

            return topMovies.stream()
                .limit(5)
                .map(m -> String.format(Locale.GERMAN, "- %s: %d vé, %,.0f VND", m.getTitle(), m.getTicketsSold(), m.getRevenue()))
                .collect(Collectors.joining("\n", "Top 5 phim bán chạy nhất:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu phim: " + e.getMessage();
        }
    }

    @Tool("Dự báo doanh thu và số lượng vé bán trong 7 ngày tới (Sử dụng AI)")
    public String get7DayForecast() {
        try {
            // AnalystBot logic: ForecastService now performs the heavy lifting and LLM call
            // We keep the service call but centralize the "Identity" of the analyst here.
            ForecastResult result = forecastService.get7DayForecast();
            List<ForecastDTO> futureData = result.getDailyData().stream()
                .filter(ForecastDTO::isFuture)
                .collect(Collectors.toList());

            double totalRevenue = futureData.stream().mapToDouble(ForecastDTO::getForecastRevenue).sum();
            int totalTickets = futureData.stream().mapToInt(ForecastDTO::getForecastTickets).sum();
            
            return String.format(
                Locale.GERMAN,
                "Dự báo 7 ngày tới:\n- Tổng doanh thu dự kiến: %,.0f VND\n- Tổng số vé dự kiến: %,d vé\n- Phân tích chi tiết: %s",
                totalRevenue, totalTickets, result.getAnalysis()
            );
        } catch (Exception e) {
            return "Lỗi khi dự báo: " + e.getMessage();
        }
    }

    @Tool("Lấy dữ liệu thô (Raw Data) của 14 ngày qua để tự phân tích")
    public String getHistoricalData() {
        try {
            ForecastResult result = forecastService.get7DayForecast();
            return result.getDailyData().stream()
                .filter(d -> !d.isFuture())
                .map(d -> String.format("%s: %,.0f VND, %d vé", d.getDate(), d.getActualRevenue(), d.getActualTickets()))
                .collect(Collectors.joining("\n", "Dữ liệu lịch sử 14 ngày qua:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Lấy tỉ lệ lấp đầy ghế (Seat Fill Rate) trung bình tháng này")
    public String getMonthlySeatFillRate() {
        try {
            double rate = seatService.getSeatFillRateCurrentMonth();
            return String.format("Tỉ lệ lấp đầy ghế trung bình tháng này: %.2f%%", rate * 100);
        } catch (Exception e) {
            return "Lỗi khi lấy tỉ lệ lấp đầy: " + e.getMessage();
        }
    }
}
```

## File: src/main/java/ai/skills/admin/MarketingBotSkills.java
```java
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

    private final MovieDAO movieDAO;

    public MarketingBotSkills() {
        this.movieDAO = new MovieDAO();
    }

    public MarketingBotSkills(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

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
```

## File: src/main/java/ai/skills/user/BookBotSkills.java
```java
package ai.skills.user;

import dao.DBConnect;
import dao.InvoiceDAO;
import dao.SeatDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
import model.BookingHistoryDTO;
import model.Invoice;
import model.SeatSelectionDTO;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

/**
 * Tools for BookBot sub-agent to handle booking flows, history, and interactive confirmations.
 */
public class BookBotSkills {

    private final InvoiceDAO invoiceDAO;
    private final SeatDAO seatDAO;
    private final dao.TicketDetailDAO ticketDAO;
    private final int userId;

    public BookBotSkills(int userId) {
        this.userId = userId;
        this.invoiceDAO = new InvoiceDAO();
        this.seatDAO = new SeatDAO();
        this.ticketDAO = new dao.TicketDetailDAO();
    }

    public BookBotSkills(int userId, InvoiceDAO invoiceDAO, SeatDAO seatDAO, dao.TicketDetailDAO ticketDAO) {
        this.userId = userId;
        this.invoiceDAO = invoiceDAO;
        this.seatDAO = seatDAO;
        this.ticketDAO = ticketDAO;
    }

    protected Connection getConnection() throws java.sql.SQLException {
        return DBConnect.getConnection();
    }

    @Tool("Lấy lịch sử đặt vé của tôi")
    public String getMyBookingHistory() {
        System.out.println("[AI-DEBUG] Tool getMyBookingHistory called for UserID: " + this.userId);
        try {
            List<BookingHistoryDTO> history = invoiceDAO.getBookingHistory(this.userId);
            System.out.println("[AI-DEBUG] Found " + history.size() + " history records");
            if (history.isEmpty()) return "Bạn chưa có giao dịch nào.";

            return history.stream()
                .map(h -> String.format("- %s: %s | Suất chiếu: %s %s | Trạng thái: %s", 
                    h.getMovieTitle(), h.getTicketCode() != null ? h.getTicketCode() : "N/A",
                    h.getShowDate(), h.getStartTime(), h.getStatus()))
                .collect(Collectors.joining("\n", "Lịch sử đặt vé của bạn:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getMyBookingHistory error: " + e.getMessage());
            return "Lỗi khi lấy lịch sử: " + e.getMessage();
        }
    }

    @Tool("Xem sơ đồ ghế và chọn ghế cho suất chiếu (showtimeId)")
    public String getSeatMap(@P("ID suất chiếu (showtimeId)") int showtimeId) {
        System.out.println("[AI-DEBUG] Tool getSeatMap called for ShowtimeID: " + showtimeId);
        try {
            List<SeatSelectionDTO> seats = seatDAO.getSeatsByShowtime(showtimeId);
            System.out.println("[AI-DEBUG] Found " + seats.size() + " seats");
            if (seats.isEmpty()) return "Không tìm thấy sơ đồ ghế cho suất chiếu này.";

            return seats.stream()
                .map(s -> String.format("[%s: %s Price:%,.0f]", s.getSeatCode(), s.getStatus(), s.getPrice()))
                .collect(Collectors.joining(" ", "Sơ đồ ghế (AVAILABLE/BOOKED):\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getSeatMap error: " + e.getMessage());
            return "Lỗi khi lấy sơ đồ ghế: " + e.getMessage();
        }
    }

    @Tool("Chuẩn bị xác nhận đặt vé (Cần showtimeId, movieName và danh sách seatIds)")
    public String prepareBooking(@P("ID suất chiếu") int showtimeId, @P("Tên phim") String movieName, @P("Mã ghế (ví dụ: A1, A2)") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool prepareBooking called: " + showtimeId + ", " + seatCodes);
        return String.format(
            "{\"actionType\": \"BOOKING_CONFIRM\", \"data\": {\"showtimeId\": %d, \"movieName\": \"%s\", \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Vui lòng xác nhận để tiến hành đặt ghế và thanh toán.\"}",
            showtimeId, movieName, seatCodes, totalAmount
        );
    }

    @Tool("Xác nhận và tiến hành đặt vé sau khi người dùng đã đồng ý (Cần showtimeId, seatCodes, totalAmount)")
    public String confirmBooking(@P("ID suất chiếu") int showtimeId, @P("Mã ghế") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool confirmBooking called for UserID: " + this.userId + ", Showtime: " + showtimeId + ", Seats: " + seatCodes);
        
        if (this.userId <= 0) {
            return "Vui lòng đăng nhập để thực hiện đặt vé.";
        }
        
        try {
            // 1. Prepare seats to register
            String[] targetCodesArray = seatCodes.toUpperCase().split("[,\\s/]+");
            java.util.Set<String> targetCodes = java.util.Arrays.stream(targetCodesArray)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());
                
            List<SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(showtimeId);
            List<SeatSelectionDTO> matchedSeats = allSeats.stream()
                .filter(s -> targetCodes.contains(s.getSeatCode().toUpperCase()))
                .collect(Collectors.toList());

            if (matchedSeats.size() < targetCodes.size()) {
                return "Một số ghế bạn chọn không tồn tại trong hệ thống. Vui lòng kiểm tra sơ đồ ghế.";
            }
            
            // Check availability
            for (SeatSelectionDTO seat : matchedSeats) {
                if ("BOOKED".equalsIgnoreCase(seat.getStatus())) {
                    return "Ghế " + seat.getSeatCode() + " đã có người đặt trước. Vui lòng chọn ghế khác.";
                }
            }

            // 2. Create Invoice object
            Invoice invoice = new Invoice();
            invoice.setUserId(this.userId);
            invoice.setShowtimeId(showtimeId);
            invoice.setTotalAmount(new java.math.BigDecimal(totalAmount));
            invoice.setStatus("PENDING");
            
            // 3. Persist to DB
            try (Connection conn = getConnection()) {
                if (conn == null) throw new Exception("Không thể kết nối database.");
                conn.setAutoCommit(false);
                try {
                    int invoiceId = invoiceDAO.insert(conn, invoice);
                    
                    // 4. Register tickets
                    List<model.TicketDetail> tickets = matchedSeats.stream().map(s -> {
                        model.TicketDetail t = new model.TicketDetail();
                        t.setInvoiceId(invoiceId);
                        t.setSeatId(s.getSeatId());
                        t.setShowtimeId(showtimeId);
                        t.setActualPrice(new java.math.BigDecimal(s.getPrice()));
                        return t;
                    }).collect(Collectors.toList());
                    
                    ticketDAO.insertBatch(conn, tickets);
                    
                    conn.commit();
                    return String.format(
                        "{\"actionType\": \"BOOKING_SUCCESS\", \"data\": {\"invoiceId\": %d, \"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Đặt vé thành công! Mã hóa đơn của bạn là #%d. Vui lòng hoàn tất thanh toán.\"}",
                        invoiceId, showtimeId, seatCodes, totalAmount, invoiceId
                    );
                } catch (Exception e) {
                    if (conn != null) conn.rollback();
                    throw e;
                }
            }
        } catch (Exception e) {
            System.err.println("[AI-ERROR] confirmBooking error: " + e.getMessage());
            e.printStackTrace();
            return "Lỗi khi xử lý đặt vé: " + e.getMessage();
        }
    }

    @Tool("Huỷ hoá đơn hoặc booking đang chờ (invoiceId)")
    public String cancelInvoice(@P("ID hóa đơn (invoiceId)") int invoiceId) {
        System.out.println("[AI-DEBUG] Tool cancelInvoice called for InvoiceID: " + invoiceId);
        try {
            invoiceDAO.updateStatus(invoiceId, "Canceled");
            return "Đã hủy hóa đơn thành công.";
        } catch (Exception e) {
            System.err.println("[AI-ERROR] cancelInvoice error: " + e.getMessage());
            return "Lỗi khi hủy hóa đơn: " + e.getMessage();
        }
    }
}
```

## File: src/main/java/ai/skills/user/InfoBotSkills.java
```java
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

    public InfoBotSkills() {
        this.movieDAO = new MovieDAO();
        this.showtimeDAO = new ShowtimeDAO();
        this.productDAO = new ProductDAO();
    }

    public InfoBotSkills(MovieDAO movieDAO, ShowtimeDAO showtimeDAO, ProductDAO productDAO) {
        this.movieDAO = movieDAO;
        this.showtimeDAO = showtimeDAO;
        this.productDAO = productDAO;
    }

    @Tool("Tìm kiếm phim theo tên hoặc từ khóa liên quan")
    public String searchMovies(@P("Tên phim hoặc từ khóa tìm kiếm") String query) {
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
```

## File: src/main/java/dao/InvoiceDAO.java
```java
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Invoice;

public class InvoiceDAO {

    public int insert(Connection conn, Invoice invoice) throws SQLException {

        String sql = """
                    INSERT INTO invoices
                    (user_id, showtime_id, booking_time, expiry_time, total_amount, status)
                    VALUES (?, ?, GETDATE(), DATEADD(MINUTE, 5, GETDATE()), ?, 'PENDING')
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getUserId());
            ps.setInt(2, invoice.getShowtimeId());
            ps.setBigDecimal(3, invoice.getTotalAmount());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        }

        throw new SQLException("Insert invoice failed");
    }

    public void cleanupExpiredInvoices() {
        String deleteTicket = """
                    DELETE td
                    FROM ticket_details td
                    JOIN invoices i ON td.invoice_id = i.invoice_id
                    WHERE i.status = 'Pending'
                      AND i.expiry_time < GETDATE()
                """;

        String cancelInvoice = """
                    UPDATE invoices
                    SET status = 'Canceled'
                    WHERE status = 'Pending'
                      AND expiry_time < GETDATE()
                """;

        try (Connection con = DBConnect.getConnection();
                Statement st = con.createStatement()) {

            st.executeUpdate(deleteTicket);
            st.executeUpdate(cancelInvoice);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public model.Invoice findById(int invoiceId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getInt("invoice_id"));
                inv.setUserId(rs.getInt("user_id"));
                inv.setShowtimeId(rs.getInt("showtime_id"));
                inv.setTotalAmount(rs.getBigDecimal("total_amount"));
                inv.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
                inv.setStatus(rs.getString("status"));
                return inv;
            }
        }
        return null;
    }

    public void updateStatus(int invoiceId, String status) throws SQLException {
        String sql = "UPDATE invoices SET status = ? WHERE invoice_id = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            ps.executeUpdate();
        }
    }

    /*
     * =========================
     * UPDATE TOTAL AMOUNT
     * =========================
     */
    public void updateTotalAmount(Connection conn, int invoiceId, double total)
            throws SQLException {

        String sql = "UPDATE invoices SET total_amount = ? WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, total);
            ps.setInt(2, invoiceId);
            ps.executeUpdate();
        }
    }

    /*
     * =========================
     * AUTO CANCEL EXPIRED
     * =========================
     */
    public void cancelExpiredInvoices(Connection conn) throws SQLException {
        String sql = """
                    UPDATE invoices
                    SET status = 'Canceled'
                    WHERE status = 'Pending'
                      AND expiry_time < GETDATE()
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    /*
     * =========================
     * MAP RESULTSET
     * =========================
     */
    private Invoice mapRow(ResultSet rs) throws SQLException {
        Invoice inv = new Invoice();
        inv.setInvoiceId(rs.getInt("invoice_id"));
        inv.setUserId(rs.getInt("user_id"));
        inv.setShowtimeId(rs.getInt("showtime_id"));
        inv.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());

        Timestamp exp = rs.getTimestamp("expiry_time");
        if (exp != null) {
            inv.setExpiryTime(exp.toLocalDateTime());
        }

        inv.setStatus(rs.getString("status"));
        inv.setTotalAmount(rs.getBigDecimal("total_amount"));
        inv.setTicketCode(rs.getString("ticket_code"));
        return inv;
    }

    // bo sung by Dat
    // ham lay tong doanh thu
    public double calculateRevenue() {
        String GET_MONTHLY_REVENUE = "SELECT ISNULL(SUM(total_amount), 0) AS monthly_revenue " +
                "FROM invoices " +
                "WHERE status = N'Paid' " +
                "AND booking_time >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) " +
                "AND booking_time < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))";
        double revenue = 0;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_MONTHLY_REVENUE);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                revenue = rs.getDouble("monthly_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // lay tong tien do an trong thang do ban duoc
    public double getMonthlyProductRevenue() {
        String GET_MONTHLY_PRODUCT_REVENUE = "SELECT ISNULL(SUM(pd.quantity * p.price), 0) AS product_revenue " +
                "FROM invoices i " +
                "JOIN products_details pd ON i.invoice_id = pd.invoice_id " +
                "JOIN products p ON pd.item_id = p.item_id " +
                "WHERE i.status = N'Paid' " +
                "AND i.booking_time >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) " +
                "AND i.booking_time <  DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))";
        double revenue = 0;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_MONTHLY_PRODUCT_REVENUE);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                revenue = rs.getDouble("product_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // lay tong tien ban ve trong do
    public double getMonthlyTicketRevenue() {
        String GET_MONTHLY_TICKET_REVENUE = "SELECT ISNULL(SUM(td.actual_price), 0) AS ticket_revenue " +
                "FROM invoices i " +
                "JOIN ticket_details td ON i.invoice_id = td.invoice_id " +
                "WHERE i.status = N'Paid' " +
                "AND i.booking_time >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) " +
                "AND i.booking_time <  DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))";
        double revenue = 0;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_MONTHLY_TICKET_REVENUE);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                revenue = rs.getDouble("ticket_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // lay tong doanh thu trong ngay
    public double getDailyRevenue() {
        String GET_DAILY_REVENUE = "SELECT ISNULL(SUM(total_amount), 0) AS daily_revenue " +
                "FROM invoices " +
                "WHERE status = N'Paid' " +
                "AND CAST(booking_time AS DATE) = CAST(GETDATE() AS DATE)";

        double revenue = 0;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_DAILY_REVENUE);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("daily_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // lay tong doanh thu trong nam
    public double getYearlyRevenue() {
        String GET_YEARLY_REVENUE = "SELECT ISNULL(SUM(total_amount), 0) AS yearly_revenue " +
                "FROM invoices " +
                "WHERE status = N'Paid' " +
                "AND booking_time >= DATEFROMPARTS(YEAR(GETDATE()), 1, 1) " +
                "AND booking_time <  DATEADD(YEAR, 1, DATEFROMPARTS(YEAR(GETDATE()), 1, 1))";

        double revenue = 0;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_YEARLY_REVENUE);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("yearly_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    /**
     * Get booking history for a specific user
     * 
     * @param userId User's account ID
     * @return List of BookingHistoryDTO ordered by booking time (newest first)
     */
    public List<model.BookingHistoryDTO> getBookingHistory(int userId) {
        String sql = """
                    SELECT
                        i.invoice_id,
                        i.booking_time,
                        i.status,
                        i.total_amount,
                        i.ticket_code,
                        m.title AS movie_title,
                        m.poster_url,
                        s.show_date,
                        sl.start_time,
                        sl.end_time,
                        ch.hall_name,
                        STRING_AGG(se.seat_code, ', ') AS seat_codes
                    FROM invoices i
                    JOIN showtimes s ON i.showtime_id = s.showtime_id
                    JOIN movies m ON s.movie_id = m.movie_id
                    JOIN cinema_halls ch ON s.hall_id = ch.hall_id
                    JOIN time_slots sl ON s.slot_id = sl.slot_id
                    LEFT JOIN ticket_details td ON i.invoice_id = td.invoice_id
                    LEFT JOIN seats se ON td.seat_id = se.seat_id
                    WHERE i.user_id = ?
                    GROUP BY i.invoice_id, i.booking_time, i.status, i.total_amount, i.ticket_code,
                             m.title, m.poster_url, s.show_date, sl.start_time, sl.end_time, ch.hall_name
                    ORDER BY i.booking_time DESC
                """;

        List<model.BookingHistoryDTO> history = new ArrayList<>();

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            System.out.println("DEBUG - InvoiceDAO.getBookingHistory() - userId: " + userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.BookingHistoryDTO dto = new model.BookingHistoryDTO();
                dto.setInvoiceId(rs.getInt("invoice_id"));
                dto.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
                dto.setStatus(rs.getString("status"));
                dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                dto.setTicketCode(rs.getString("ticket_code"));
                dto.setMovieTitle(rs.getString("movie_title"));
                dto.setPosterUrl(rs.getString("poster_url"));
                dto.setShowDate(rs.getDate("show_date").toLocalDate());
                dto.setStartTime(rs.getTime("start_time").toLocalTime());
                dto.setEndTime(rs.getTime("end_time").toLocalTime());
                dto.setHallName(rs.getString("hall_name"));
                dto.setSeatCodes(rs.getString("seat_codes"));

                history.add(dto);
            }

            System.out.println("DEBUG - InvoiceDAO.getBookingHistory() - Found " + history.size() + " bookings");
        } catch (SQLException e) {
            System.err.println("ERROR - InvoiceDAO.getBookingHistory() - SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Get daily revenue history for the last N days
     */
    public Map<LocalDate, Double> getDailyRevenueHistory(int days) {
        String sql = """
                    SELECT CAST(booking_time AS DATE) as gap_date, SUM(total_amount) as daily_revenue
                    FROM invoices
                    WHERE status = 'Paid'
                      AND booking_time >= DATEADD(DAY, -?, GETDATE())
                    GROUP BY CAST(booking_time AS DATE)
                    ORDER BY gap_date ASC
                """;
        Map<LocalDate, Double> history = new LinkedHashMap<>();
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.put(rs.getDate("gap_date").toLocalDate(), rs.getDouble("daily_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

}
```

## File: src/main/java/dao/MovieDAO.java
```java
package dao;

import model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public Movie findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
        try (Connection con = DBConnect.getConnection()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        }
        return null;
    }

    /* =========================
       FIND ALL
       ========================= */
    public List<Movie> findAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM movies ORDER BY release_date DESC";
        List<Movie> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       SEARCH BY TITLE (SQL)
       ========================= */
    public List<Movie> searchByTitle(Connection conn, String keyword) throws SQLException {
        String sql = "SELECT * FROM movies WHERE title LIKE ?";
        List<Movie> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /* =========================
       INSERT (ADMIN)
       ========================= */
    public void insert(Connection conn, Movie movie) throws SQLException {
        String sql = """
            INSERT INTO movies
            (title, duration, description, release_date, age_rating, poster_url)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE (ADMIN)
       ========================= */
    public void update(Connection conn, Movie movie) throws SQLException {
        String sql = """
            UPDATE movies
            SET title = ?, duration = ?, description = ?, 
                release_date = ?, age_rating = ?, poster_url = ?
            WHERE movie_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());
            ps.setInt(7, movie.getMovieId());
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE (ADMIN)
       ========================= */
    public void delete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie m = new Movie();
        m.setMovieId(rs.getInt("movie_id"));
        m.setTitle(rs.getString("title"));
        m.setDuration(rs.getInt("duration"));
        m.setDescription(rs.getString("description"));
        Date d = rs.getDate("release_date");
        if (d != null) {
            m.setReleaseDate(d.toLocalDate());
        }
        m.setAgeRating(rs.getString("age_rating"));
        m.setPosterUrl(rs.getString("poster_url"));
        return m;
    }

    // lay toan bo film
    public List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY release_date DESC";

        System.out.println("MovieDAO: getAllMovies() connecting...");
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) {
                System.out.println("MovieDAO: Connection failed (null)");
                return list;
            }
            System.out.println("MovieDAO: Connection established. Executing query...");
            try (PreparedStatement ps = conn.prepareStatement(sql); 
                 ResultSet rs = ps.executeQuery()) {
                System.out.println("MovieDAO: Query executed. Mapping rows...");
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                System.out.println("MovieDAO: Done. Found " + list.size() + " movies.");
            }
        } catch (Exception e) {
            System.out.println("MovieDAO Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy phim theo nhiều thể loại (OR logic) Phim sẽ hiển thị nếu thuộc ít
     * nhất 1 trong các genre được chọn
     */
    public List<Movie> getMoviesByMultipleGenres(String[] genres) {
        List<Movie> list = new ArrayList<>();

        if (genres == null || genres.length == 0) {
            return list;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.* ")
                .append("FROM movie_genre_rel mgl ")
                .append("JOIN movies m ON mgl.movie_id = m.movie_id ")
                .append("JOIN movie_genres mg ON mg.genre_id = mgl.genre_id ")
                .append("WHERE mg.genre_name IN (");

        for (int i = 0; i < genres.length; i++) {
            sql.append("?");
            if (i < genres.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(") ORDER BY m.release_date DESC");

        try (Connection con = DBConnect.getConnection()) {
            if (con == null) return list;
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                for (int i = 0; i < genres.length; i++) {
                    ps.setString(i + 1, genres[i]);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Tìm kiếm phim trong danh sách theo keyword Search trong title và
     * description
     */
    public List<Movie> searchMovies(List<Movie> movies, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return movies;
        }

        List<Movie> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Movie movie : movies) {
            String title = movie.getTitle() != null ? movie.getTitle().toLowerCase() : "";
            String description = movie.getDescription() != null ? movie.getDescription().toLowerCase() : "";

            if (title.contains(lowerKeyword) || description.contains(lowerKeyword)) {
                result.add(movie);
            }
        }

        return result;
    }

    // insert va tra ve id cua phim vua them
    public int insertAndReturnId(Connection conn, Movie movie) throws SQLException {

        String sql = """
        INSERT INTO movies
        (title, duration, description, release_date, age_rating, poster_url)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // movie_id vừa insert
                }
            }
        }

        throw new SQLException("Insert movie failed, no ID returned.");
    }

    public int getGenreIdByMovieId(int movieId) {
        String sql = """
        SELECT movie_genre_id
        FROM movie_genre_rel
        WHERE movie_id = ?
    """;

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("movie_genre_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // không tìm thấy
    }

}
```

## File: src/main/java/ai/CineAgentProvider.java
```java
package ai;

import ai.skills.admin.AnalystBotSkills;
import ai.skills.admin.MarketingBotSkills;
import ai.skills.admin.ModerateBotSkills;
import ai.skills.user.BookBotSkills;
import ai.skills.user.InfoBotSkills;
import dao.ChatMessageDAO;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import util.ConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lớp cung cấp các Agent chuyên biệt sử dụng LangChain4j.
 * Đã hỗ trợ xoay tua API Key và tối ưu hóa Model theo tác vụ.
 */
public class CineAgentProvider {

    // Model constants
    public static final String FAST_MODEL = "llama3-8b-8192";           // Cực nhanh, rẻ, dùng cho query đơn giản
    public static final String VERSATILE_MODEL = "llama-3.3-70b-versatile"; // Cân bằng, dùng cho chatbot chính
    public static final String THINKING_MODEL = "deepseek-r1-distill-llama-70b"; // Thinking model cho Analyst
    
    private static final String GROQ_URL = "https://api.groq.com/openai/v1";

    /**
     * Quản lý xoay tua API Key.
     */
    private static class ApiKeyManager {
        private static final List<String> keys = new ArrayList<>();
        private static final AtomicInteger currentIndex = new AtomicInteger(0);

        static {
            String rawKeys = ConfigLoader.get("ai.api.key");
            if (rawKeys != null) {
                for (String k : rawKeys.split(",")) {
                    if (!k.trim().isEmpty()) keys.add(k.trim());
                }
            }
            if (keys.isEmpty()) {
                System.err.println("[AI-WARN] No API Keys found! AI services will fail.");
            }
        }

        public static String getNextKey() {
            if (keys.isEmpty()) return "";
            int index = currentIndex.getAndIncrement() % keys.size();
            return keys.get(index);
        }

        public static int getKeyCount() {
            return keys.size();
        }
    }

    /**
     * Giao diện chung cho các AI Agent.
     */
    public interface CineAgent {
        String chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Giao diện cho Streaming AI Agent.
     */
    public interface StreamingCineAgent {
        dev.langchain4j.service.TokenStream chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Khởi tạo Agent cho người dùng cuối (Customer).
     * Sử dụng VERSATILE_MODEL để đảm bảo chất lượng phản hồi.
     */
    public static CineAgent createUserAgent(int userId) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(VERSATILE_MODEL)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills(userId))
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. " +
                    "Bạn có 2 bộ phận hỗ trợ:\n" +
                    "1. InfoBot: Tra cứu phim, suất chiếu, giá combo.\n" +
                    "2. BookBot: Hỗ trợ đặt vé, xem lịch sử và chuẩn bị xác nhận đặt vé.\n" +
                    "Khi khách hàng muốn đặt vé, hãy dùng BookBot để lấy sơ đồ ghế, sau đó dùng tool prepareBooking để hiển thị xác nhận."
                )
                .build();
    }

    /**
     * Khởi tạo Agent cho quản trị viên (Admin).
     * Sử dụng THINKING_MODEL cho AnalystBot để phân tích dữ liệu chuyên sâu.
     */
    public static CineAgent createAdminAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao dựa trên tư duy phân tích của DeepSeek. " +
                    "Bạn điều hành 3 chuyên gia:\n" +
                    "1. AnalystBot: Thống kê doanh thu, vé và dự báo.\n" +
                    "2. MarketingBot: Tạo nội dung quảng cáo dựa trên dữ liệu phim.\n" +
                    "3. ModerateBot: Kiểm soát người dùng, logs hệ thống và trạng thái phòng.\n" +
                    "Hãy cung cấp báo cáo chuyên sâu và chuyên nghiệp."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho người dùng cuối.
     */
    public static StreamingCineAgent createStreamingUserAgent(int userId) {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(VERSATILE_MODEL)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills(userId))
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. Giúp người dùng tra cứu phim, lịch chiếu và đặt vé qua InfoBot và BookBot."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho quản trị viên.
     */
    public static StreamingCineAgent createStreamingAdminAgent() {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao. Hỗ trợ thống kê, marketing và điều hành hệ thống rạp phim."
                )
                .build();
    }

    /**
     * Utility method to get a fast model for simple operations.
     */
    public static OpenAiChatModel getFastModel() {
        return OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(FAST_MODEL)
                .build();
    }
    /**
     * Helper for non-streaming Admin tasks (like Forecasting).
     */
    public static OpenAiChatModel createAdminAgentModel() {
        return OpenAiChatModel.builder()
                .apiKey(ApiKeyManager.getNextKey())
                .baseUrl(GROQ_URL)
                .modelName(THINKING_MODEL)
                .build();
    }
}
```

## File: src/main/java/dao/ChatMessageDAO.java
```java
package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.sql.*;
import java.util.*;

/**
 * Custom ChatMemoryStore that persists messages to SQL Server database.
 * Auto-called by LangChain4j when managing chat memory.
 */
public class ChatMessageDAO implements ChatMemoryStore {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        List<ChatMessage> history = new ArrayList<>();

        String sql = "SELECT role, content FROM (" +
                "   SELECT TOP 30 role, content, id FROM chat_messages " +
                "   WHERE session_id = ? ORDER BY id DESC" +
                ") AS recent_msgs ORDER BY id ASC";

        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) {
                System.err.println("[AI-DB-ERROR] Connection is null for memoryId: " + memoryId);
                return history;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sessionId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String role = rs.getString("role");
                        String content = rs.getString("content");
                        try {
                            ChatMessage message = deserialize(role, content);
                            if (message != null) {
                                history.add(message);
                            }
                        } catch (Exception e) {
                            System.err.println("[AI-DB-ERROR] Error deserializing message: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[AI-DB-ERROR] Error loading chat memory from DB for session " + sessionId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return history;
    }

    private ChatMessage deserialize(String role, String content) throws JsonProcessingException {
        if (content == null || content.isEmpty())
            return null;

        if (content.trim().startsWith("{")) {
            Map<String, Object> map = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {
            });
            String text = (String) map.get("text");

            return switch (role.toLowerCase()) {
                case "user" -> new UserMessage(text);
                case "system" -> new SystemMessage(text);
                case "assistant" -> {
                    List<ToolExecutionRequest> toolRequests = new ArrayList<>();
                    if (map.containsKey("toolExecutionRequests")) {
                        List<Map<String, Object>> requests = (List<Map<String, Object>>) map
                                .get("toolExecutionRequests");
                        for (Map<String, Object> req : requests) {
                            toolRequests.add(ToolExecutionRequest.builder()
                                    .id((String) req.get("id"))
                                    .name((String) req.get("name"))
                                    .arguments((String) req.get("arguments"))
                                    .build());
                        }
                    }
                    if (!toolRequests.isEmpty()) {
                        yield AiMessage.from(toolRequests);
                    } else {
                        yield AiMessage.from(text);
                    }
                }
                case "tool_execution_result" -> {
                    String id = (String) map.get("toolExecutionRequestId");
                    String toolName = (String) map.get("toolName");
                    yield ToolExecutionResultMessage.from(id, toolName, text);
                }
                default -> null;
            };
        }

        // Fallback for legacy plain text messages
        return switch (role.toLowerCase()) {
            case "user" -> new UserMessage(content);
            case "assistant" -> new AiMessage(content);
            case "system" -> new SystemMessage(content);
            default -> null;
        };
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String sessionId = memoryId.toString();
        Integer userId = extractUserIdFromSessionId(sessionId);

        String deleteSql = "DELETE FROM chat_messages WHERE session_id = ?";
        String insertSql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) {
                System.err.println("[AI-DB-ERROR] Connection is null during update for: " + memoryId);
                return;
            }
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                    psDel.setString(1, sessionId);
                    psDel.executeUpdate();
                }

                try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                    for (ChatMessage msg : messages) {
                        psIns.setString(1, sessionId);
                        if (userId != null && userId > 0) {
                            psIns.setInt(2, userId);
                        } else {
                            psIns.setNull(2, java.sql.Types.INTEGER);
                        }

                        psIns.setString(3, toRoleString(msg));

                        try {
                            String json = serialize(msg);
                            psIns.setNString(4, json);
                        } catch (Exception e) {
                            String text = (msg.text() != null) ? msg.text() : "";
                            psIns.setNString(4, text);
                        }

                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }

                conn.commit();
                System.out.println("[AI-DB-DEBUG] Successfully synced " + messages.size() + " messages for session: " + sessionId);
            } catch (Exception e) {
                if (conn != null) conn.rollback();
                System.err.println("[AI-DB-ERROR] Error during transaction for session " + sessionId + ": " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("[AI-DB-ERROR] Error syncing chat memory to DB for session " + sessionId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Efficiently appends a single message to the chat history.
     * Can be used to optimize high-frequency chat updates.
     */
    public void appendMessage(String sessionId, ChatMessage msg) {
        Integer userId = extractUserIdFromSessionId(sessionId);
        String sql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sessionId);
            if (userId != null && userId > 0) {
                ps.setInt(2, userId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, toRoleString(msg));
            ps.setNString(4, (msg.text() != null) ? msg.text() : "");

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error appending chat message to DB: " + e.getMessage());
        }
    }

    private String serialize(ChatMessage msg) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        if (msg instanceof UserMessage) {
            map.put("text", ((UserMessage) msg).text());
        } else if (msg instanceof AiMessage) {
            AiMessage ai = (AiMessage) msg;
            map.put("text", ai.text());
            if (ai.hasToolExecutionRequests()) {
                List<Map<String, String>> requests = new ArrayList<>();
                for (ToolExecutionRequest req : ai.toolExecutionRequests()) {
                    Map<String, String> reqMap = new HashMap<>();
                    reqMap.put("id", req.id());
                    reqMap.put("name", req.name());
                    reqMap.put("arguments", req.arguments());
                    requests.add(reqMap);
                }
                map.put("toolExecutionRequests", requests);
            }
        } else if (msg instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage tr = (ToolExecutionResultMessage) msg;
            map.put("toolExecutionRequestId", tr.id());
            map.put("toolName", tr.toolName());
            map.put("text", tr.text());
        } else if (msg instanceof SystemMessage) {
            map.put("text", ((SystemMessage) msg).text());
        }
        return objectMapper.writeValueAsString(map);
    }

    private String toRoleString(ChatMessage msg) {
        return switch (msg.type()) {
            case USER -> "user";
            case AI -> "assistant";
            case SYSTEM -> "system";
            default -> msg.type().name().toLowerCase();
        };
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        String sql = "DELETE FROM chat_messages WHERE session_id = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting chat memory from DB: " + e.getMessage());
        }
    }

    // Utility to map a session pattern to a user ID if needed, though typically
    // session_id alone is fine
    private Integer extractUserIdFromSessionId(String sessionId) {
        if (sessionId.contains("-u")) {
            try {
                return Integer.parseInt(sessionId.split("-u")[1]);
            } catch (Exception e) {
            }
        }
        // Legacy support
        if (sessionId.contains("_User_")) {
            try {
                return Integer.parseInt(sessionId.split("_User_")[1]);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
```

## File: src/main/java/controller/ChatServlet.java
```java
package controller;

import ai.CineAgentProvider;
import model.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet xử lý Chatbot sử dụng kiến trúc AI Agent mới.
 */
@WebServlet(urlPatterns = "/ChatServlet/*", asyncSupported = true)
public class ChatServlet extends HttpServlet {

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            objectMapper = new ObjectMapper();
            log("ChatServlet (Agentic) initialized successfully");
        } catch (Exception e) {
            log("Error initializing ChatServlet", e);
            throw new ServletException("Failed to initialize ChatServlet", e);
        }
    }

    /**
     * Lấy hoặc tạo AI Agent cho session hiện tại.
     * Phân biệt AdminAgent và UserAgent dựa trên role của user.
     * Tự động làm mới Agent nếu User ID trong session thay đổi (ví dụ: người dùng vừa đăng nhập).
     */
    private CineAgentProvider.CineAgent getOrCreateAgent(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        int currentUid = (user != null) ? user.getAccountId() : 0;
        
        CineAgentProvider.CineAgent agent = (CineAgentProvider.CineAgent) session.getAttribute("aiAgent");
        Integer agentUid = (Integer) session.getAttribute("aiAgentUserId");

        // Làm mới agent nếu chưa có hoặc UID đã thay đổi
        if (agent == null || agentUid == null || agentUid != currentUid) {
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createAdminAgent();
            } else {
                agent = CineAgentProvider.createUserAgent(currentUid);
            }
            session.setAttribute("aiAgent", agent);
            session.setAttribute("aiAgentUserId", currentUid);
            log("Created/Refreshed AI Agent (UID: " + currentUid + ", Role: " + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AI-DEBUG] ChatServlet POST request received");
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathInfo = request.getPathInfo();
        
        System.out.println("[AI-DEBUG] RequestURI: " + requestURI + ", PathInfo: " + pathInfo);
        log("ChatServlet POST: requestURI=" + requestURI + ", pathInfo=" + pathInfo);

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/chat")) {
            handleChat(request, response);
        } else if (pathInfo.startsWith("/reset")) {
            handleReset(request, response);
        } else if (pathInfo.startsWith("/stream")) {
            handleStreamChat(request, response);
        } else {
            handleChat(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private void handleChat(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String userMessage = request.getParameter("message");

            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(out, "Message không được để trống");
                return;
            }

            HttpSession session = request.getSession(true);
            CineAgentProvider.CineAgent agent = getOrCreateAgent(session);
            
            // Format memoryId: sessionId-uId để ChatMessageDAO có thể trích xuất userId
            int uid = (Integer) session.getAttribute("aiAgentUserId");
            String memoryId = session.getId() + "-u" + uid;

            long startTime = System.currentTimeMillis();
            // Gọi AI Agent xử lý (AI sẽ tự động gọi Tool nếu cần)
            String reply = agent.chat(memoryId, userMessage);
            long endTime = System.currentTimeMillis();

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            
            // Hỗ trợ Structured Response (JSON trong reply)
            if (reply.trim().startsWith("{") && reply.trim().endsWith("}")) {
                try {
                    JsonNode structured = objectMapper.readTree(reply);
                    if (structured.has("actionType")) {
                        jsonResponse.set("action", structured);
                        jsonResponse.put("reply", structured.has("message") ? structured.get("message").asText() : "Yêu cầu hành động từ hệ thống.");
                    } else {
                        jsonResponse.put("reply", reply);
                    }
                } catch (Exception e) {
                    jsonResponse.put("reply", reply);
                }
            } else {
                jsonResponse.put("reply", reply);
            }

            jsonResponse.put("processingTime", endTime - startTime);
            jsonResponse.put("sessionId", session.getId());

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

            log("Processed Agent message in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            log("Error processing chat message", e);
            sendErrorResponse(out, "Đã xảy ra lỗi hệ thống AI: " + e.getMessage());
        }
    }

    private CineAgentProvider.StreamingCineAgent getOrCreateStreamingAgent(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        int currentUid = (user != null) ? user.getAccountId() : 0;
        
        CineAgentProvider.StreamingCineAgent agent = (CineAgentProvider.StreamingCineAgent) session.getAttribute("aiStreamingAgent");
        Integer agentUid = (Integer) session.getAttribute("aiStreamingAgentUserId");

        if (agent == null || agentUid == null || agentUid != currentUid) {
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createStreamingAdminAgent();
            } else {
                agent = CineAgentProvider.createStreamingUserAgent(currentUid);
            }
            session.setAttribute("aiStreamingAgent", agent);
            session.setAttribute("aiStreamingAgentUserId", currentUid);
            log("Created/Refreshed Streaming AI Agent (UID: " + currentUid + ", Role: " + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    private void handleStreamChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter out = response.getWriter();
        String userMessage = request.getParameter("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            out.print("event: error\ndata: Message cannot be empty\n\n");
            out.flush();
            return;
        }

        HttpSession session = request.getSession(true);
        CineAgentProvider.StreamingCineAgent agent = getOrCreateStreamingAgent(session);

        // Format memoryId: sessionId-uId
        int uid = (Integer) session.getAttribute("aiStreamingAgentUserId");
        String memoryId = session.getId() + "-u" + uid;

        // Bật AsyncContext để không block thread của Tomcat/Servlet
        jakarta.servlet.AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(120000); // 2 minutes timeout for slow AI
        
        // Fix Bug #8: Add AsyncListener
        asyncContext.addListener(new jakarta.servlet.AsyncListener() {
            @Override public void onComplete(jakarta.servlet.AsyncEvent e) {}
            @Override public void onTimeout(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onError(jakarta.servlet.AsyncEvent e) { asyncContext.complete(); }
            @Override public void onStartAsync(jakarta.servlet.AsyncEvent e) {}
        });

        // Cần lấy writer từ asyncContext để đảm bảo an toàn trong môi trường async
        PrintWriter asyncOut = asyncContext.getResponse().getWriter();

        try {
            log("[STREAM-DEBUG] Starting chat for memoryId: " + memoryId);
            dev.langchain4j.service.TokenStream tokenStream = agent.chat(memoryId, userMessage);

            tokenStream
                .onNext(token -> {
                    try {
                        log("[STREAM] Received token: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
                        // Fix Bug #10: Use Jackson for JSON escaping
                        String json = objectMapper.writeValueAsString(java.util.Map.of("token", token));
                        asyncOut.print("data: " + json + "\n\n");
                        asyncOut.flush();
                    } catch (Exception e) {
                        log("Error sending token", e);
                    }
                })
                .onComplete(aiResponse -> {
                    try {
                        AiMessage aiMessage = aiResponse.content();
                        log("[STREAM] Completed successfully. Response length: " + (aiMessage != null ? aiMessage.text().length() : 0));
                        asyncOut.print("data: {\"status\": \"complete\"}\n\n");
                        asyncOut.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error completing stream", e);
                    }
                })
                .onError(error -> {
                    try {
                        String errMsg = error.getMessage() != null ? error.getMessage() : "Unknown AI Error";
                        log("[STREAM-ERROR] AI Stream Error: " + errMsg);
                        error.printStackTrace();
                        
                        String userFriendlyMsg = "Hệ thống AI đang bận (Rate Limit). Vui lòng thử lại sau vài giây.";
                        if (errMsg.contains("rate_limit_exceeded")) {
                            userFriendlyMsg = "Hệ thống đang quá tải. Tôi đang tự động thử lại với tài nguyên khác, vui lòng gửi lại tin nhắn sau 5-10 giây.";
                        }

                        if (!asyncContext.getResponse().isCommitted()) {
                             asyncOut.print("data: {\"error\": \"" + userFriendlyMsg + "\"}\n\n");
                             asyncOut.flush();
                        }
                    } catch (Exception e) {
                        log("Error sending error stream", e);
                    } finally {
                        try { asyncContext.complete(); } catch(Exception e) {}
                    }
                })
                .start();
            log("[STREAM-DEBUG] tokenStream.start() called.");
        } catch (Exception e) {
            log("Error starting stream", e);
            asyncOut.print("data: {\"error\": \"Internal Server Error: " + e.getMessage().replace("\"", "'") + "\"}\n\n");
            asyncOut.flush();
            asyncContext.complete();
        }
    }

    private void handleReset(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute("aiAgent");
                session.removeAttribute("aiAgentUserId");
                session.removeAttribute("aiStreamingAgent");
                session.removeAttribute("aiStreamingAgentUserId");
                log("Reset AI Agent for session: " + session.getId());
            }

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Phiên thảo luận AI đã được làm mới");

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

        } catch (Exception e) {
            log("Error resetting agent", e);
            sendErrorResponse(out, "Lỗi khi reset: " + e.getMessage());
        }
    }

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        try {
            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", false);
            jsonResponse.put("error", errorMessage);

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
        } catch (Exception e) {
            log("Error sending error response", e);
        }
    }

    @Override
    public void destroy() {
        log("ChatServlet destroyed");
        super.destroy();
    }
}
```

## File: src/main/java/dao/DBConnect.java
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author LENOVO
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.ConfigLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBConnect {
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
    static String user = ConfigLoader.get("db.username");
    static String pass = ConfigLoader.get("db.password");
    static String url = ConfigLoader.get("db.url");
    static String driver = ConfigLoader.get("db.driver-class-name");

    public static Connection getConnection() {
        // Debug-level log without exposing full connection details at higher levels
        LOGGER.fine("[DB] Attempting to obtain a database connection.");
        try {
            Class.forName(driver);
            // Set login timeout to 5 seconds to prevent indefinite hangs
            DriverManager.setLoginTimeout(5);
            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn != null) {
                LOGGER.fine("[DB] Database connection established successfully.");
            } else {
                LOGGER.warning("[DB] Database connection obtained is null.");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception with stack trace at SEVERE level, without printing to stdout/stderr directly
            LOGGER.log(Level.SEVERE, "[DB] Failed to obtain database connection.", e);
            return null;
        }
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```
