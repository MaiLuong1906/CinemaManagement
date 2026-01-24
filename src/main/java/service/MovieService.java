/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DBConnect;
import dao.MovieDAO;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class MovieService {
    // lay ra list phim
    
    // update 
    
    // xoa phim
    public boolean DeleteMovie (int id) throws Exception{
        // check validator
        
        // logic chinh
        MovieDAO movieDao = new MovieDAO();
        boolean flag;
        try{
            movieDao.delete(DBConnect.getConnection(), id);
            flag = true;
        }catch(SQLException e){
            throw new Exception("Phim không tồn tại", e);
        }
        return flag;
    }
    //
    
}
