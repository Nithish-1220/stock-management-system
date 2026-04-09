package com.zs.stockmanagement.reports.stock.dto;

import java.util.List;

public class StockProductDetail {
    private int productId;
    private String productName;
    private long totalStock;
    private double stockValue;
    private List<StockVariantDetail> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public long getTotalStock() { return totalStock; }
    public void setTotalStock(long totalStock) { this.totalStock = totalStock; }
    public double getStockValue() { return stockValue; }
    public void setStockValue(double stockValue) { this.stockValue = stockValue; }
    public List<StockVariantDetail> getVariants() { return variants; }
    public void setVariants(List<StockVariantDetail> variants) { this.variants = variants; }
}