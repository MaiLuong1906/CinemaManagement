package com.mycompany.cinema.dao;

import com.mycompany.cinema.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class UserDAO implements DAOInterface<User>{
    @Override
public User getById(int id) {
    User user = null;

    String sql = "SELECT user_id, full_name, gender, date_of_birth, address FROM user_profile WHERE id = ?";

    try (Connection con = JDBCUtils.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("full_name"));
            user.setGender(rs.getInt("gender"));
            user.setDob(rs.getDate("date_of_birth").toLocalDate());
            user.setAddress(rs.getString("address"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return user;
}
    // ham lay user bang username
    public User getByUsername(String username){
        return null;
    }
    // hehehehehe
}
