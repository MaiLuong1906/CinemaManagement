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
import service.TicketManagementService;
import java.util.List;

/**
 *
 * @author nguye
 */
public class AdminTicketServlet extends HttpServlet {
    TicketManagementService ticketManagementService = new TicketManagementService();
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
        TicketManagementService service = new TicketManagementService();

        int page = 1; // mặc định page 1
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        try {
            List<Movie_Ticket_ViewDTO> movies =
                    service.getAllOfPageNumber(page);
            int totalPages = service.returnNumberPage();
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
                    service.getTicketSoldBySlotCurrentMonth();
            request.setAttribute("ticketSoldBySlot", ticketSoldBySlot);
        }catch(Exception ex){
            request.setAttribute("errorMessage", ex.getMessage());
        }
        // chuyen tiep
        request.getRequestDispatcher("views/admin/statistic/ticket-management.jsp").forward(request, response);
    }

}
