package com.zs.stockmanagement.reports.purchase.service;

import com.zs.stockmanagement.reports.purchase.dao.PurchaseSummaryDAO;
import com.zs.stockmanagement.reports.purchase.dto.PurchaseSummaryResponse;

public class PurchaseSummaryService {

    private final PurchaseSummaryDAO dao;

    public PurchaseSummaryService() {
        this.dao = new PurchaseSummaryDAO();
    }

    public PurchaseSummaryResponse getPurchaseSummary(int shopId, int branchId) {

        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("invalid shopId/branchId");
        }

        PurchaseSummaryResponse summary = dao.getPurchaseSummary(shopId, branchId);
        if(summary==null){
            throw new RuntimeException("purchase Summary is null");
        }
        return summary;
    }
}