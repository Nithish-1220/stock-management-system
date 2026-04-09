package com.zs.stockmanagement.reports.profit.resource;

import com.zs.stockmanagement.reports.profit.dto.ProfitSummaryResponse;
import com.zs.stockmanagement.reports.profit.service.ProfitSummaryService;
import com.zs.stockmanagement.reports.purchase.dto.PurchaseSummaryResponse;
import com.zs.stockmanagement.reports.purchase.service.PurchaseSummaryService;
import com.zs.stockmanagement.reports.sales.dto.SalesSummaryResponse;
import com.zs.stockmanagement.reports.sales.service.SalesSummaryService;
import com.zs.stockmanagement.reports.stock.dto.StockSummaryResponse;
import com.zs.stockmanagement.reports.stock.service.StockSummaryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

    private final ProfitSummaryService profitSummaryService = new ProfitSummaryService();
    private final StockSummaryService stockSummaryService = new StockSummaryService();
    private final SalesSummaryService salesSummaryService = new SalesSummaryService();
    private final PurchaseSummaryService purchaseSummaryService = new PurchaseSummaryService();

    private final int shopId;
    private final int branchId;

    public ReportResource(int shopId, int branchId) {
        this.shopId = shopId;
        this.branchId = branchId;
    }

    @GET
    @Path("/profit-summary")
    public Response getProfitSummary(@QueryParam("startDate") String startDateStr, @QueryParam("endDate") String endDateStr) {

        try {
            if (startDateStr == null || endDateStr == null || startDateStr.trim().isEmpty() || endDateStr.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{ error in date}")
                        .build();
            }

            LocalDateTime startDate = LocalDateTime.parse(startDateStr);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr);
            ProfitSummaryResponse summary = profitSummaryService.getProfitSummary(shopId, branchId, startDate, endDate);
            return Response.ok(summary).build();

        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{ error in date}")
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{error}")
                    .build();
        }
    }

    @GET
    @Path("/stock-summary")
    public Response getStockSummary(@QueryParam("threshold") @DefaultValue("10") int threshold) {
        try {
            StockSummaryResponse summary = stockSummaryService.getStockSummary(shopId, branchId, threshold);
            return Response.ok(summary).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{error in stock summary}")
                    .build();
        }
    }

    @GET
    @Path("/sales-summary")
    public SalesSummaryResponse getSalesSummary() {
        try {
            return salesSummaryService.getSalesSummary(shopId, branchId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GET
    @Path("/purchase-summary")
    public PurchaseSummaryResponse getPurchaseSummary() {
        try {
            return purchaseSummaryService.getPurchaseSummary(shopId, branchId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}