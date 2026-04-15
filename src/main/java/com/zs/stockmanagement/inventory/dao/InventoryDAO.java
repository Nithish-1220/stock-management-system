package com.zs.stockmanagement.inventory.dao;

import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.inventory.model.Stock;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public List<Stock> getStocks(int shopId, int branchId) {

        String stockQuery = """
                select i.variant_id,i.quantity from inventory i join branches b on b.branch_id = i.branch_id where b.shop_id = ? AND i.branch_id = ?
                """;

        try (Connection connection = DBController.getConnection();
             PreparedStatement stockPs = connection.prepareStatement(stockQuery)) {
            stockPs.setInt(1, shopId);
            stockPs.setInt(2, branchId);
            try (ResultSet stockRs = stockPs.executeQuery()) {
                List<Stock> inventory = new ArrayList<>();
                while (stockRs.next()) {
                    Stock stock = new Stock();
                    stock.setVariantId(stockRs.getInt("variant_id"));
                    stock.setQuantity(stockRs.getInt("quantity"));
                    inventory.add(stock);
                }
                return inventory;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Stock getStocks(int shopId, int branchId, int productId, int variantId) {
        String stockQuery = """
                select i.variant_id,i.quantity
                                from inventory i 
                                join branches b on b.branch_id = i.branch_id 
                                join variants v on i.variant_id = v.variant_id 
                                where b.shop_id = ? 
                                AND i.branch_id = ?  
                                and v.product_id = ? 
                                And i.variant_id=?;
                """;

        try (Connection connection = DBController.getConnection();
             PreparedStatement stockPs = connection.prepareStatement(stockQuery)) {
            stockPs.setInt(1, shopId);
            stockPs.setInt(2, branchId);
            stockPs.setInt(3, productId);
            stockPs.setInt(4, variantId);
            try (ResultSet stockRs = stockPs.executeQuery()) {
                if (stockRs.next()) {
                    Stock stock = new Stock();
                    stock.setVariantId(stockRs.getInt("variant_id"));
                    stock.setQuantity(stockRs.getInt("quantity"));
                    return stock;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
}