package com.zs.stockmanagement.purchase.model;

import java.time.LocalDateTime;
import java.util.*;

public class Purchase {
    private int purchaseId;
    private LocalDateTime dataTime;
    private int vendorId;
    private List<PurchaseItem> purchaseItems;
    private double totalAmount;

    public Purchase(LocalDateTime dataTime, int purchaseId, List<PurchaseItem> purchaseItems, double totalAmount, int vendorId) {
        this.dataTime = dataTime;
        this.purchaseId = purchaseId;
        this.purchaseItems = purchaseItems;
        this.totalAmount = totalAmount;
        this.vendorId = vendorId;
    }

    public Purchase(List<PurchaseItem> purchaseItems, double totalAmount, int vendorId) {
        this.purchaseItems = purchaseItems;
        this.totalAmount = totalAmount;
        this.vendorId = vendorId;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "dataTime=" + dataTime +
                ", purchaseId=" + purchaseId +
                ", vendorId=" + vendorId +
                ", purchaseItems=" + purchaseItems +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public Purchase()
    {
        purchaseItems = new ArrayList<>();
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(LocalDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
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
