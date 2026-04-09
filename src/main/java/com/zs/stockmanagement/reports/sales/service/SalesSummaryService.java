package com.zs.stockmanagement.reports.sales.service;

import com.zs.stockmanagement.reports.sales.dao.SalesSummaryDAO;
import com.zs.stockmanagement.reports.sales.dto.SalesSummaryResponse;
import com.zs.stockmanagement.sale.model.Sale;

public class SalesSummaryService {

    private final SalesSummaryDAO dao;

    public SalesSummaryService() {
        this.dao = new SalesSummaryDAO();
    }

    public SalesSummaryResponse getSalesSummary(int shopId, int branchId) {

        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("Invalid shopId or branchId.");
        }

        SalesSummaryResponse summary = dao.getSalesSummary(shopId, branchId);
        if(summary==null){
            throw new RuntimeException("summary Is null");
        }
        return summary;
    }
}