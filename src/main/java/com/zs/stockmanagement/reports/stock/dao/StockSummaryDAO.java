package com.zs.stockmanagement.reports.stock.dao;

import com.zs.stockmanagement.reports.stock.dto.StockProductDetail;
import com.zs.stockmanagement.reports.stock.dto.StockSummaryResponse;
import com.zs.stockmanagement.reports.stock.dto.StockVariantDetail;
import com.zs.stockmanagement.utils.DBController;

import java.sql.*;
import java.util.*;

public class StockSummaryDAO {
    public StockSummaryResponse getStockSummary(int shopId, int branchId, int lowStockThreshold) {

        StockSummaryResponse summary = new StockSummaryResponse();
        summary.setProductDetails(new ArrayList<>());
        summary.setLowStock(new ArrayList<>());

        String stockQuery = """
            SELECT
                p.product_id,
                p.product_name,
                v.variant_id,
                v.price,
                i.quantity
            FROM inventory i
            JOIN variants v ON i.variant_id = v.variant_id
            JOIN products p ON v.product_id = p.product_id
            JOIN branches b ON i.branch_id = b.branch_id
            WHERE b.shop_id = ?
              AND i.branch_id = ?
              AND p.is_deleted = FALSE
              AND v.is_deleted = FALSE
            ORDER BY p.product_id, v.variant_id
            """;

        long grandTotalQuantity = 0;
        double grandTotalValue = 0.0;

        try (Connection con = DBController.getConnection();
             PreparedStatement ps = con.prepareStatement(stockQuery)) {

            ps.setInt(1, shopId);
            ps.setInt(2, branchId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int variantId = rs.getInt("variant_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                double variantStockValue = quantity * price;

                StockVariantDetail variant = new StockVariantDetail();
                variant.setVariantId(variantId);
                variant.setStockAvailable(quantity);
                variant.setStockValue(variantStockValue);
                variant.setProductName(productName);

                if (quantity <= lowStockThreshold) {
                    summary.getLowStock().add(variant);
                }

                StockProductDetail product = null;
                for (StockProductDetail p : summary.getProductDetails()) {
                    if (p.getProductId() == productId) {
                        product = p;
                        break;
                    }
                }

                if (product == null) {
                    product = new StockProductDetail();
                    product.setProductId(productId);
                    product.setProductName(productName);
                    product.setTotalStock(0);
                    product.setStockValue(0.0);
                    product.setVariants(new ArrayList<>());

                    summary.getProductDetails().add(product);
                }

                product.getVariants().add(variant);
                product.setTotalStock(product.getTotalStock() + quantity);
                product.setStockValue(product.getStockValue() + variantStockValue);

                grandTotalQuantity += quantity;
                grandTotalValue += variantStockValue;
            }

            summary.setTotalStockQuantity(grandTotalQuantity);
            summary.setTotalStockValue(grandTotalValue);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching stock summary", e);
        }

        return summary;
    }
}
