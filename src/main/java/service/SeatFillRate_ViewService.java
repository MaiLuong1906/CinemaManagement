/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SeatFillRate_ViewDTO;

/**
 *
 * @author nguye
 */
public class SeatFillRate_ViewService {
    // toan bo tien ich nếu cần độ phủ ghế
    private final SeatFillRate_ViewDAO dao;

    public SeatFillRate_ViewService() {
        this.dao = new SeatFillRate_ViewDAO();
    }

    public SeatFillRate_ViewService(SeatFillRate_ViewDAO dao) {
        this.dao = dao;
    }
    // lay do phu ghe cua thang hien tai
    public double getSeatFillRateCurrentMonth(){
        return dao.getCinemaFillRateCurrentMonth();
    }
    // lay ra do phu ghe theo tung showtime
    public List<SeatFillRate_ViewDTO> getSeatFillRateForShowtimeCurrentMonth(){
        try {
            return dao.getSeatFillRateByTimeSlotCurrentMonth();
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi khi lấy dữ liệu!");
        }
    }
    public List<SeatFillRate_ViewDTO> getAllCurrentMonth() {
    try {
        return dao.getAllSeatCoverageCurrentMonth();
    } catch (Exception ex) {
        throw new RuntimeException("Lỗi khi lấy dữ liệu!", ex);
    }
}
}
