package model;

public class Seat {
    private int seatId;
    private int hallId;
    private String seatCode;
    private int seatTypeId;

    public Seat() {}

    public Seat(int seatId, int hallId, String seatCode, int seatTypeId) {
        this.seatId = seatId;
        this.hallId = hallId;
        this.seatCode = seatCode;
        this.seatTypeId = seatTypeId;
    }

    // Getters and Setters
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public String getSeatCode() { return seatCode; }
    public void setSeatCode(String seatCode) { this.seatCode = seatCode; }

    public int getSeatTypeId() { return seatTypeId; }
    public void setSeatTypeId(int seatTypeId) { this.seatTypeId = seatTypeId; }
}
