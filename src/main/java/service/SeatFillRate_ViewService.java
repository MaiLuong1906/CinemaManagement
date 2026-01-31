/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.*;

/**
 *
 * @author nguye
 */
public class SeatFillRate_ViewService {
    // toan bo tien ich nếu cần độ phủ ghế
    SeatFillRate_ViewDAO dao = new SeatFillRate_ViewDAO();
    // lay do phu ghe cua thang hien tai
    public double getSeatFillRateCurrentMonth(){
        return dao.getCinemaFillRateCurrentMonth();
    }
}
