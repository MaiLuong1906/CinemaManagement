/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package controller.handle.showtime;

import java.util.List;

import controller.handle.ControllerHandle;
import dao.CinemaHallDAO;
import dao.DBConnect;
import dao.MovieDAO;
import dao.MovieGenreDAO;
import dao.ShowtimeDAO;
import dao.TimeSlotDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import model.CinemaHall;
import model.Movie;
import model.Showtime;
import model.TimeSlot;


/**
 *
 * @author nguye
 */
public class AddShowtime implements ControllerHandle{
    
    @Override
    public void excute(HttpServletRequest request, HttpServletResponse respone) 
            throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            handleAddShowtimeGet(request, respone);
        } else {
            handleAddShowtimePost(request, respone);
        }
    }
    // add danh cho showtime
    private void handleAddShowtimeGet(HttpServletRequest request, HttpServletResponse respone)
            throws Exception {
        // du lieu dau vao
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> movieList = movieDAO.getAllMovies();
        request.setAttribute("movieList", movieList);
        // chon phong
        CinemaHallDAO cinemaHallDAO = new CinemaHallDAO(DBConnect.getConnection());
        List<CinemaHall> hallList = new ArrayList<>();
        try {
            hallList = cinemaHallDAO.getAllHalls();
        } catch (SQLException ex) {
            System.out.println("Khong co phong nao");
        }
        request.setAttribute("hallList", hallList);
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        List<TimeSlot> listChieu = null;
        try {
            listChieu = timeSlotDAO.findAll(DBConnect.getConnection());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        request.setAttribute("listChieu", listChieu);
        request.getRequestDispatcher("/views/admin/movies/addShowTime.jsp")
            .forward(request, respone);
    }
    
    private void handleAddShowtimePost(HttpServletRequest request, HttpServletResponse respone)
            throws Exception {
        //dung session de luu 
        HttpSession session = request.getSession();
        // logic sau khi nhan form
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        int hallId = Integer.parseInt(request.getParameter("hallId"));
        int startTimeId = Integer.parseInt(request.getParameter("timeSlotId"));
        LocalDate showDate = LocalDate.parse(request.getParameter("showDate"));
        Showtime showtime = new Showtime(movieId, hallId, showDate, startTimeId);
        ShowtimeDAO showtimeDAO = new ShowtimeDAO();
        boolean flag = true;
        try{
            showtimeDAO.insert(DBConnect.getConnection(), showtime);
            flag = true;
            session.setAttribute("dbError", "");
        }catch(SQLException ex){
            flag = false;
            if(ex.getMessage().contains("UQ_Showtime")){
                session.setAttribute("dbError", "Lịch chiếu bị trùng!");
            }else{
                session.setAttribute("dbError", "Lỗi từ hệ thống SQL!");
            }
        }
        if(flag==true) session.setAttribute("message", "Thêm phim thành công");
        else session.setAttribute("message", "Thêm phim thất bại");
        session.setAttribute("success", flag);
        // goi lai GET tra ket qua
        respone.sendRedirect(request.getContextPath() + "/MovieAdminServlet?command=add-showtime");
    }
    

}
