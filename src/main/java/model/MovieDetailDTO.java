package model;

import java.time.LocalTime;

public class MovieDetailDTO {

    private int showtimeId;          // ✅ đổi từ movieId
    private String movieTitle;
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String hallName;
    private String genres;

    public MovieDetailDTO() {
    }

    public MovieDetailDTO(int showtimeId,String movieTitle,String slotName,LocalTime startTime,
                          LocalTime endTime,String hallName, String genres) {
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.slotName = slotName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hallName = hallName;
        this.genres = genres;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getSlotName() {
        return slotName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getHallName() {
        return hallName;
    }

    public String getGenres() {
        return genres;
    }
}
