package com.zs.stockmanagement.reports.purchase.dao;

import com.zs.stockmanagement.reports.purchase.dto.PurchaseProductDetail;
import com.zs.stockmanagement.reports.purchase.dto.PurchaseSummaryResponse;
import com.zs.stockmanagement.reports.purchase.dto.PurchaseVariantDetail;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSummaryDAO {
    public PurchaseSummaryResponse getPurchaseSummary(int shopId, int branchId) {

        PurchaseSummaryResponse summary = new PurchaseSummaryResponse();
        List<PurchaseProductDetail> productDetails = new ArrayList<>();

        String ordersCountQuery = """
                SELECT COUNT(pb.purchase_id)
                FROM purchase_bill pb
                JOIN branches b ON pb.branch_id = b.branch_id
                WHERE b.shop_id = ? AND pb.branch_id = ? AND pb.status = 'ACTIVE'
                """;

        String topVendorQuery = """
                SELECT v.vendor_name
                FROM purchase_bill pb
                JOIN vendors v ON pb.vendor_id = v.vendor_id
                JOIN branches b ON pb.branch_id = b.branch_id
                WHERE b.shop_id = ? AND pb.branch_id = ? AND pb.status = 'ACTIVE'
                GROUP BY v.vendor_id, v.vendor_name
                ORDER BY SUM(pb.total_amount) DESC
                LIMIT 1
                """;

        String purchaseDataQuery = """
                SELECT p.product_id, p.product_name, pi.variant_id,
                       SUM(pi.quantity) AS total_qty, SUM(pi.total_amount) AS total_amt
                FROM purchase_item pi
                JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
                JOIN variants v ON pi.variant_id = v.variant_id
                JOIN products p ON v.product_id = p.product_id
                JOIN branches b ON p.branch_id = b.branch_id
                WHERE b.shop_id = ? AND pb.branch_id = ? AND pb.status = 'ACTIVE'
                GROUP BY p.product_id, p.product_name, pi.variant_id
                ORDER BY p.product_id, pi.variant_id
                """;

        double totalInvestment = 0.0;

        try (Connection con = DBController.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(ordersCountQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    summary.setTotalOrders(rs.getInt(1));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(topVendorQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    summary.setTopVendor(rs.getString(1));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(purchaseDataQuery)) {
                ps.setInt(1, shopId);
                ps.setInt(2, branchId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String productName = rs.getString("product_name");
                    int variantId = rs.getInt("variant_id");
                    int qty = rs.getInt("total_qty");
                    double amt = rs.getDouble("total_amt");

                    totalInvestment += amt;

                    PurchaseProductDetail product = null;
                    for (PurchaseProductDetail p : productDetails) {
                        if (p.getProductId() == productId) {
                            product = p;
                            break;
                        }
                    }

                    if (product == null) {
                        product = new PurchaseProductDetail();
                        product.setProductId(productId);
                        product.setProductName(productName);
                        product.setVariants(new ArrayList<>());
                        productDetails.add(product);
                    }

                    PurchaseVariantDetail variant = new PurchaseVariantDetail();
                    variant.setVariantId(variantId);
                    variant.setTotalPurchase(qty);
                    variant.setTotalAmount(amt);

                    product.getVariants().add(variant);
                }
            }

            summary.setTotalInvestment(totalInvestment);
            summary.setProductDetails(productDetails);

        } catch (SQLException e) {
            return null;
        }

        return summary;
    }
}
