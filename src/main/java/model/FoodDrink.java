package model;

import java.math.BigDecimal;

public class FoodDrink {
    private int itemId;
    private String itemName;
    private BigDecimal price;
    private int stockQuantity;

    public FoodDrink() {
        this.stockQuantity = 0;
    }

    public FoodDrink(int itemId, String itemName, BigDecimal price, int stockQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}

