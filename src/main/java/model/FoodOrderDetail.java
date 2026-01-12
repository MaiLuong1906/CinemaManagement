package model;

public class FoodOrderDetail {
    private int invoiceId;
    private int itemId;
    private int quantity;

    public FoodOrderDetail() {}

    public FoodOrderDetail(int invoiceId, int itemId, int quantity) {
        this.invoiceId = invoiceId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
