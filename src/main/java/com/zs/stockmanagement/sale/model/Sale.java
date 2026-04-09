package com.zs.stockmanagement.sale.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class Sale {
    private int saleId;
    private LocalDateTime dateTime;
    private int customerId;
    private List<SaleItem> saleItems;
    private double totalAmount;
    private String status;

    public Sale(int customerId, LocalDateTime dateTime, List<SaleItem> saleItems, double totalAmount) {
        this.customerId = customerId;
        this.dateTime = dateTime;
        this.saleItems = saleItems;
        this.totalAmount = totalAmount;
    }

    public Sale(int customerId,List<SaleItem> saleItems, double totalAmount) {
        this.customerId = customerId;
        this.saleItems = saleItems;
        this.totalAmount = totalAmount;
    }


    public Sale() {
        saleItems = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "customerId=" + customerId +
                ", saleId=" + saleId +
                ", dateTime=" + dateTime +
                ", saleItems=" + saleItems +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
