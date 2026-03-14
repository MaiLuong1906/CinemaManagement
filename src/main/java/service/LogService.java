package service;

import dao.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogService {
    public static void insertLog(Integer accountId, String action, String targetTable, int targetId, String message) {
        String sql = "INSERT INTO system_logs (account_id, action, target_table, target_id, log_message) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (accountId != null) {
                ps.setInt(1, accountId  );
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, action);
            ps.setString(3, targetTable);
            ps.setInt(4, targetId);
            ps.setString(5, message);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLatestLogs() {
        String sql = "SELECT TOP 10 action, target_table, log_message, created_at FROM system_logs ORDER BY created_at DESC";
        StringBuilder sb = new StringBuilder("10 Logs hệ thống gần nhất:\n");
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append(String.format("- [%s] %s %s: %s\n", 
                    rs.getTimestamp("created_at"), rs.getString("action"), 
                    rs.getString("target_table"), rs.getString("log_message")));
            }
        } catch (Exception e) {
            return "Lỗi khi lấy log: " + e.getMessage();
        }
        return sb.toString();
    }
}
