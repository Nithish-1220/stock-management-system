package com.zs.stockmanagement.reports.profit.service;

import com.zs.stockmanagement.reports.profit.dao.ProfitSummaryDAO;
import com.zs.stockmanagement.reports.profit.dto.ProfitSummaryResponse;

import java.time.LocalDateTime;

public class ProfitSummaryService {

    private final ProfitSummaryDAO profitSummaryDAO = new ProfitSummaryDAO();

    public ProfitSummaryResponse getProfitSummary(int shopId, int branchId, LocalDateTime startDate, LocalDateTime endDate) {

        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date / End date is null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after the End date");
        }

        return profitSummaryDAO.getProfitSummary(shopId, branchId, startDate, endDate);
    }
}