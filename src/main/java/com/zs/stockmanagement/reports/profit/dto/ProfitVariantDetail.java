package com.zs.stockmanagement.reports.profit.dto;

public class ProfitVariantDetail {
    private int variantId;
    private int totalItemsPurchased;
    private int totalItemsSold;
    private double totalSalesAmount;
    private double totalPurchaseAmount;
    private double profit;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    public int getTotalItemsPurchased() { return totalItemsPurchased; }
    public void setTotalItemsPurchased(int totalItemsPurchased) { this.totalItemsPurchased = totalItemsPurchased; }
    public int getTotalItemsSold() { return totalItemsSold; }
    public void setTotalItemsSold(int totalItemsSold) { this.totalItemsSold = totalItemsSold; }
    public double getTotalSalesAmount() { return totalSalesAmount; }
    public void setTotalSalesAmount(double totalSalesAmount) { this.totalSalesAmount = totalSalesAmount; }
    public double getTotalPurchaseAmount() { return totalPurchaseAmount; }
    public void setTotalPurchaseAmount(double totalPurchaseAmount) { this.totalPurchaseAmount = totalPurchaseAmount; }
    public double getProfit() { return profit; }
    public void setProfit(double profit) { this.profit = profit; }
}