package com.zs.stockmanagement.purchase.model;

public class PurchaseItem {
    private int productId;
    private String productName;
    private int variantId;
    private int quantity;
    private double costPrice;
    private double totalAmount;


    public PurchaseItem(int productId,String productName, int variantId, int quantity,double costPrice,double totalAmount) {
        this.productId = productId;
        this.productName = productName;
        this.variantId = variantId;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.totalAmount = totalAmount;
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

    public PurchaseItem() {}

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
