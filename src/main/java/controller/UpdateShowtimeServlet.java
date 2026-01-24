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
        try {

            ShowtimeDAO showtimeDAO = new ShowtimeDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();
            MovieDAO movieDAO = new MovieDAO();
            CinemaHallDAO hallDAO = new CinemaHallDAO(DBConnect.getConnection());
            Showtime st = showtimeDAO.findById(DBConnect.getConnection(), showtimeId);
            List<Movie> movieList = movieDAO.findAll(DBConnect.getConnection());
            List<CinemaHall> hallList = hallDAO.getAllHalls();

            List<TimeSlot> availableSlots =slotDAO.getAvailableSlots(
                            DBConnect.getConnection(),
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
