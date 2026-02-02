/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.Ticket_Movie_ViewDAO;
import dao.*;
import java.sql.SQLException;
import java.util.List;
import model.Movie_Ticket_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;

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
    
    // 
    // tat ca cac ham service cho thong ke ve 
    // tong ve theo phim
    Ticket_Movie_ViewDAO ticket_Movie_ViewDAO = new Ticket_Movie_ViewDAO();
    // ham lay ra so page
    public int returnNumberPage(){
        try {
            return ticket_Movie_ViewDAO.getTotalPages();
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi truy suất dữ liệu!!!");
        }
    }
    // lay ra tat ca cac thuoc tinh
    public List<Movie_Ticket_ViewDTO> getAllOfPageNumber(int pageNumber){
        try {
            return ticket_Movie_ViewDAO.getByPage(pageNumber);
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi truy suất dữ liệu!!!");
        }
    }
    // ve theo gio chieu
    TicketSoldBySlot_ViewDAO bySlot_ViewDAO = new TicketSoldBySlot_ViewDAO();
    public List<TicketSoldBySlot_ViewDTO> getTicketSoldBySlotCurrentMonth() {
        try {
            return bySlot_ViewDAO.getAll();
        } catch (Exception ex) {
            throw new RuntimeException(
                "Lỗi truy vấn dữ liệu!!!", ex
            );
        }
    }
}
