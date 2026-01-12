package model;

public class CinemaHall {
    private int hallId;
    private String hallName;

    public CinemaHall() {}

    public CinemaHall(int hallId, String hallName) {
        this.hallId = hallId;
        this.hallName = hallName;
    }

    // Getters and Setters
    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }
}

