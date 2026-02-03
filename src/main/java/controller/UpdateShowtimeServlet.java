/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CinemaHallDAO;
import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.TimeSlotDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import util.MovieUtils;
import model.Showtime;
import java.util.List;
import model.Movie;
import model.CinemaHall;
import model.TimeSlot;

/**
 *
 * @author nguye
 */
public class UpdateShowtimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // lay ra du lieu da co
        int showtimeId = MovieUtils.getIntParameter(request, "showtimeId");
        try (java.sql.Connection conn = DBConnect.getConnection()) {

            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();
            MovieDAO movieDAO = new MovieDAO();
            CinemaHallDAO hallDAO = new CinemaHallDAO();
            Showtime st = showtimeDAO.findById(conn, showtimeId);
            List<Movie> movieList = movieDAO.findAll(conn);
            List<CinemaHall> hallList = hallDAO.getAllHalls(conn);

            List<TimeSlot> availableSlots =slotDAO.getAvailableSlots(
                            conn,
                            st.getHallId(),
                            st.getShowDate(),
                            st.getShowtimeId()
                    );
            request.setAttribute("showtime", st);
            request.setAttribute("movieList", movieList);
            request.setAttribute("hallList", hallList);
            request.setAttribute("slotList", availableSlots);
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.getRequestDispatcher("views/admin/movies/UpdateShowtime.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // them vao database
        try{
            int showtimeId = MovieUtils.getIntParameter(request, "showtimeId");
            int movieId = MovieUtils.getIntParameter(request, "movieId");
            int hallId = MovieUtils.getIntParameter(request, "hallId");
            LocalDate showDate = MovieUtils.getLocalDateParameter(request, "showDate");
            int slotId = MovieUtils.getIntParameter(request, "slotId");
            Showtime showtime = new Showtime(showtimeId, movieId, hallId, showDate, slotId);
            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            showtimeDAO.update(DBConnect.getConnection(), showtime);
            // gui phan hoi ve 
            session.setAttribute("flash_success", "Cập nhật suất chiếu thành công!");
        }catch (IllegalArgumentException e) {
        // tra loi valid
        session.setAttribute("flash_error", e.getMessage());
    } catch (SQLException e) {
        // loi db
        session.setAttribute("flash_error", "Lỗi hệ thống khi cập nhật suất chiếu!");
    }
        response.sendRedirect(request.getContextPath() + "/ListMovieForAdmin");
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
