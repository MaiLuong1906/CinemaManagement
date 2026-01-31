/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import model.*;
import service.*;
import java.util.List;

/**
 *
 * @author nguye
 */
public class AdminTicketServlet extends HttpServlet {
    TicketManagementService ticketManagementService = new TicketManagementService();
    SeatFillRate_ViewService setFillRateService = new SeatFillRate_ViewService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // set up thuoc tinh
        try {
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold() ; // so ve da ban
            request.setAttribute("monthlyTicketsSold", monthlyTicketsSold);
        } catch (SQLException ex) {
            request.setAttribute("error_for_getAtribute", "null");
        }
        try {
            int dailyTicketsSold = ticketManagementService.getDailyTicketsSold();
            request.setAttribute("dailyTicketsSold", dailyTicketsSold);
        } catch (SQLException ex) {
            request.setAttribute("error_for_getAtribute", "null");
        }
        try {
            int yearlyTicketsSold = ticketManagementService.getYearlyTicketsSold();
            request.setAttribute("yearlyTicketsSold", yearlyTicketsSold);
        } catch (SQLException ex) {
            request.setAttribute("error_for_getAtribute", "null");
        }
        // cac thuoc tinh ve ve theo thuoc tinh
        int page = 1; // mặc định page 1
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        try {
            List<Movie_Ticket_ViewDTO> movies =
                    ticketManagementService.getAllOfPageNumber(page);
            int totalPages = ticketManagementService.returnNumberPage();
            request.setAttribute("movies", movies);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
        } catch (RuntimeException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
        }
        int slotPage = 1;
        String slotPageParam = request.getParameter("slotPage");
        if (slotPageParam != null) {
            slotPage = Integer.parseInt(slotPageParam);
        }
        // 
        try{
            List<TicketSoldBySlot_ViewDTO> ticketSoldBySlot =
                    ticketManagementService.getTicketSoldBySlotCurrentMonth();
            request.setAttribute("ticketSoldBySlot", ticketSoldBySlot);
        }catch(Exception ex){
            request.setAttribute("errorMessage", ex.getMessage());
        }
        //
        try{
            double seatFillRate = setFillRateService.getSeatFillRateCurrentMonth();
            request.setAttribute("seatFillRate", seatFillRate);
        }catch(Exception ex){
            request.setAttribute("errorMessage", ex.getMessage());
        }
        
        // chuyen tiep
        request.getRequestDispatcher("views/admin/statistic/ticket-management.jsp").forward(request, response);
    }

}
