package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showtime {
    private int showtimeId;
    private int movieId;
    private int hallId;
    private LocalDateTime startTime;
    private BigDecimal basePrice;

    public Showtime() {}

    public Showtime(int showtimeId, int movieId, int hallId, LocalDateTime startTime, BigDecimal basePrice) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }
    // cons them db
    public Showtime(int movieId, int hallId, LocalDateTime startTime, BigDecimal basePrice) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
}
