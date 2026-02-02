/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TimeSlotKpiDTO;
/**
 *
 * @author nguye
 */
public class TimeSlotService {
    TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
    TimeSlotKpiDAO timeSlotKpiDAO = new TimeSlotKpiDAO();
    // lay ra so suat chieu hien tai
    public int countTimeSlot(){
        try {
            return timeSlotDAO.countAllTimeSlots();
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi từ hệ thống");
        }
    }
    // lay ra kpi tung khung gio trong thang
    public List<TimeSlotKpiDTO> getTimeSlotKpiCurrentMonth(){
        try {
            return timeSlotKpiDAO.getTimeSlotKpiCurrentMonth();
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi lấy dữ liệu");
        }
    }
    // du lieu dau vao cho bieu do statistic
    public List<int[]> getTotalTicketsBySlotCurrentMonth(){
        try {
            return timeSlotKpiDAO.getTotalTicketsBySlotCurrentMonth();
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi lấy dữ liệu");
        }
    }
}
