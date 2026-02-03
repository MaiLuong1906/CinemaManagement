package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

}
