package com.zs.stockmanagement.reports.profit.dao;

import com.zs.stockmanagement.reports.profit.dto.ProfitProductDetail;
import com.zs.stockmanagement.reports.profit.dto.ProfitSummaryResponse;
import com.zs.stockmanagement.reports.profit.dto.ProfitVariantDetail;
import com.zs.stockmanagement.utils.DBController;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ProfitSummaryDAO {

    public ProfitSummaryResponse getProfitSummary(int shopId, int branchId, LocalDateTime startDate, LocalDateTime endDate) {

        ProfitSummaryResponse summary = new ProfitSummaryResponse();

        Timestamp start = Timestamp.valueOf(startDate);
        Timestamp end = Timestamp.valueOf(endDate);

        String purchaseBillCountQuery = "SELECT COUNT(*) FROM purchase_bill pb JOIN branches b ON pb.branch_id = b.branch_id WHERE b.shop_id = ? AND pb.branch_id = ? AND pb.status = 'ACTIVE' AND pb.date_time BETWEEN ? AND ?";
        String salesBillCountQuery = "SELECT COUNT(*) FROM sales_bill sb JOIN branches b ON sb.branch_id = b.branch_id WHERE b.shop_id = ? AND sb.branch_id = ? AND sb.status = 'ACTIVE' AND sb.date_time BETWEEN ? AND ?";

        String purchaseQuery = """
            SELECT p.product_id, p.product_name, pi.variant_id, 
                   SUM(pi.quantity) AS pur_qty, SUM(pi.total_amount) AS pur_amt
            FROM purchase_item pi
            JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
            JOIN variants v ON pi.variant_id = v.variant_id
            JOIN products p ON v.product_id = p.product_id
            JOIN branches b ON pb.branch_id = b.branch_id
            WHERE b.shop_id = ? AND pb.branch_id = ? AND pb.status = 'ACTIVE' AND pb.date_time BETWEEN ? AND ?
            GROUP BY p.product_id, p.product_name, pi.variant_id
            """;

        String salesQuery = """
            SELECT p.product_id, p.product_name, si.variant_id, 
                   SUM(si.quantity) AS sale_qty, SUM(si.total_amount) AS sale_amt
            FROM sales_item si
            JOIN sales_bill sb ON si.sales_id = sb.sales_id
            JOIN variants v ON si.variant_id = v.variant_id
            JOIN products p ON v.product_id = p.product_id
            JOIN branches b ON sb.branch_id = b.branch_id
            WHERE b.shop_id = ? AND sb.branch_id = ? AND sb.status = 'ACTIVE' AND sb.date_time BETWEEN ? AND ?
            GROUP BY p.product_id, p.product_name, si.variant_id
            """;

        List<ProfitProductDetail> productList = new ArrayList<>();

        int runningTotalItemsPurchased = 0;
        int runningTotalItemsSold = 0;
        double runningTotalInvestment = 0.0;
        double runningTotalSales = 0.0;

        try (Connection connection = DBController.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(purchaseBillCountQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ps.setTimestamp(3, start);
                ps.setTimestamp(4, end);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) summary.setTotalPurchaseBills(rs.getInt(1));
            }

            try (PreparedStatement ps = connection.prepareStatement(salesBillCountQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ps.setTimestamp(3, start);
                ps.setTimestamp(4, end);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) summary.setTotalSalesBills(rs.getInt(1));
            }

            try (PreparedStatement ps = connection.prepareStatement(purchaseQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ps.setTimestamp(3, start);
                ps.setTimestamp(4, end);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int variantId = rs.getInt("variant_id");

                    ProfitProductDetail product = null;
                    for (ProfitProductDetail p : productList) {
                        if (p.getProductId() == productId) {
                            product = p;
                            break;
                        }
                    }
                    if (product == null) {
                        product = new ProfitProductDetail();
                        product.setProductId(productId);
                        product.setProductName(rs.getString("product_name"));
                        product.setVariants(new ArrayList<>());
                        productList.add(product);
                    }

                    ProfitVariantDetail variant = null;
                    for (ProfitVariantDetail v : product.getVariants()) {
                        if (v.getVariantId() == variantId) {
                            variant = v;
                            break;
                        }
                    }
                    if (variant == null) {
                        variant = new ProfitVariantDetail();
                        variant.setVariantId(variantId);
                        product.getVariants().add(variant);
                    }

                    int purQty = rs.getInt("pur_qty");
                    double purAmt = rs.getDouble("pur_amt");

                    variant.setTotalItemsPurchased(purQty);
                    variant.setTotalPurchaseAmount(purAmt);

                    runningTotalItemsPurchased += purQty;
                    runningTotalInvestment += purAmt;
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(salesQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ps.setTimestamp(3, start);
                ps.setTimestamp(4, end);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int variantId = rs.getInt("variant_id");

                    ProfitProductDetail product = null;
                    for (ProfitProductDetail p : productList) {
                        if (p.getProductId() == productId) {
                            product = p;
                            break;
                        }
                    }
                    if (product == null) {
                        product = new ProfitProductDetail();
                        product.setProductId(productId);
                        product.setProductName(rs.getString("product_name"));
                        product.setVariants(new ArrayList<>());
                        productList.add(product);
                    }

                    ProfitVariantDetail variant = null;
                    for (ProfitVariantDetail v : product.getVariants()) {
                        if (v.getVariantId() == variantId) {
                            variant = v;
                            break;
                        }
                    }
                    if (variant == null) {
                        variant = new ProfitVariantDetail();
                        variant.setVariantId(variantId);
                        product.getVariants().add(variant);
                    }

                    int saleQty = rs.getInt("sale_qty");
                    double saleAmt = rs.getDouble("sale_amt");

                    variant.setTotalItemsSold(saleQty);
                    variant.setTotalSalesAmount(saleAmt);

                    runningTotalItemsSold += saleQty;
                    runningTotalSales += saleAmt;
                }
            }

            for (ProfitProductDetail product : productList) {
                for (ProfitVariantDetail variant : product.getVariants()) {
                    double profit = variant.getTotalSalesAmount() - variant.getTotalPurchaseAmount();
                    variant.setProfit(profit);
                }
            }

            summary.setTotalItemsPurchased(runningTotalItemsPurchased);
            summary.setTotalItemsSold(runningTotalItemsSold);
            summary.setTotalInvestment(runningTotalInvestment);
            summary.setTotalSalesAmount(runningTotalSales);
            summary.setTotalProfit(runningTotalSales - runningTotalInvestment);
            summary.setProductDetails(productList);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching profit summary");
        }

        return summary;
    }
}
