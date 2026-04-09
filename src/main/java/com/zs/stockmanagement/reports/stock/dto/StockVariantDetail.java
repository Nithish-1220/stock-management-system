package com.zs.stockmanagement.reports.stock.dto;

public class StockVariantDetail {
    private int variantId;
    private int stockAvailable;
    private double stockValue;

    private String productName;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    public int getStockAvailable() { return stockAvailable; }
    public void setStockAvailable(int stockAvailable) { this.stockAvailable = stockAvailable; }
    public double getStockValue() { return stockValue; }
    public void setStockValue(double stockValue) { this.stockValue = stockValue; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}