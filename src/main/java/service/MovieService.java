/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DBConnect;
import dao.MovieDAO;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class MovieService {
    private MovieDAO movieDao;

    public MovieService() {
        this.movieDao = new MovieDAO();
    }

    public MovieService(MovieDAO movieDao) {
        this.movieDao = movieDao;
    }

    // xoa phim
    public boolean DeleteMovie (Connection conn, int id) throws Exception{
        // check validator
        
        // logic chinh
        boolean flag;
        try{
            movieDao.delete(conn, id);
            flag = true;
        }catch(SQLException e){
            throw new Exception("Phim không tồn tại", e);
        }
        return flag;
    }
    //
    
}
