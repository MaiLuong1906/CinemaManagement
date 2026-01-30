package dao;


import model.TicketDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDetailDAO {

    private static final String INSERT_SQL =
            "INSERT INTO ticket_details (invoice_id, seat_id, hall_id, showtime_id, actual_price) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_INVOICE_SQL =
            "SELECT * FROM ticket_details WHERE invoice_id = ?";

    private static final String DELETE_BY_INVOICE_SQL =
            "DELETE FROM ticket_details WHERE invoice_id = ?";

    private static final String CHECK_SEAT_SQL =
            "SELECT 1 FROM ticket_details WHERE showtime_id = ? AND seat_id = ?";

    // =========================
    // 1. Đặt 1 ghế
    // =========================
    public void insert(Connection conn, TicketDetail t) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, t.getInvoiceId());
            ps.setInt(2, t.getSeatId());
            ps.setInt(3, t.getHallId());
            ps.setInt(4, t.getShowtimeId());
            ps.setBigDecimal(5, t.getActualPrice());
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
                ps.setInt(3, t.getHallId());
                ps.setInt(4, t.getShowtimeId());
                ps.setBigDecimal(5, t.getActualPrice());
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
        t.setHallId(rs.getInt("hall_id"));
        t.setShowtimeId(rs.getInt("showtime_id"));
        t.setActualPrice(rs.getBigDecimal("actual_price"));
        return t;
    }
    // lay ra so ve da ban trong thang
    public int countSoldTicketsThisMonth() throws SQLException {
            String COUNT_TICKETS_THIS_MONTH_SQL =
            "SELECT COUNT(*) " +
            "FROM ticket_details td " +
            "JOIN invoices i ON td.invoice_id = i.invoice_id " +
            "WHERE i.status = 'Paid' " +
            "AND MONTH(i.booking_time) = MONTH(GETDATE()) " +
            "AND YEAR(i.booking_time) = YEAR(GETDATE())";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_TICKETS_THIS_MONTH_SQL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    // lay ra so ve ban trong hom nay
    public int countSoldTicketsToday() throws SQLException {
        String COUNT_TICKETS_TODAY_SQL =
                "SELECT COUNT(*) " +
                "FROM ticket_details td " +
                "JOIN invoices i ON td.invoice_id = i.invoice_id " +
                "WHERE i.status = 'Paid' " +
                "AND i.booking_time >= CAST(GETDATE() AS DATE) " +
                "AND i.booking_time <  DATEADD(DAY, 1, CAST(GETDATE() AS DATE))";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_TICKETS_TODAY_SQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    // lay ra so ve ban trong nam
    public int countSoldTicketsThisYear() throws SQLException {

    String COUNT_TICKETS_THIS_YEAR_SQL =
            "SELECT COUNT(*) " +
            "FROM ticket_details td " +
            "JOIN invoices i ON td.invoice_id = i.invoice_id " +
            "WHERE i.status = 'Paid' " +
            "AND i.booking_time >= DATEFROMPARTS(YEAR(GETDATE()), 1, 1) " +
            "AND i.booking_time <  DATEFROMPARTS(YEAR(GETDATE()) + 1, 1, 1)";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(COUNT_TICKETS_THIS_YEAR_SQL);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }
    }
    return 0;
}
}

