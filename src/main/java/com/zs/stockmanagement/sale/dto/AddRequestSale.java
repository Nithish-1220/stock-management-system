package com.zs.stockmanagement.sale.dto;

import com.zs.stockmanagement.sale.model.SaleItem;

import java.util.List;

public class AddRequestSale {
    private int customerId;
    private List<AddRequestSaleItem> saleItems;
    private double totalAmount;

    public AddRequestSale(int customerId, List<AddRequestSaleItem> saleItems, String status, double totalAmount) {
        this.customerId = customerId;
        this.saleItems = saleItems;
        this.totalAmount = totalAmount;
    }

    public AddRequestSale() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<AddRequestSaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<AddRequestSaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
