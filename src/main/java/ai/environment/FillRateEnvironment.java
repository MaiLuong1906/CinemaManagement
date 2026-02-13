/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.agent;

import service.SeatFillRate_ViewService;
import model.SeatFillRate_ViewDTO;

import java.util.List;

/**
 *
 * @author nguye
 */
public class FillRateEnvironment {

    private SeatFillRate_ViewService seatService;

    public FillRateEnvironment(SeatFillRate_ViewService seatService) {
        this.seatService = seatService;
    }

    // Lấy độ phủ ghế tháng hiện tại
    public double getCurrentMonthSeatCoverage() {
        return seatService.getSeatFillRateCurrentMonth();
    }

    // Lấy độ phủ theo từng suất chiếu
    public List<SeatFillRate_ViewDTO> getShowtimeCoverage() {
        return seatService.getSeatFillRateForShowtimeCurrentMonth();
    }
}

