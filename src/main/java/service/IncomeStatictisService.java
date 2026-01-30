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
    public double calculateMonthlyRevenue(){
        return invoiceDAO.calculateRevenue();
    }
    // tinh tien tu ve ban ra
    public double calculateTicketRevenue(){
        return invoiceDAO.getMonthlyTicketRevenue();
    }
    // tinh tien tu san pham ban ra
    public double calculateProductRevenue(){
        return invoiceDAO.getMonthlyProductRevenue();
    }
    // vi invoice co paid con ben detail thi khong nen % chuan phai la 
    public double calculateTotalRevenue(){
        return this.calculateTicketRevenue()+this.calculateProductRevenue();
    }
    // tinh phan tram moi cai
    public double calculatePercentTicket(){
        return this.calculateTicketRevenue()/this.calculateTotalRevenue();
    }
    // tinh phan tram moi cai
    public double calculatePercentProduct(){
        return this.calculateProductRevenue()/this.calculateTotalRevenue();
    }
    // lay doanh thu trong ngay
    public double getDaylyRevenue(){
        return invoiceDAO.getDailyRevenue();
    }
    // lay doanh thu theo nam
    public double getYearlyRevenue(){
        return invoiceDAO.getYearlyRevenue();
    }
    
    
}
