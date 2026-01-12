package model;

import java.math.BigDecimal;

public class TicketDetail {
    private int invoiceId;
    private int seatId;
    private int hallId;
    private BigDecimal actualPrice;

    public TicketDetail() {}

    public TicketDetail(int invoiceId, int seatId, int hallId, BigDecimal actualPrice) {
        this.invoiceId = invoiceId;
        this.seatId = seatId;
        this.hallId = hallId;
        this.actualPrice = actualPrice;
    }

    // Getters and Setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public BigDecimal getActualPrice() { return actualPrice; }
    public void setActualPrice(BigDecimal actualPrice) { this.actualPrice = actualPrice; }
}

