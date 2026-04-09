package com.zs.stockmanagement.reports.sales.dto;

import java.util.List;

public class SalesSummaryResponse {
    private int totalOrders;
    private double totalRevenue;
    private String topSellingProduct;
    private List<SalesProductDetail> productDetails;

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public String getTopSellingProduct() { return topSellingProduct; }
    public void setTopSellingProduct(String topSellingProduct) { this.topSellingProduct = topSellingProduct; }
    public List<SalesProductDetail> getProductDetails() { return productDetails; }
    public void setProductDetails(List<SalesProductDetail> productDetails) { this.productDetails = productDetails; }
}