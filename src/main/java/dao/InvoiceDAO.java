package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
    
    
    
    // bo sung by Dat
    // ham lay tong doanh thu
    public double calculateRevenue(){
        String GET_MONTHLY_REVENUE =
        "SELECT ISNULL(SUM(total_amount), 0) AS monthly_revenue " +
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
         String GET_MONTHLY_PRODUCT_REVENUE =
        "SELECT ISNULL(SUM(pd.quantity * p.price), 0) AS product_revenue " +
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
        String GET_MONTHLY_TICKET_REVENUE =
        "SELECT ISNULL(SUM(td.actual_price), 0) AS ticket_revenue " +
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
        String GET_DAILY_REVENUE =
            "SELECT ISNULL(SUM(total_amount), 0) AS daily_revenue " +
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
        String GET_YEARLY_REVENUE =
            "SELECT ISNULL(SUM(total_amount), 0) AS yearly_revenue " +
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

}
