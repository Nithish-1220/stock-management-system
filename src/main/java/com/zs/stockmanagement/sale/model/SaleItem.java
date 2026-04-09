package com.zs.stockmanagement.sale.model;

public class SaleItem {
    private int variantId;
    private int quantity;
    private double sellingPrice;
    private double totalAmount;

    public SaleItem(int variantId, double sellingPrice, int quantity, double totalAmount) {
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.totalAmount = totalAmount;
        this.variantId = variantId;
    }

    public SaleItem() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
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

    @Override
    public String toString() {
        return "SaleItem{" +
                "quantity=" + quantity +
                ", variantId=" + variantId +
                ", sellingPrice=" + sellingPrice +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
