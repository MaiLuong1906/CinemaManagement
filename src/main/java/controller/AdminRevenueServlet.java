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
import service.IncomeStatictisService;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author nguye
 */
public class AdminRevenueServlet extends HttpServlet {
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IncomeStatictisService revenueService = new  IncomeStatictisService();
        // format tien
        NumberFormat vndFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        // phan tram
        double percentProduct = revenueService.calculatePercentProduct();
        double percentTicket = revenueService.calculatePercentTicket();
        request.setAttribute("percentProduct", percentProduct);
        request.setAttribute("percentTicket", percentTicket);
        //lay ra doanh thu
        double monthlyRevenue = revenueService.calculateTotalRevenue();
        double dailyRevenue = revenueService.getDaylyRevenue();
        double yearlyRevenue = revenueService.getYearlyRevenue();
        request.setAttribute("monthlyRevenue", vndFormat.format(monthlyRevenue));
        request.setAttribute("dailyRevenue", vndFormat.format(dailyRevenue));
        request.setAttribute("yearlyRevenue", vndFormat.format(yearlyRevenue));
        // rieng
        double ticketRevenue = revenueService.calculateTicketRevenue();
        double productRevenue = revenueService.calculateProductRevenue();
        request.setAttribute("ticketRevenue", vndFormat.format(ticketRevenue));
        request.setAttribute("productRevenue", vndFormat.format(productRevenue));
        request.getRequestDispatcher("views/admin/statistic/overal-revenue.jsp").forward(request, response);
    }
    //
    
    //

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    

}
