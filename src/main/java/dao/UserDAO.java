/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import model.Account;

/**
 *
 * @author LENOVO
 */
public class UserDAO {

    public static Account checkLogin(Account account) {
        Account res = null;
        try {
            Connection con = DBConnect.getConnection();

            String sql = "select * from [Users] where phone = ? and password_hash = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, account.getphoneNumber());
            pst.setString(2, account.getPasswordHash());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String fullname = rs.getString("full_name");
                String phone = rs.getString("phone");
                String password = rs.getString("password_hash");
                int role = Integer.parseInt(rs.getString("role_id"));

                res = new Account(fullname, phone, password, role, new Date());
                res
            }
        } catch (Exception e) {
        }
        return res;
    }

    public static void insert(User user) {
        try {
            Connection con = DBConnect.getConnection();
            String sql = "INSERT INTO [Users](full_name, email, phone, password_hash, role_id, created_at) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user.getFullname());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getPassword());
            pst.setInt(5, user.getRole());
            LocalDate date = LocalDate.now();
            pst.setDate(6, java.sql.Date.valueOf(date));
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        boolean check = false;
        try {
            Connection con = DBConnect.getConnection();
            String sql = "SELECT *  FROM [users] WHERE phone = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, phoneNumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }
}
