/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package service;
import dao.InvoiceDAO;

/**
 *
 * @author nguye
 */
public class IncomeStatictisService {
    InvoiceDAO invoiceDAO = new InvoiceDAO();

    // trang service de lay cac phan lien quan den doanh thu
    // tra ve ti le bieu do tron
    
    // tinh tong doanh thu theo thang
    public double calculateTotalRevenue(){
        return invoiceDAO.calculateRevenue();
    }
    // tinh tien tu ve ban ra
    public double calculateTicketRevenue(){
        return invoiceDAO.getMonthlyTicketRevenue();
    }
    // tinh tien tu san pham ban ra
    public double calculateProductRevenue(){
        return invoiceDAO.getMonthlyTicketRevenue();
    }
    // tinh phan tram moi cai
    public double calculatePercentTicket(){
        return this.calculateTicketRevenue()/this.calculateTotalRevenue();
    }
    // tinh phan tram moi cai
    public double calculatePercentProduct(){
        return this.calculateProductRevenue()/this.calculateTotalRevenue();
    }
    
    
    
    
}
