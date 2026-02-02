package dao;

import model.Account;

import java.sql.*;

public class AccountDAO {
    public Account findByPhone(Connection conn, String phone) throws SQLException {
        String sql = """
            SELECT account_id, phone_number, password_hash, role_id, status
            FROM accounts
            WHERE phone_number = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, phone);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Account acc = new Account();
            acc.setId(rs.getInt("account_id"));
            acc.setPhoneNumber(rs.getString("phone_number"));
            acc.setPasswordHash(rs.getString("password_hash"));
            acc.setRoleId(rs.getString("role_id"));
            acc.setStatus(rs.getBoolean("status"));
            return acc;
        }
        return null;
    }
    // Admin: update role
    public void updateRole(int accountId, String role) throws SQLException {
        String sql = "UPDATE accounts SET role_id = ? WHERE account_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    // Admin: lock / unlock
    public void updateStatus(int accountId, boolean status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    public boolean existsByPhone(Connection conn, String phone) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE phone_number = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, phone);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public int insert(Connection conn, String phone, String passwordHash) throws SQLException {
        String sql = """
            INSERT INTO accounts(phone_number, password_hash)
            VALUES (?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, phone);
        ps.setString(2, passwordHash);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // account_id
        }
        throw new SQLException("Cannot get account_id");
    }
}
