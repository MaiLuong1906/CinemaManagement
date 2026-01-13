package dao;


import model.UserProfile;

import java.sql.*;

public class UserProfileDAO {

    /* =========================
       INSERT PROFILE (khi đăng ký)
       ========================= */
    public void insert(Connection conn, UserProfile profile) throws SQLException {
        String sql = """
            INSERT INTO user_profiles
            (user_id, full_name, email, gender, address, date_of_birth)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFullName());
            ps.setString(3, profile.getEmail());
            ps.setBoolean(4, profile.isGender());
            ps.setString(5, profile.getAddress());
            ps.setDate(6, Date.valueOf(profile.getDateOfBirth())); // LocalDate -> SQL Date
            ps.executeUpdate();
        }
    }

    /* =========================
       FIND BY USER ID
       ========================= */
    public UserProfile findByUserId(int userId) throws SQLException {
        String sql = """
            SELECT * FROM user_profiles
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       UPDATE PROFILE
       ========================= */
    public void update(UserProfile profile) throws SQLException {
        String sql = """
            UPDATE user_profiles
            SET full_name = ?, email = ?, gender = ?, address = ?, date_of_birth = ?
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, profile.getFullName());
            ps.setString(2, profile.getEmail());
            ps.setBoolean(3, profile.isGender());
            ps.setString(4, profile.getAddress());
            ps.setDate(5, Date.valueOf(profile.getDateOfBirth()));
            ps.setInt(6, profile.getUserId());

            ps.executeUpdate();
        }
    }

    /* =========================
       CHECK EXISTENCE
       ========================= */
    public boolean existsByUserId(int userId) throws SQLException {
        String sql = """
            SELECT 1 FROM user_profiles
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /* =========================
       DELETE PROFILE (admin)
       ========================= */
    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM user_profiles WHERE user_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private UserProfile mapRow(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setUserId(rs.getInt("user_id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setEmail(rs.getString("email"));
        profile.setGender(rs.getBoolean("gender"));
        profile.setAddress(rs.getString("address"));
        profile.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return profile;
    }
}
