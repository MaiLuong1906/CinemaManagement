/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    
}
