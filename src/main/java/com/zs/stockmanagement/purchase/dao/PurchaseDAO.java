package com.zs.stockmanagement.purchase.dao;

import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.purchase.model.Purchase;
import com.zs.stockmanagement.purchase.model.PurchaseItem;
import com.zs.stockmanagement.utils.DBController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    public List<Purchase> getPurchases(int shopId, int branchId) {

        String purchaseQuery = """
                select purchase_id, date_time, vendor_id, total_amount from purchase_bill pb
                                join branches b on pb.branch_id = b.branch_id
                                where b.shop_id = ? And b.branch_id = ? AND status = 'Active';
                """;

        String purchaseItem = """
                select p.product_id,p.product_name,pi.variant_id, pi.quantity, pi.cost_price, pi.total_amount from purchase_item pi join variants v on pi.variant_id = v.variant_id join products p on v.product_id = p.product_id where purchase_id=?;
                """;

        try (Connection connection = DBController.getConnection();
             PreparedStatement purchasePs = connection.prepareStatement(purchaseQuery)) {

            purchasePs.setInt(1, shopId);
            purchasePs.setInt(2, branchId);

            try (ResultSet purchaseRs = purchasePs.executeQuery()) {

                List<Purchase> purchases = new ArrayList<>();
                while (purchaseRs.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setPurchaseId(purchaseRs.getInt("purchase_id"));
                    purchase.setDataTime(purchaseRs.getTimestamp("date_time").toLocalDateTime());
                    purchase.setVendorId(purchaseRs.getInt("vendor_id"));
                    purchase.setTotalAmount(purchaseRs.getDouble("total_amount"));

                    try (PreparedStatement purchaseItemPs = connection.prepareStatement(purchaseItem)) {

                        purchaseItemPs.setInt(1, purchase.getPurchaseId());

                        try (ResultSet purchaseItemRs = purchaseItemPs.executeQuery()) {

                            while (purchaseItemRs.next()) {
                                PurchaseItem item = new PurchaseItem();
                                item.setProductId(purchaseItemRs.getInt("product_id"));
                                item.setProductName(purchaseItemRs.getString("product_name"));
                                item.setVariantId(purchaseItemRs.getInt("variant_id"));
                                item.setQuantity(purchaseItemRs.getInt("quantity"));
                                item.setCostPrice(purchaseItemRs.getDouble("cost_price"));
                                item.setTotalAmount(purchaseItemRs.getDouble("total_amount"));
                                purchase.getPurchaseItems().add(item);
                            }

                        }

                    }

                    purchases.add(purchase);
                }

                System.out.println(purchases);
                return purchases;
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

    }

    public Purchase getPurchases(int shopId, int branchId, int purchaseId) {

        String purchaseQuery = """
                select purchase_id, date_time, vendor_id, total_amount from purchase_bill pb
                join branches b on pb.branch_id = b.branch_id
                where b.shop_id = ? And b.branch_id = ? AND purchase_id =? AND status = 'active';
                """;

        String purchaseItem =
                """
                        select p.product_id,p.product_name,pi.variant_id, pi.quantity, pi.cost_price, pi.total_amount from purchase_item pi join variants v on pi.variant_id = v.variant_id join products p on v.product_id = p.product_id where purchase_id=?;
                        """;

        try (Connection connection = DBController.getConnection();
             PreparedStatement purchasePs = connection.prepareStatement(purchaseQuery)) {

            purchasePs.setInt(1, shopId);
            purchasePs.setInt(2, branchId);
            purchasePs.setInt(3, purchaseId);

            try (ResultSet purchaseRs = purchasePs.executeQuery()) {

                Purchase purchase = new Purchase();
                if (purchaseRs.next()) {
                    purchase.setPurchaseId(purchaseRs.getInt("purchase_id"));
                    purchase.setDataTime(purchaseRs.getTimestamp("date_time").toLocalDateTime());
                    purchase.setVendorId(purchaseRs.getInt("vendor_id"));
                    purchase.setTotalAmount(purchaseRs.getDouble("total_amount"));

                    try (PreparedStatement purchaseItemPs = connection.prepareStatement(purchaseItem)) {

                        purchaseItemPs.setInt(1, purchase.getPurchaseId());

                        try (ResultSet purchaseItemRs = purchaseItemPs.executeQuery()) {

                            while (purchaseItemRs.next()) {
                                PurchaseItem item = new PurchaseItem();
                                item.setProductId(purchaseItemRs.getInt("product_id"));
                                item.setProductName(purchaseItemRs.getString("product_name"));
                                item.setVariantId(purchaseItemRs.getInt("variant_id"));
                                item.setQuantity(purchaseItemRs.getInt("quantity"));
                                item.setCostPrice(purchaseItemRs.getDouble("cost_price"));
                                item.setTotalAmount(purchaseItemRs.getDouble("total_amount"));
                                purchase.getPurchaseItems().add(item);
                            }

                        }

                    }

                }

                return purchase;

            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

    }

    public List<Purchase> addPurchases() {
        return null;
    }

    public Purchase addPurchase(int shopId, int branchId, Purchase purchase) {

        String insertPurchase = """
                INSERT INTO purchase_bill(branch_id, vendor_id, total_amount) VALUES (?,?,?)
                """;
        String insertItem = """
                INSERT INTO purchase_item(purchase_id, variant_id, quantity, cost_price, total_amount) VALUES (?,?,?,?,?)
                """;

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int purchaseId;

                try (PreparedStatement purchaseBillPs = connection.prepareStatement(insertPurchase, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    purchaseBillPs.setInt(1, branchId);
                    purchaseBillPs.setInt(2, purchase.getVendorId());
                    purchaseBillPs.setDouble(3, purchase.getTotalAmount());
                    purchaseBillPs.executeUpdate();

                    try (ResultSet purchaseBillRs = purchaseBillPs.getGeneratedKeys()) {

                        if (purchaseBillRs.next()) {
                            purchaseId = purchaseBillRs.getInt(1);
                        } else {
                            throw new SQLException("Failed to generate purchase ID.");
                        }

                    }

                }

                try (PreparedStatement purchaseItemPs = connection.prepareStatement(insertItem)) {

                    for (PurchaseItem item : purchase.getPurchaseItems()) {
                        purchaseItemPs.setInt(1, purchaseId);
                        purchaseItemPs.setInt(2, item.getVariantId());
                        purchaseItemPs.setInt(3, item.getQuantity());
                        purchaseItemPs.setDouble(4, item.getCostPrice());
                        purchaseItemPs.setDouble(5, item.getTotalAmount());
                        purchaseItemPs.addBatch();
                    }
                    purchaseItemPs.executeBatch();

                    String updateInventory = """
                                INSERT INTO inventory (branch_id, variant_id, quantity)
                                VALUES (?, ?, ?)
                                ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)
                            """;

                    try (PreparedStatement inventoryPs = connection.prepareStatement(updateInventory)) {

                        for (PurchaseItem item : purchase.getPurchaseItems()) {
                            inventoryPs.setInt(1, branchId);
                            inventoryPs.setInt(2, item.getVariantId());
                            inventoryPs.setInt(3, item.getQuantity());
                            inventoryPs.addBatch();
                        }
                        inventoryPs.executeBatch();

                    }

                }

                connection.commit();
                return getPurchases(shopId, branchId, purchaseId);

            } catch (SQLException e) {
                connection.rollback();
                throw new DataBaseException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }

    }

    public boolean deletePurchase(int shopId, int branchId, int purchaseId) {

        String branchCheckQuery = """
                SELECT branch_id FROM branches
                    WHERE branch_id = ? AND shop_id = ?""";

        String stockCheckQuery =
                """
                        SELECT pi.variant_id, i.quantity AS current_stock, pi.quantity AS purchased_quantity
                        FROM purchase_item pi
                        JOIN purchase_bill pb ON pi.purchase_id = pb.purchase_id
                        JOIN inventory i ON i.variant_id = pi.variant_id AND i.branch_id = pb.branch_id
                        WHERE pb.purchase_id = ?
                        AND pb.branch_id = ?
                        AND pb.status = 'ACTIVE'
                        """;

        String reverseStockQuery =
                "UPDATE inventory i " +
                        "JOIN purchase_item pi ON i.variant_id = pi.variant_id " +
                        "SET i.quantity = i.quantity - pi.quantity " +
                        "WHERE pi.purchase_id = ? " +
                        "AND i.branch_id = ?";

        String cancelPurchaseQuery =
                "UPDATE purchase_bill " +
                        "SET status = 'CANCELLED' " +
                        "WHERE purchase_id = ? " +
                        "AND branch_id = ? " +
                        "AND status = 'ACTIVE'";

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

                boolean hasItems = false;
                try (PreparedStatement stockPs = connection.prepareStatement(stockCheckQuery)) {
                    stockPs.setInt(1, purchaseId);
                    stockPs.setInt(2, branchId);
                    try (ResultSet stockRs = stockPs.executeQuery()) {
                        while (stockRs.next()) {
                            hasItems = true;
                            int currentStock = stockRs.getInt("current_stock");
                            int purchasedQuantity = stockRs.getInt("purchased_quantity");
                            System.out.println(currentStock + " " + purchasedQuantity);
                            if (currentStock < purchasedQuantity) {
                                connection.rollback();
                                return false;
                            }
                        }
                    }
                }

                if (!hasItems) {
                    connection.rollback();
                    return false;
                }

                try (PreparedStatement reversePs = connection.prepareStatement(reverseStockQuery)) {

                    reversePs.setInt(1, purchaseId);
                    reversePs.setInt(2, branchId);
                    reversePs.executeUpdate();

                }

                try (PreparedStatement cancelPs = connection.prepareStatement(cancelPurchaseQuery)) {

                    cancelPs.setInt(1, purchaseId);
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
                throw new DataBaseException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }

    }

    //FIXME
    public Purchase updatePurchase(int shopId, int branchId, int purchaseId, Purchase purchase) {
        String checkQuery = """
                select pb.purchase_id from purchase_bill pb
                                join branches b on pb.branch_id = b.branch_id
                                where pb.purchase_id = ?
                                AND pb.branch_id = ?
                                AND b.shop_id =?
                                AND pb.status='ACTIVE';
                """;

        String purchaseBillUpdateQuery = """
                update purchase_bill set date_time = ?,vendor_id=?,total_amount=?
                where purchase_id = ?
                AND branch_id =?
                And status='ACTIVE';
                """;

        String purchaseItemUpdateQuery = """
                update purchase_item
                                set quantity = ? ,
                                cost_price =? ,
                                total_amount=?
                                where  purchase_id =?
                                And variant_id=?;
                """;

        String getOldQuantityQuery = """
                select quantity from purchase_item
                where purchase_id = ? AND variant_id = ?
                """;

        String increaseStock = """
                update inventory set quantity = quantity + ?
                                where variant_id = ? AND branch_id = ?
                """;

        String decreaseStock = """
                update inventory set quantity = quantity - ?
                                where variant_id = ? AND branch_id = ? AND quantity >= ?
                """;

        try (Connection connection = DBController.getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, purchaseId);
                    checkPs.setInt(2, branchId);
                    checkPs.setInt(3, shopId);
                    try (ResultSet checkRs = checkPs.executeQuery()) {
                        if (!checkRs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                try (PreparedStatement purchaseBillUpdatePs = connection.prepareStatement(purchaseBillUpdateQuery)) {
                    purchaseBillUpdatePs.setTimestamp(1, Timestamp.valueOf(purchase.getDataTime()));
                    purchaseBillUpdatePs.setInt(2, purchase.getVendorId());
                    purchaseBillUpdatePs.setDouble(3, purchase.getTotalAmount());
                    purchaseBillUpdatePs.setInt(4, purchaseId);
                    purchaseBillUpdatePs.setInt(5, branchId);
                    int rowsAffected = purchaseBillUpdatePs.executeUpdate();
                    if (rowsAffected == 0) {
                        connection.rollback();
                        return null;
                    }
                }

                try (PreparedStatement purchaseItemPs = connection.prepareStatement(purchaseItemUpdateQuery);
                     PreparedStatement getOldQuantityPs = connection.prepareStatement(getOldQuantityQuery);
                     PreparedStatement increasePs = connection.prepareStatement(increaseStock);
                     PreparedStatement decreasePs = connection.prepareStatement(decreaseStock)) {

                    for (PurchaseItem item : purchase.getPurchaseItems()) {
                        getOldQuantityPs.setInt(1, purchaseId);
                        getOldQuantityPs.setInt(2, item.getVariantId());

                        int oldQuantity;
                        try (ResultSet getOldQuantityRs = getOldQuantityPs.executeQuery()) {
                            if (getOldQuantityRs.next()) {
                                oldQuantity = getOldQuantityRs.getInt("quantity");
                            } else {
                                throw new SQLException("old quantity is none");
                            }
                        }

                        int difference = item.getQuantity() - oldQuantity;

                        purchaseItemPs.setInt(1, item.getQuantity());
                        purchaseItemPs.setDouble(2, item.getCostPrice());
                        purchaseItemPs.setDouble(3, item.getTotalAmount());
                        purchaseItemPs.setInt(4, purchaseId);
                        purchaseItemPs.setInt(5, item.getVariantId());
                        int rowsAffected2 = purchaseItemPs.executeUpdate();
                        if (rowsAffected2 == 0) {
                            connection.rollback();
                            return null;
                        }

                        if (difference > 0) {
                            increasePs.setInt(1, difference);
                            increasePs.setInt(2, item.getVariantId());
                            increasePs.setInt(3, branchId);
                            int affected = increasePs.executeUpdate();
                            if (affected == 0) {
                                connection.rollback();
                                return null;
                            }
                        } else if (difference < 0) {
                            int absDiff = Math.abs(difference);
                            decreasePs.setInt(1, absDiff);
                            decreasePs.setInt(2, item.getVariantId());
                            decreasePs.setInt(3, branchId);
                            decreasePs.setInt(4, absDiff);
                            int affected = decreasePs.executeUpdate();
                            if (affected == 0) {
                                connection.rollback();
                                return null;
                            }
                        }
                    }
                }
                connection.commit();
                return purchase;

            } catch (Exception e) {
                connection.rollback();
                throw new DataBaseException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
}