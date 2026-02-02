/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;
import service.IncomeStatictisService;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import model.Movie_Ticket_ViewDTO;
import service.TicketManagementService;
import service.TimeSlotService;

/**
 *
 * @author nguye
 */
public class AdminStatisticServlet extends HttpServlet {
    IncomeStatictisService incomeStatictisService = new IncomeStatictisService();
    TicketManagementService ticketManagementService  = new TicketManagementService();
    TimeSlotService timeslotService = new TimeSlotService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        // format tien
        NumberFormat vndFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        double totalIncome = incomeStatictisService.calculateTotalRevenue();
        request.setAttribute("totalIncome", vndFormat.format(totalIncome));
        try {
            // set up thuoc tinh
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold() ; // so ve da ban
            request.setAttribute("monthlyTicketsSold", monthlyTicketsSold);
        } catch (SQLException ex) {
            request.setAttribute("error_for_getAtribute", "null");
        }
         // format tien
        double totalIncomeToday = incomeStatictisService.getDaylyRevenue();
        request.setAttribute("totalIncomeToday", vndFormat.format(totalIncomeToday));
        // top phim
        TicketManagementService service = new TicketManagementService();
        

        try {
            // top phim
            List<Movie_Ticket_ViewDTO> topMovies =
                    service.getAllOfPageNumber(1);
            // bad phim
            int totalPages = service.returnNumberPage();
            List<Movie_Ticket_ViewDTO> badMovies =
                    service.getAllOfPageNumber(totalPages);

            request.setAttribute("topMovies", topMovies);
            request.setAttribute("badMovies", badMovies);

        } catch (RuntimeException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
        }
        // lay ra so suat chieu hien tai
        try {
            int numberOfShowtime = timeslotService.countTimeSlot(); // so ve da ban
            request.setAttribute("numberOfShowtime", numberOfShowtime);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 

        // dieu huong
        request.getRequestDispatcher("/views/admin/users/admin-statictis.jsp").forward(request, response);
        
    }
}
