package model;

import java.time.LocalDate;

public class ForecastDTO {
    private LocalDate date;
    private double actualRevenue;
    private int actualTickets;
    private double forecastRevenue;
    private int forecastTickets;
    private boolean isFuture;

    public ForecastDTO() {}

    public ForecastDTO(LocalDate date, double actualRevenue, int actualTickets, boolean isFuture) {
        this.date = date;
        this.actualRevenue = actualRevenue;
        this.actualTickets = actualTickets;
        this.isFuture = isFuture;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getActualRevenue() {
        return actualRevenue;
    }

    public void setActualRevenue(double actualRevenue) {
        this.actualRevenue = actualRevenue;
    }

    public int getActualTickets() {
        return actualTickets;
    }

    public void setActualTickets(int actualTickets) {
        this.actualTickets = actualTickets;
    }

    public double getForecastRevenue() {
        return forecastRevenue;
    }

    public void setForecastRevenue(double forecastRevenue) {
        this.forecastRevenue = forecastRevenue;
    }

    public int getForecastTickets() {
        return forecastTickets;
    }

    public void setForecastTickets(int forecastTickets) {
        this.forecastTickets = forecastTickets;
    }

    public boolean isFuture() {
        return isFuture;
    }

    public void setFuture(boolean future) {
        isFuture = future;
    }
}
