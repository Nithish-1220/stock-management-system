package com.zs.stockmanagement.inventory.model;

public class Stock {
    private int variantId;
    private int quantity;

    @Override
    public String toString() {
        return "Stock{" +
                "quantity=" + quantity +
                ", variantId=" + variantId +
                '}';
    }

    public Stock() {

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public Stock(int quantity, int variantId) {
        this.quantity = quantity;
        this.variantId = variantId;
    }
}
