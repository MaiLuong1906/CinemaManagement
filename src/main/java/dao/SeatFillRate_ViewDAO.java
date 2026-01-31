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
public class SeatFillRate_ViewDAO {
    // lay do phu ghe theo thang bang ghe da ban / tong so ghe
    public double getCinemaFillRateCurrentMonth() {

        String sql = """
                SELECT
                SUM(seats_sold) * 100.0 / NULLIF(SUM(total_seats), 0)
                FROM vw_seat_coverage_detail
                WHERE year = YEAR(GETDATE())
                AND month = MONTH(GETDATE())
        """;

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                return rs.getDouble(1);
                }
                return 0.0;

        } catch (SQLException e) {
                throw new RuntimeException("Cannot calculate cinema fill rate", e);
        }
        }

}
