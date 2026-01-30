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
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author nguye
 */
public class AdminStatisticServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // dieu huong
        IncomeStatictisService incomeStatictisService = new IncomeStatictisService();
        // format tien
        NumberFormat vndFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        double totalIncome = incomeStatictisService.calculateTotalRevenue();
        request.setAttribute("totalIncome", vndFormat.format(totalIncome));
        request.getRequestDispatcher("/views/admin/users/admin-statictis.jsp").forward(request, response);
        
    }
}
