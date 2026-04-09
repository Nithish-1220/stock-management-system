package com.zs.stockmanagement.reports.stock.service;


import com.zs.stockmanagement.reports.stock.dao.StockSummaryDAO;
import com.zs.stockmanagement.reports.stock.dto.StockSummaryResponse;

public class StockSummaryService {

    private final StockSummaryDAO dao;

    public StockSummaryService() {
        this.dao = new StockSummaryDAO();
    }

    public StockSummaryResponse getStockSummary(int shopId, int branchId, int lowStockThreshold) {
        
        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("Invalid shopId or branchId");
        }
        
        if (lowStockThreshold < 0) {
            throw new IllegalArgumentException("Low stock threshold is negative");
        }

        return dao.getStockSummary(shopId, branchId, lowStockThreshold);
    }
}