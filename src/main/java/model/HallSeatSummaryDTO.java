//package model;
//
//import model.CinemaHall;
//
//public class HallSeatSummaryDTO {
//    private CinemaHall hall;
//    private int totalSeats;
//    private int activeSeats;
//    private int inactiveSeats;
//    
//    public HallSeatSummaryDTO(CinemaHall hall, int totalSeats, int activeSeats) {
//        this.hall = hall;
//        this.totalSeats = totalSeats;
//        this.activeSeats = activeSeats;
//        this.inactiveSeats = totalSeats - activeSeats;
//    }
//    
//    // Getters
//    public CinemaHall getHall() { return hall; }
//    public int getTotalSeats() { return totalSeats; }
//    public int getActiveSeats() { return activeSeats; }
//    public int getInactiveSeats() { return inactiveSeats; }
//    
//    public String getStatusText() {
//        return hall.getStatus() ? "Hoạt động" : "Tạm đóng";
//    }
//}