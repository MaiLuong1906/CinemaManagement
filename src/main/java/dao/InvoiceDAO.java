package dao;

import model.Invoice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public Invoice findById(Connection conn, int invoiceId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       FIND BY USER
       ========================= */
    public List<Invoice> findByUser(Connection conn, int userId) throws SQLException {
        String sql = """
            SELECT * FROM invoices
            WHERE user_id = ?
            ORDER BY booking_time DESC
        """;

        List<Invoice> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       CREATE INVOICE (PENDING)
       ========================= */
    public Invoice create(Connection conn, int userId, int showtimeId,
                          LocalDateTime expiryTime) throws SQLException {

        String sql = """
            INSERT INTO invoices (user_id, showtime_id, expiry_time)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, showtimeId);
            ps.setTimestamp(3, Timestamp.valueOf(expiryTime));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int invoiceId = rs.getInt(1);
                return findById(conn, invoiceId);
            }
        }
        return null;
    }

    /* =========================
       UPDATE STATUS
       ========================= */
    public void updateStatus(Connection conn, int invoiceId, String status)
            throws SQLException {

        String sql = "UPDATE invoices SET status = ? WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE TOTAL AMOUNT
       ========================= */
    public void updateTotalAmount(Connection conn, int invoiceId, double total)
            throws SQLException {

        String sql = "UPDATE invoices SET total_amount = ? WHERE invoice_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, total);
            ps.setInt(2, invoiceId);
            ps.executeUpdate();
        }
    }

    /* =========================
       AUTO CANCEL EXPIRED
       ========================= */
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

    /* =========================
       MAP RESULTSET
       ========================= */
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
}

