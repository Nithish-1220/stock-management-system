package com.zs.stockmanagement.purchase.dto;

import com.zs.stockmanagement.purchase.model.PurchaseItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestPurchase {
    private int vendorId;
    private List<RequestPurchaseItem> requestPurchaseItems;
    private double totalAmount;

    public RequestPurchase(List<RequestPurchaseItem> requestPurchaseItems, double totalAmount, int vendorId) {
        this.requestPurchaseItems = requestPurchaseItems;
        this.totalAmount = totalAmount;
        this.vendorId = vendorId;
    }

    public RequestPurchase() {
    }

    @Override
    public String toString() {
        return "Purchase{" +
                ", vendorId=" + vendorId +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public List<RequestPurchaseItem> getRequestPurchaseItems() {
        return requestPurchaseItems;
    }

    public void setRequestPurchaseItems(List<RequestPurchaseItem> requestPurchaseItems) {
        this.requestPurchaseItems = requestPurchaseItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }
}
