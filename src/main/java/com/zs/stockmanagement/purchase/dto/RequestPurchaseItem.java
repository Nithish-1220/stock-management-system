package com.zs.stockmanagement.purchase.dto;

public class RequestPurchaseItem {
    private int variantId;
    private int quantity;
    private double costPrice;
    private double totalAmount;

    public RequestPurchaseItem(double costPrice, int quantity, double totalAmount, int variantId) {
        this.costPrice = costPrice;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.variantId = variantId;
    }


    @Override
    public String toString() {
        return "PurchaseItem{" +
                "costPrice=" + costPrice +
                ", variantId=" + variantId +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public RequestPurchaseItem() {

    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
}
