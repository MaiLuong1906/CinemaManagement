package service;

import dao.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LogService {
    public static void insertLog(Integer accountId, String action, String targetTable, int targetId, String message) {
        String sql = "INSERT INTO system_logs (account_id, action, target_table, target_id, log_message) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = new DBConnect().getConnection();
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
}
