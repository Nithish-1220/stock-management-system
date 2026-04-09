package com.zs.stockmanagement.reports.sales.dao;

import com.zs.stockmanagement.reports.sales.dto.SalesProductDetail;
import com.zs.stockmanagement.reports.sales.dto.SalesSummaryResponse;
import com.zs.stockmanagement.reports.sales.dto.SalesVariantDetail;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesSummaryDAO {
    public SalesSummaryResponse getSalesSummary(int shopId, int branchId) {

        SalesSummaryResponse summary = new SalesSummaryResponse();
        List<SalesProductDetail> productDetails = new ArrayList<>();

        String ordersCountQuery = """
                SELECT COUNT(sb.sales_id)
                FROM sales_bill sb
                JOIN branches b ON sb.branch_id = b.branch_id
                WHERE b.shop_id = ? AND sb.branch_id = ? AND sb.status = 'ACTIVE'
                """;

        String salesDataQuery = """
                SELECT p.product_id, p.product_name, si.variant_id,
                       SUM(si.quantity) AS total_qty, SUM(si.total_amount) AS total_amt
                FROM sales_item si
                JOIN sales_bill sb ON si.sales_id = sb.sales_id
                JOIN variants v ON si.variant_id = v.variant_id
                JOIN products p ON v.product_id = p.product_id
                JOIN branches b ON p.branch_id = b.branch_id
                WHERE b.shop_id = ? AND sb.branch_id = ? AND sb.status = 'ACTIVE'
                GROUP BY p.product_id, p.product_name, si.variant_id
                ORDER BY p.product_id, si.variant_id
                """;

        double totalRevenue = 0.0;

        try (Connection con = DBController.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(ordersCountQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    summary.setTotalOrders(rs.getInt(1));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(salesDataQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String productName = rs.getString("product_name");
                    int variantId = rs.getInt("variant_id");
                    int qty = rs.getInt("total_qty");
                    double amt = rs.getDouble("total_amt");

                    totalRevenue += amt;

                    SalesProductDetail product = null;
                    for (SalesProductDetail p : productDetails) {
                        if (p.getProductId() == productId) {
                            product = p;
                            break;
                        }
                    }

                    if (product == null) {
                        product = new SalesProductDetail();
                        product.setProductId(productId);
                        product.setProductName(productName);
                        product.setVariants(new ArrayList<>());
                        productDetails.add(product);
                    }

                    SalesVariantDetail variant = new SalesVariantDetail();
                    variant.setVariantId(variantId);
                    variant.setTotalSales(qty);
                    variant.setTotalAmount(amt);

                    product.getVariants().add(variant);
                }
            }

            String topSellingProduct = null;
            int maxSales = -1;

            for (SalesProductDetail p : productDetails) {
                int currentProductTotalSales = 0;
                for (SalesVariantDetail v : p.getVariants()) {
                    currentProductTotalSales += v.getTotalSales();
                }

                if (currentProductTotalSales > maxSales) {
                    maxSales = currentProductTotalSales;
                    topSellingProduct = p.getProductName();
                }
            }

            summary.setTotalRevenue(totalRevenue);
            summary.setTopSellingProduct(topSellingProduct);
            summary.setProductDetails(productDetails);

        } catch (SQLException e) {
            return null;
        }

        return summary;
    }
}
