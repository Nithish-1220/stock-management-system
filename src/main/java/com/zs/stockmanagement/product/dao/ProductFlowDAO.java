package com.zs.stockmanagement.product.dao;

import com.zs.stockmanagement.product.dto.*;
import com.zs.stockmanagement.product.model.*;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFlowDAO {

    public List<ProductFlowResponse> getProductFlow(int shopId, int branchId) {

        String productQuery = """
                SELECT p.product_id, p.product_name
                FROM products p
                JOIN branches b ON p.branch_id = b.branch_id
                WHERE b.shop_id = ?
                AND b.branch_id = ?
                AND p.is_deleted = FALSE
                """;

        String variantQuery = """
                SELECT v.variant_id
                FROM variants v
                JOIN products p ON v.product_id = p.product_id
                WHERE p.product_id = ?
                AND v.is_deleted = FALSE
                """;

        String attributeQuery = """
                SELECT a.attribute_key, va.attribute_value
                FROM variant_attribute va
                JOIN attribute a ON va.attribute_id = a.attribute_id
                WHERE va.variant_id = ?
                """;

        String flowQuery = """
                SELECT 'PURCHASE' AS type,
                       pb.date_time,
                       pb.purchase_id AS ref_id,
                       pi.quantity,
                       pi.cost_price AS price
                FROM purchase_item pi
                JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
                WHERE pi.variant_id = ?
                AND pb.status = 'ACTIVE'
                
                UNION ALL
                
                SELECT 'SALE' AS type,
                       sb.date_time,
                       sb.sales_id AS ref_id,
                       si.quantity,
                       si.selling_price AS price
                FROM sales_item si
                JOIN sales_bill sb ON si.sales_id = sb.sales_id
                WHERE si.variant_id = ?
                AND sb.status = 'ACTIVE'
                
                ORDER BY date_time
                """;

        List<ProductFlowResponse> productList = new ArrayList<>();

        try (Connection con = DBController.getConnection();
             PreparedStatement productPs = con.prepareStatement(productQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);
            ResultSet productRs = productPs.executeQuery();
            while (productRs.next()) {
                ProductFlowResponse product = new ProductFlowResponse();
                product.setProductId(productRs.getInt("product_id"));
                product.setProductName(productRs.getString("product_name"));
                List<VariantFlowResponse> variantList = new ArrayList<>();

                try (PreparedStatement variantPs = con.prepareStatement(variantQuery)) {
                    variantPs.setInt(1, product.getProductId());
                    ResultSet variantRs = variantPs.executeQuery();
                    while (variantRs.next()) {
                        VariantFlowResponse variant = new VariantFlowResponse();
                        int variantId = variantRs.getInt("variant_id");
                        variant.setVariantId(variantId);
                        Map<String, String> attributes = new HashMap<>();
                        try (PreparedStatement attrPs = con.prepareStatement(attributeQuery)) {
                            attrPs.setInt(1, variantId);
                            ResultSet attrRs = attrPs.executeQuery();
                            while (attrRs.next()) {
                                attributes.put(
                                        attrRs.getString("attribute_key"),
                                        attrRs.getString("attribute_value")
                                );
                            }
                        }
                        variant.setAttributes(attributes);

                        List<FlowEntry> flowList = new ArrayList<>();
                        try (PreparedStatement flowPs = con.prepareStatement(flowQuery)) {

                            flowPs.setInt(1, variantId);
                            flowPs.setInt(2, variantId);
                            ResultSet flowRs = flowPs.executeQuery();
                            while (flowRs.next()) {
                                FlowEntry flow = new FlowEntry();
                                flow.setType(flowRs.getString("type"));
                                flow.setDate(flowRs.getTimestamp("date_time").toLocalDateTime());
                                flow.setQuantity(flowRs.getInt("quantity"));
                                flow.setPrice(flowRs.getDouble("price"));
                                if ("PURCHASE".equals(flow.getType())) {
                                    flow.setPurchaseId(flowRs.getInt("ref_id"));
                                } else {
                                    flow.setSalesId(flowRs.getInt("ref_id"));
                                }
                                System.out.println(flow);
                                flowList.add(flow);
                            }
                        }
                        variant.setFlow(flowList);
                        variantList.add(variant);
                    }
                }
                product.setVariants(variantList);
                productList.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product ");
        }
        return productList;
    }

    public ProductFlowResponse getProductFlowById(int shopId, int branchId, int productId) {

        String productQuery = """
                SELECT p.product_id, p.product_name
                FROM products p
                JOIN branches b ON p.branch_id = b.branch_id
                WHERE b.shop_id = ?
                AND b.branch_id = ?
                AND p.product_id = ?
                AND p.is_deleted = FALSE
                """;

        String variantQuery = """
                SELECT v.variant_id
                FROM variants v
                JOIN products p ON v.product_id = p.product_id
                WHERE p.product_id = ?
                AND v.is_deleted = FALSE
                """;

        String attributeQuery = "SELECT a.attribute_key, va.attribute_value FROM variant_attribute va JOIN attribute a ON va.attribute_id = a.attribute_id WHERE va.variant_id = ?";

        String flowQuery = """
                SELECT 'PURCHASE' AS type, pb.date_time, pb.purchase_id AS ref_id, pi.quantity, pi.cost_price AS price
                FROM purchase_item pi JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
                WHERE pi.variant_id = ? AND pb.status = 'ACTIVE'
                UNION ALL
                SELECT 'SALE' AS type, sb.date_time, sb.sales_id AS ref_id, si.quantity, si.selling_price AS price
                FROM sales_item si JOIN sales_bill sb ON si.sales_id = sb.sales_id
                WHERE si.variant_id = ? AND sb.status = 'ACTIVE'
                ORDER BY date_time
                """;

        try (Connection con = DBController.getConnection();
             PreparedStatement productPs = con.prepareStatement(productQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);
            productPs.setInt(3, productId);
            ResultSet productRs = productPs.executeQuery();
            if (productRs.next()) {
                ProductFlowResponse product = new ProductFlowResponse();
                product.setProductId(productRs.getInt("product_id"));
                product.setProductName(productRs.getString("product_name"));
                List<VariantFlowResponse> variantList = new ArrayList<>();

                try (PreparedStatement variantPs = con.prepareStatement(variantQuery)) {

                    variantPs.setInt(1, product.getProductId());
                    ResultSet variantRs = variantPs.executeQuery();
                    while (variantRs.next()) {
                        VariantFlowResponse variant = new VariantFlowResponse();
                        int currentVariantId = variantRs.getInt("variant_id");
                        variant.setVariantId(currentVariantId);

                        Map<String, String> attributes = new HashMap<>();
                        try (PreparedStatement attrPs = con.prepareStatement(attributeQuery)) {
                            attrPs.setInt(1, currentVariantId);
                            ResultSet attrRs = attrPs.executeQuery();
                            while (attrRs.next()) {
                                attributes.put(attrRs.getString("attribute_key"), attrRs.getString("attribute_value"));
                            }
                        }
                        variant.setAttributes(attributes);

                        List<FlowEntry> flowList = new ArrayList<>();
                        try (PreparedStatement flowPs = con.prepareStatement(flowQuery)) {
                            flowPs.setInt(1, currentVariantId);
                            flowPs.setInt(2, currentVariantId);
                            ResultSet flowRs = flowPs.executeQuery();
                            while (flowRs.next()) {
                                FlowEntry flow = new FlowEntry();
                                flow.setType(flowRs.getString("type"));
                                flow.setDate(flowRs.getTimestamp("date_time").toLocalDateTime());
                                flow.setQuantity(flowRs.getInt("quantity"));
                                flow.setPrice(flowRs.getDouble("price"));
                                if ("PURCHASE".equals(flow.getType())) {
                                    flow.setPurchaseId(flowRs.getInt("ref_id"));
                                } else {
                                    flow.setSalesId(flowRs.getInt("ref_id"));
                                }
                                flowList.add(flow);
                            }
                        }
                        variant.setFlow(flowList);
                        variantList.add(variant);
                    }
                }
                product.setVariants(variantList);
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product ");
        }
        return null;
    }

    public ProductFlowResponse getProductFlowByProductIdAndVariantId(int shopId, int branchId, int productId, int variantId) {

        String productQuery = """
                SELECT p.product_id, p.product_name
                FROM products p
                JOIN branches b ON p.branch_id = b.branch_id
                WHERE b.shop_id = ?
                AND b.branch_id = ?
                AND p.product_id = ?
                AND p.is_deleted = FALSE
                """;

        String variantQuery = """
                SELECT v.variant_id
                FROM variants v
                JOIN products p ON v.product_id = p.product_id
                WHERE p.product_id = ?
                AND v.variant_id = ?
                AND v.is_deleted = FALSE
                """;

        String attributeQuery = "SELECT a.attribute_key, va.attribute_value FROM variant_attribute va JOIN attribute a ON va.attribute_id = a.attribute_id WHERE va.variant_id = ?";

        String flowQuery = """
                SELECT 'PURCHASE' AS type, pb.date_time, pb.purchase_id AS ref_id, pi.quantity, pi.cost_price AS price
                FROM purchase_item pi JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
                WHERE pi.variant_id = ? AND pb.status = 'ACTIVE'
                UNION ALL
                SELECT 'SALE' AS type, sb.date_time, sb.sales_id AS ref_id, si.quantity, si.selling_price AS price
                FROM sales_item si JOIN sales_bill sb ON si.sales_id = sb.sales_id
                WHERE si.variant_id = ? AND sb.status = 'ACTIVE'
                ORDER BY date_time
                """;

        try (Connection con = DBController.getConnection();
             PreparedStatement productPs = con.prepareStatement(productQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);
            productPs.setInt(3, productId);

            ResultSet productRs = productPs.executeQuery();

            if (productRs.next()) {
                ProductFlowResponse product = new ProductFlowResponse();
                product.setProductId(productRs.getInt("product_id"));
                product.setProductName(productRs.getString("product_name"));

                List<VariantFlowResponse> variantList = new ArrayList<>();

                try (PreparedStatement variantPs = con.prepareStatement(variantQuery)) {
                    variantPs.setInt(1, product.getProductId());
                    variantPs.setInt(2, variantId); // Set the specific variant ID

                    ResultSet variantRs = variantPs.executeQuery();

                    if (variantRs.next()) {
                        VariantFlowResponse variant = new VariantFlowResponse();
                        int currentVariantId = variantRs.getInt("variant_id");
                        variant.setVariantId(currentVariantId);

                        Map<String, String> attributes = new HashMap<>();
                        try (PreparedStatement attrPs = con.prepareStatement(attributeQuery)) {
                            attrPs.setInt(1, currentVariantId);
                            ResultSet attrRs = attrPs.executeQuery();
                            while (attrRs.next()) {
                                attributes.put(attrRs.getString("attribute_key"), attrRs.getString("attribute_value"));
                            }
                        }
                        variant.setAttributes(attributes);

                        List<FlowEntry> flowList = new ArrayList<>();
                        try (PreparedStatement flowPs = con.prepareStatement(flowQuery)) {
                            flowPs.setInt(1, currentVariantId);
                            flowPs.setInt(2, currentVariantId);
                            ResultSet flowRs = flowPs.executeQuery();
                            while (flowRs.next()) {
                                FlowEntry flow = new FlowEntry();
                                flow.setType(flowRs.getString("type"));
                                flow.setDate(flowRs.getTimestamp("date_time").toLocalDateTime());
                                flow.setQuantity(flowRs.getInt("quantity"));
                                flow.setPrice(flowRs.getDouble("price"));
                                if ("PURCHASE".equals(flow.getType())) {
                                    flow.setPurchaseId(flowRs.getInt("ref_id"));
                                } else {
                                    flow.setSalesId(flowRs.getInt("ref_id"));
                                }
                                flowList.add(flow);
                            }
                        }
                        variant.setFlow(flowList);
                        variantList.add(variant);
                    }
                }
                product.setVariants(variantList);
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product and variant ");
        }
        return null;
    }

    public List<LowStockProductResponse> getLowStockProducts(int shopId, int branchId, int threshold) {

        String lowStockQuery = """
                SELECT
                    p.product_id,
                    p.product_name,
                    v.variant_id,
                    i.quantity
                FROM inventory i
                JOIN variants v ON i.variant_id = v.variant_id
                JOIN products p ON v.product_id = p.product_id
                JOIN branches b ON i.branch_id = b.branch_id
                WHERE b.shop_id = ?
                  AND i.branch_id = ?
                  AND i.quantity <= ?
                  AND p.is_deleted = FALSE
                  AND v.is_deleted = FALSE
                ORDER BY p.product_id
                """;

        String attributeQuery = """
                SELECT a.attribute_key, va.attribute_value
                FROM variant_attribute va
                JOIN attribute a ON va.attribute_id = a.attribute_id
                WHERE va.variant_id = ?
                """;

        List<LowStockProductResponse> productList = new ArrayList<>();

        try (Connection con = DBController.getConnection();
             PreparedStatement ps = con.prepareStatement(lowStockQuery)) {
            ps.setInt(1, shopId);
            ps.setInt(2, branchId);
            ps.setInt(3, threshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("product_id");

                LowStockProductResponse currentProduct = new LowStockProductResponse();
                currentProduct.setProductId(productId);
                currentProduct.setProductName(rs.getString("product_name"));
                currentProduct.setVariants(new ArrayList<>());
                productList.add(currentProduct);

                LowStockVariantResponse variant = new LowStockVariantResponse();
                int variantId = rs.getInt("variant_id");
                variant.setVariantId(variantId);
                variant.setQuantity(rs.getInt("quantity"));

                Map<String, String> attributes = new HashMap<>();
                try (PreparedStatement attrPs = con.prepareStatement(attributeQuery)) {
                    attrPs.setInt(1, variantId);
                    ResultSet attrRs = attrPs.executeQuery();
                    while (attrRs.next()) {
                        attributes.put(
                                attrRs.getString("attribute_key"),
                                attrRs.getString("attribute_value")
                        );
                    }
                }
                variant.setAttributes(attributes);
                currentProduct.getVariants().add(variant);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching low stock products", e);
        }
        return productList;
    }

    public TopProductResponse getTopSellingProduct(int shopId, int branchId) {

        String topProductQuery = """
                SELECT p.product_id, p.product_name
                FROM sales_item si
                JOIN sales_bill sb ON si.sales_id = sb.sales_id
                JOIN variants v ON si.variant_id = v.variant_id
                JOIN products p ON v.product_id = p.product_id
                JOIN branches b ON sb.branch_id = b.branch_id
                WHERE b.shop_id = ?
                  AND b.branch_id = ?
                  AND sb.status = 'ACTIVE'
                  AND p.is_deleted = FALSE
                  AND v.is_deleted = FALSE
                GROUP BY p.product_id, p.product_name
                ORDER BY SUM(si.quantity) DESC
                LIMIT 1
                """;

        String variantSalesQuery = """
            SELECT si.variant_id, SUM(si.quantity) AS sales_count
            FROM sales_item si
            JOIN sales_bill sb ON si.sales_id = sb.sales_id
            JOIN variants v ON si.variant_id = v.variant_id
            WHERE sb.branch_id = ?
              AND v.product_id = ?
              AND sb.status = 'ACTIVE'
            GROUP BY si.variant_id
            """;

        try (Connection con = DBController.getConnection();
             PreparedStatement productPs = con.prepareStatement(topProductQuery)) {

            productPs.setInt(1, shopId);
            productPs.setInt(2, branchId);

            ResultSet productRs = productPs.executeQuery();

            if (productRs.next()) {
                TopProductResponse product = new TopProductResponse();
                product.setProductId(productRs.getInt("product_id"));
                product.setProductName(productRs.getString("product_name"));

                List<TopVariantSales> variantList = new ArrayList<>();

                try (PreparedStatement variantPs = con.prepareStatement(variantSalesQuery)) {
                    variantPs.setInt(1, branchId);
                    variantPs.setInt(2, product.getProductId());

                    ResultSet variantRs = variantPs.executeQuery();

                    while (variantRs.next()) {
                        TopVariantSales variantSales = new TopVariantSales();
                        variantSales.setVariantId(variantRs.getInt("variant_id"));
                        variantSales.setSalesCount(variantRs.getInt("sales_count"));
                        variantList.add(variantSales);
                    }
                }

                product.setVariants(variantList);
                return product;
            }

        } catch (SQLException e) {
            throw new RuntimeException("error fetching top selling product");
        }

        return null;
    }
}