package com.zs.stockmanagement.reports.profit.dto;

import java.util.List;

public class ProfitSummaryResponse {
    private int totalItemsSold;
    private int totalItemsPurchased;
    private int totalSalesBills;
    private int totalPurchaseBills;
    private double totalInvestment;
    private double totalSalesAmount;
    private double totalProfit;
    private List<ProfitProductDetail> productDetails;

    public int getTotalItemsSold() { return totalItemsSold; }
    public void setTotalItemsSold(int totalItemsSold) { this.totalItemsSold = totalItemsSold; }
    public int getTotalItemsPurchased() { return totalItemsPurchased; }
    public void setTotalItemsPurchased(int totalItemsPurchased) { this.totalItemsPurchased = totalItemsPurchased; }
    public int getTotalSalesBills() { return totalSalesBills; }
    public void setTotalSalesBills(int totalSalesBills) { this.totalSalesBills = totalSalesBills; }
    public int getTotalPurchaseBills() { return totalPurchaseBills; }
    public void setTotalPurchaseBills(int totalPurchaseBills) { this.totalPurchaseBills = totalPurchaseBills; }
    public double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(double totalInvestment) { this.totalInvestment = totalInvestment; }
    public double getTotalSalesAmount() { return totalSalesAmount; }
    public void setTotalSalesAmount(double totalSalesAmount) { this.totalSalesAmount = totalSalesAmount; }
    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }
    public List<ProfitProductDetail> getProductDetails() { return productDetails; }
    public void setProductDetails(List<ProfitProductDetail> productDetails) { this.productDetails = productDetails; }
}