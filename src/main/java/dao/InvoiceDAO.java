package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Invoice;

public class InvoiceDAO {

    public int insert(Connection conn, Invoice invoice) throws SQLException {

        String sql = """
            INSERT INTO invoices
            (user_id, showtime_id, booking_time, expiry_time, total_amount, status)
            VALUES (?, ?, GETDATE(), DATEADD(MINUTE, 5, GETDATE()), ?, 'PENDING')
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getUserId());
            ps.setInt(2, invoice.getShowtimeId());
            ps.setBigDecimal(3, invoice.getTotalAmount());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
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
}
