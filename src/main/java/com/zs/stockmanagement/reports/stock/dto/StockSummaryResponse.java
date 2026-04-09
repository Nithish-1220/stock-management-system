package com.zs.stockmanagement.reports.stock.dto;

import java.util.List;

public class StockSummaryResponse {
    private long totalStockQuantity;
    private double totalStockValue;
    private List<StockProductDetail> productDetails;
    private List<StockVariantDetail> lowStock;


    public long getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public void setTotalStockQuantity(long totalStockQuantity) {
        this.totalStockQuantity = totalStockQuantity;
    }

    public double getTotalStockValue() {
        return totalStockValue;
    }

    public void setTotalStockValue(double totalStockValue) {
        this.totalStockValue = totalStockValue;
    }

    public List<StockProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<StockProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<StockVariantDetail> getLowStock() {
        return lowStock;
    }

    public void setLowStock(List<StockVariantDetail> lowStock) {
        this.lowStock = lowStock;
    }
}