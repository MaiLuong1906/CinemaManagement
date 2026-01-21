/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CinemaHallDAO;
import dao.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import dao.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import dao.ShowtimeDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import model.CinemaHall;
import model.Movie;
import model.Showtime;
import model.TimeSlot;
import dao.TimeSlotDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nguye
 */
@WebServlet("/AddShowTimeServlet")
public class addShowTimeServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addShowTimeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addShowTimeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
               .forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //dung session de luu 
        HttpSession session = request.getSession();
        // logic sau khi nhan form
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        int hallId = Integer.parseInt(request.getParameter("hallId"));
        BigDecimal basePrice = new BigDecimal(request.getParameter("basePrice"));
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
            session.setAttribute("dbError", ex.getMessage()); // in loi tu procedure
        }
        if(flag==true) session.setAttribute("message", "Thêm phim thành công");
        else session.setAttribute("message", "Thêm phim thất bại");
        session.setAttribute("success", flag);
        // goi lai doGet tra ket qua
        response.sendRedirect(request.getContextPath() + "/AddShowTimeServlet");

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
