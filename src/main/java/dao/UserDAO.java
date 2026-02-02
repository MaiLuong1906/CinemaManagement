/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.UserDTO;
import util.Encoding;

/**
 *
 * @author LENOVO
 */
public class UserDAO {

    // Login by : phone number + password
    // Join Account and UserProfile
    public static UserDTO login(String phoneNumber, String password) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.gender, u.address, u.date_of_birth, "
                + "a.phone_number, a.password_hash, a.role_id, a.status, a.created_at "
                + "FROM accounts a "
                + "INNER JOIN user_profiles u ON a.account_id = u.user_id "
                + "WHERE a.phone_number = ? AND a.password_hash = ?";
        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, phoneNumber);
            pst.setString(2, Encoding.toSHA1(password));

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                // Account fields
                user.setAccountId(rs.getInt("user_id"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setRoleId(rs.getString("role_id"));
                user.setStatus(rs.getBoolean("status"));
                user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                // UserProfile fields
                user.setProfileId(rs.getInt("user_id")); // CRITICAL FIX: Set profileId for update operations
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setGender(rs.getBoolean("gender"));
                user.setAddress(rs.getString("address"));
                user.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                return user;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Register new account
    public static boolean register(String phoneNumber, String email, String password,
            String fullName, boolean gender, String address, LocalDate dateOfBirth) {
        Connection con = null;
        PreparedStatement psAccount = null;
        PreparedStatement psProfile = null;

        try {
            con = DBConnect.getConnection();
            con.setAutoCommit(false); // Start transaction

            // Insert Accounts
            String sqlAccounts = "insert into accounts (phone_number, password_hash, role_id, created_at)"
                    + "values(?, ?, ?, ?)";
            // To get primary key after excute
            psAccount = con.prepareStatement(sqlAccounts, Statement.RETURN_GENERATED_KEYS);
            psAccount.setString(1, phoneNumber);
            psAccount.setString(2, Encoding.toSHA1(password));
            psAccount.setString(3, "User");
            psAccount.setObject(4, LocalDateTime.now());

            psAccount.executeUpdate();
            // Collect ID
            ResultSet rs = psAccount.getGeneratedKeys();
            int accountId = 0;
            if (rs.next()) {
                accountId = rs.getInt(1);
            } else {
                con.rollback();
                return false;
            }

            String sqlProfile = "insert into user_profiles (user_id, full_name, email, gender, address, date_of_birth)"
                    + "values(?, ?, ?, ?, ?, ?)";
            psProfile = con.prepareStatement(sqlProfile);

            psProfile.setInt(1, accountId);
            psProfile.setString(2, fullName);
            psProfile.setString(3, email);
            psProfile.setBoolean(4, gender);
            psProfile.setString(5, address);
            psProfile.setObject(6, dateOfBirth);

            psProfile.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    // Check if the phone number already exists.
    public static boolean checkPhoneNumber(String phoneNumber) {
        boolean ok = true;
        String sql = "select * from accounts where phone_number = ?";
        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, phoneNumber);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                ok = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }
}
