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
import java.util.logging.Level;
import java.util.logging.Logger;
import service.TicketManagementService;

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
        
        
        
        // chuyen tiep
        request.getRequestDispatcher("views/admin/statistic/ticket-management.jsp").forward(request, response);
    }

}
