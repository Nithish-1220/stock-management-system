package com.zs.stockmanagement.product.model;

import java.time.LocalDateTime;

public class FlowEntry {

    private String type;
    private LocalDateTime date;
    private Integer purchaseId;
    private Integer salesId;
    private int quantity;
    private double price;

    public FlowEntry(LocalDateTime date, double price, Integer purchaseId, int quantity, Integer salesId, String type) {
        this.date = date;
        this.price = price;
        this.purchaseId = purchaseId;
        this.quantity = quantity;
        this.salesId = salesId;
        this.type = type;
    }

    public FlowEntry() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FlowEntry{" +
                "date=" + date +
                ", type='" + type + '\'' +
                ", purchaseId=" + purchaseId +
                ", salesId=" + salesId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }
}