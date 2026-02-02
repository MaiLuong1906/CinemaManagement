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
import service.TimeSlotService;
import service.SeatFillRate_ViewService;
import model.SeatFillRate_ViewDTO;
import java.util.ArrayList;
import java.util.List;
import model.TimeSlotKpiDTO;

/**
 *
 * @author nguye
 */
public class AdminShowtimeServlet extends HttpServlet {
    TimeSlotService timeslotService = new TimeSlotService();
    SeatFillRate_ViewService service = new SeatFillRate_ViewService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // lay ra so suat chieu hien tai
        try {
            int numberOfShowtime = timeslotService.countTimeSlot(); // so ve da ban
            request.setAttribute("numberOfShowtime", numberOfShowtime);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        // lay do phu ghe
        try {
            List<SeatFillRate_ViewDTO> list = service.getSeatFillRateForShowtimeCurrentMonth();
            request.setAttribute("list", list);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        // lay kpi
        try {
            List<TimeSlotKpiDTO> listKpi = timeslotService.getTimeSlotKpiCurrentMonth();
            request.setAttribute("listKpi", listKpi);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        // bieu do
        List<int[]> ticketBySlot = timeslotService.getTotalTicketsBySlotCurrentMonth();
        request.setAttribute("ticketBySlot", ticketBySlot);     
        // chuyen tiep
        request.getRequestDispatcher("views/admin/statistic/showtime-management.jsp").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

}
