/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author nguye
 */
public class TicketsSoldDAO {
    // lay ra so ve da ban trong thang do
    public int countSoldTicketsThisMonth() throws SQLException {
        String sql =
            "SELECT COUNT(*) FROM vw_sold_tickets " +
            "WHERE booking_time >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) " +
            "AND booking_time <  DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    // lay ra so ve trong ngay 
    public int countSoldTicketsToday() throws SQLException {
        String sql =
            "SELECT COUNT(*) FROM vw_sold_tickets " +
            "WHERE booking_time >= CAST(GETDATE() AS DATE) " +
            "AND booking_time <  DATEADD(DAY, 1, CAST(GETDATE() AS DATE))";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    // lay ra so ve trong nam
    public int countSoldTicketsThisYear() throws SQLException {

    String sql =
        "SELECT COUNT(*) FROM vw_sold_tickets " +
        "WHERE booking_time >= DATEFROMPARTS(YEAR(GETDATE()), 1, 1) " +
        "AND booking_time <  DATEADD(YEAR, 1, DATEFROMPARTS(YEAR(GETDATE()), 1, 1))";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        return rs.next() ? rs.getInt(1) : 0;
    }
}

    /**
     * Get daily sold tickets history for the last N days
     */
    public Map<LocalDate, Integer> getDailyTicketHistory(int days) throws SQLException {
        String sql = """
                    SELECT CAST(booking_time AS DATE) as gap_date, COUNT(*) as daily_tickets
                    FROM vw_sold_tickets
                    WHERE booking_time >= DATEADD(DAY, -?, GETDATE())
                    GROUP BY CAST(booking_time AS DATE)
                    ORDER BY gap_date ASC
                """;
        Map<LocalDate, Integer> history = new LinkedHashMap<>();
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.put(rs.getDate("gap_date").toLocalDate(), rs.getInt("daily_tickets"));
            }
        }
        return history;
    }
}
