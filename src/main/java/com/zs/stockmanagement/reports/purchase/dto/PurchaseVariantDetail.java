package com.zs.stockmanagement.reports.purchase.dto;

public class PurchaseVariantDetail {
    private int variantId;
    private int totalPurchase;
    private double totalAmount;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    public int getTotalPurchase() { return totalPurchase; }
    public void setTotalPurchase(int totalPurchase) { this.totalPurchase = totalPurchase; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}