package com.zs.stockmanagement.sale.dao;

import com.zs.stockmanagement.sale.model.Sale;
import com.zs.stockmanagement.sale.model.SaleItem;
import com.zs.stockmanagement.utils.DBController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    public List<Sale> getSales(int shopId, int branchId) {
        String salesQuery = "select sales_id, date_time, customer_id,total_amount from sales_bill sb  " +
                "join branches b on sb.branch_id = b.branch_id " +
                "where b.shop_id = ? And b.branch_id = ? ;";

        String salesItem = "select variant_id, quantity, selling_price, total_amount from sales_item where sales_id=?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement salesPs = connection.prepareStatement(salesQuery)) {

            salesPs.setInt(1, shopId);
            salesPs.setInt(2, branchId);

            try (ResultSet salesRs = salesPs.executeQuery()) {
                List<Sale> sales = new ArrayList<>();

                while (salesRs.next()) {
                    Sale sale = new Sale();
                    sale.setSaleId(salesRs.getInt("sales_id"));
                    sale.setDateTime(salesRs.getTimestamp("date_time").toLocalDateTime());
                    sale.setCustomerId(salesRs.getInt("customer_id"));
                    sale.setTotalAmount(salesRs.getDouble("total_amount"));

                    try (PreparedStatement saleItemPs = connection.prepareStatement(salesItem)) {
                        saleItemPs.setInt(1, sale.getSaleId());
                        try (ResultSet salesItemRs = saleItemPs.executeQuery()) {
                            while (salesItemRs.next()) {
                                SaleItem item = new SaleItem();
                                item.setVariantId(salesItemRs.getInt("variant_id"));
                                item.setQuantity(salesItemRs.getInt("quantity"));
                                item.setSellingPrice(salesItemRs.getDouble("selling_price"));
                                item.setTotalAmount(salesItemRs.getDouble("total_amount"));
                                sale.getSaleItems().add(item);
                            }
                        }
                    }
                    sales.add(sale);
                }
                return sales;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Sale getSales(int shopId, int branchId, int salesId) {
        String salesQuery = "select sales_id, date_time, customer_id,total_amount from sales_bill sb  " +
                "join branches b on sb.branch_id = b.branch_id " +
                "where b.shop_id = ? And b.branch_id = ? AND sales_id = ? ;";

        String salesItem = "select variant_id, quantity, selling_price, total_amount from sales_item where sales_id=?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement salesPs = connection.prepareStatement(salesQuery)) {

            salesPs.setInt(1, shopId);
            salesPs.setInt(2, branchId);
            salesPs.setInt(3, salesId);

            try (ResultSet salesRs = salesPs.executeQuery()) {
                Sale sale = new Sale();
                if (salesRs.next()) {
                    sale.setSaleId(salesRs.getInt("sales_id"));
                    sale.setDateTime(salesRs.getTimestamp("date_time").toLocalDateTime());
                    sale.setCustomerId(salesRs.getInt("customer_id"));
                    sale.setTotalAmount(salesRs.getDouble("total_amount"));

                    try (PreparedStatement saleItemPs = connection.prepareStatement(salesItem)) {
                        saleItemPs.setInt(1, sale.getSaleId());
                        try (ResultSet salesItemRs = saleItemPs.executeQuery()) {
                            while (salesItemRs.next()) {
                                SaleItem item = new SaleItem();
                                item.setVariantId(salesItemRs.getInt("variant_id"));
                                item.setQuantity(salesItemRs.getInt("quantity"));
                                item.setSellingPrice(salesItemRs.getDouble("selling_price"));
                                item.setTotalAmount(salesItemRs.getDouble("total_amount"));
                                sale.getSaleItems().add(item);
                            }
                        }
                    }
                }
                return sale;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Sale> addSales() {
        return null;
    }

    public Sale addSale(int shopId, int branchId, Sale sale) {

        String branchCheckQuery = "SELECT branch_id FROM branches WHERE branch_id = ? AND shop_id = ?";
        String decreaseStockQuery = "UPDATE inventory SET quantity = quantity - ? WHERE variant_id = ? AND branch_id = ? AND quantity >= ?";
        String insertSale = "INSERT INTO sales_bill(branch_id, customer_id, total_amount) VALUES (?,?,?)";
        String insertItem = "INSERT INTO sales_item (sales_id, variant_id, quantity, selling_price, total_amount) VALUES (?,?,?,?,?)";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement branchPs = connection.prepareStatement(branchCheckQuery)) {
                    branchPs.setInt(1, branchId);
                    branchPs.setInt(2, shopId);
                    try (ResultSet branchRs = branchPs.executeQuery()) {
                        if (!branchRs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                try (PreparedStatement stockPs = connection.prepareStatement(decreaseStockQuery)) {
                    for (SaleItem item : sale.getSaleItems()) {
                        stockPs.setInt(1, item.getQuantity());
                        stockPs.setInt(2, item.getVariantId());
                        stockPs.setInt(3, branchId);
                        stockPs.setInt(4, item.getQuantity());
                        int affectedRows = stockPs.executeUpdate();
                        if (affectedRows == 0) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                int saleId;
                try (PreparedStatement salesBillPs = connection.prepareStatement(insertSale, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    salesBillPs.setInt(1, branchId);
                    salesBillPs.setInt(2, sale.getCustomerId());
                    salesBillPs.setDouble(3, sale.getTotalAmount());
                    salesBillPs.executeUpdate();

                    try (ResultSet rs = salesBillPs.getGeneratedKeys()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return null;
                        }
                        saleId = rs.getInt(1);
                    }
                }

                try (PreparedStatement salesItemPs = connection.prepareStatement(insertItem)) {
                    for (SaleItem item : sale.getSaleItems()) {
                        salesItemPs.setInt(1, saleId);
                        salesItemPs.setInt(2, item.getVariantId());
                        salesItemPs.setInt(3, item.getQuantity());
                        salesItemPs.setDouble(4, item.getSellingPrice());
                        salesItemPs.setDouble(5, item.getTotalAmount());
                        salesItemPs.addBatch();
                    }
                    salesItemPs.executeBatch();
                }

                connection.commit();
                sale.setSaleId(saleId);
                return sale;

            } catch (Exception e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteSale(int shopId, int branchId, int saleId) {
        String branchCheckQuery = "SELECT branch_id FROM branches WHERE branch_id = ? AND shop_id = ?";
        String reverseStockQuery = "UPDATE inventory i JOIN sales_item si ON i.variant_id = si.variant_id SET i.quantity = i.quantity + si.quantity WHERE si.sales_id = ? AND i.branch_id = ?";
        String cancelPurchaseQuery = "UPDATE sales_bill SET status = 'CANCELLED' WHERE sales_id = ? AND branch_id = ? AND status = 'ACTIVE'";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement branchPs = connection.prepareStatement(branchCheckQuery)) {
                    branchPs.setInt(1, branchId);
                    branchPs.setInt(2, shopId);
                    try (ResultSet branchRs = branchPs.executeQuery()) {
                        if (!branchRs.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement reversePs = connection.prepareStatement(reverseStockQuery)) {
                    reversePs.setInt(1, saleId);
                    reversePs.setInt(2, branchId);
                    reversePs.executeUpdate();
                }

                try (PreparedStatement cancelPs = connection.prepareStatement(cancelPurchaseQuery)) {
                    cancelPs.setInt(1, saleId);
                    cancelPs.setInt(2, branchId);
                    int rowsUpdated = cancelPs.executeUpdate();

                    if (rowsUpdated == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (Exception e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateSale(int shopId, int branchId, Sale sale) {
        String getOldStockQuery = "select quantity from sales_item where sales_id = ? AND  variant_id = ? ";
        String decreaseStock = "UPDATE inventory SET quantity = quantity - ? WHERE variant_id = ? AND branch_id = ? AND quantity >= ?";
        String increaseStock = "UPDATE inventory SET quantity = quantity + ? WHERE variant_id = ? AND branch_id = ?";
        String checkQuery = "select sb.sales_id from sales_bill sb join branches b on sb.branch_id = b.branch_id where sb.branch_id = ? and b.shop_id=? AND sb.status = 'ACTIVE' And sb.sales_id = ?;";
        String updateQuery = "UPDATE sales_bill SET date_time = ?, branch_id = ?, customer_id = ?, total_amount = ?, status = ? WHERE sales_id = ?;";
        String itemUpdateQuery = "UPDATE sales_item SET quantity = ?, selling_price = ?, total_amount = ? WHERE sales_id = ? AND variant_id = ?;";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, branchId);
                    checkPs.setInt(2, shopId);
                    checkPs.setInt(3, sale.getSaleId());
                    try (ResultSet checkRs = checkPs.executeQuery()) {
                        if (!checkRs.next()) {
                            throw new SQLException("checking wrong");
                        }
                    }
                }

                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                    updatePs.setTimestamp(1, Timestamp.valueOf(sale.getDateTime()));
                    updatePs.setInt(2, branchId);
                    updatePs.setInt(3, sale.getCustomerId());
                    updatePs.setDouble(4, sale.getTotalAmount());
                    updatePs.setString(5, sale.getStatus());
                    updatePs.setInt(6, sale.getSaleId());
                    updatePs.executeUpdate();
                }

                try (PreparedStatement itemUpdatePs = connection.prepareStatement(itemUpdateQuery);
                     PreparedStatement oldStockPs = connection.prepareStatement(getOldStockQuery);
                     PreparedStatement decreasePs = connection.prepareStatement(decreaseStock);
                     PreparedStatement increasePs = connection.prepareStatement(increaseStock)) {

                    for (SaleItem saleItem : sale.getSaleItems()) {
                        itemUpdatePs.setInt(1, saleItem.getQuantity());
                        itemUpdatePs.setDouble(2, saleItem.getSellingPrice());
                        itemUpdatePs.setDouble(3, saleItem.getTotalAmount());
                        itemUpdatePs.setInt(4, sale.getSaleId());
                        itemUpdatePs.setInt(5, saleItem.getVariantId());
                        itemUpdatePs.addBatch();

                        oldStockPs.setInt(1, sale.getSaleId());
                        oldStockPs.setInt(2, saleItem.getVariantId());

                        int oldSoldQty;
                        try (ResultSet oldStockRs = oldStockPs.executeQuery()) {
                            if (oldStockRs.next()) {
                                oldSoldQty = oldStockRs.getInt("quantity");
                            } else {
                                throw new SQLException("oldStock is empty");
                            }
                        }

                        int difference = saleItem.getQuantity() - oldSoldQty;

                        if (difference > 0) {
                            decreasePs.setInt(1, difference);
                            decreasePs.setInt(2, saleItem.getVariantId());
                            decreasePs.setInt(3, branchId);
                            decreasePs.setInt(4, difference);
                            int rows = decreasePs.executeUpdate();
                            if (rows == 0) {
                                connection.rollback();
                                return false;
                            }

                        } else if (difference < 0) {
                            increasePs.setInt(1, Math.abs(difference));
                            increasePs.setInt(2, saleItem.getVariantId());
                            increasePs.setInt(3, branchId);
                            increasePs.executeUpdate();
                        }
                    }
                    itemUpdatePs.executeBatch();
                }

                connection.commit();
                return true;

            } catch (Exception e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}