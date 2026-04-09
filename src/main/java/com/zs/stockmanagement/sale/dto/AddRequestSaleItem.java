package com.zs.stockmanagement.sale.dto;

public class AddRequestSaleItem {
    private int variantId;
    private int quantity;
    private double sellingPrice;
    private double totalAmount;

    public AddRequestSaleItem(int variantId,double sellingPrice,  int quantity ,double totalAmount) {
        this.variantId = variantId;
        this.totalAmount = totalAmount;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    public AddRequestSaleItem() {
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
}
