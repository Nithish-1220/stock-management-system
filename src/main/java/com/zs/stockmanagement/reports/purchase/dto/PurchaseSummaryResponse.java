package com.zs.stockmanagement.reports.purchase.dto;

import java.util.List;

public class PurchaseSummaryResponse {
    private int totalOrders;
    private double totalInvestment;
    private String topVendor;
    private List<PurchaseProductDetail> productDetails;

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(double totalInvestment) { this.totalInvestment = totalInvestment; }
    public String getTopVendor() { return topVendor; }
    public void setTopVendor(String topVendor) { this.topVendor = topVendor; }
    public List<PurchaseProductDetail> getProductDetails() { return productDetails; }
    public void setProductDetails(List<PurchaseProductDetail> productDetails) { this.productDetails = productDetails; }
}