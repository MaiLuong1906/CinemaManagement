/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.TicketsSoldDAO;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class TicketManagementService {
    // dung cho lay cac service cho thong ke ve ban ra
    TicketsSoldDAO ticketsSoldDAO = new TicketsSoldDAO();
    // lay ra so ve theo thang
    public int getMothlyTicketsSold() throws SQLException{
        return ticketsSoldDAO.countSoldTicketsThisMonth();
    }
    // lay ra so ve theo ngay
    public int getDailyTicketsSold() throws SQLException{
        return ticketsSoldDAO.countSoldTicketsToday();
    }
    // Lay ra so ve theo nam
    public int getYearlyTicketsSold() throws SQLException{
        return ticketsSoldDAO.countSoldTicketsThisYear();
    }
}
