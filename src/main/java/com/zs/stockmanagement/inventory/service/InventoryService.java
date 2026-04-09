package com.zs.stockmanagement.inventory.service;

import com.zs.stockmanagement.inventory.dao.InventoryDAO;
import com.zs.stockmanagement.inventory.model.Stock;

import java.util.List;

public class InventoryService {
    InventoryDAO inventoryDAO = new InventoryDAO();

    public List<Stock> getStocks(int shopId, int branchId){
        List<Stock> inventory = inventoryDAO.getStocks(shopId,branchId);
        if(inventory==null) throw new RuntimeException("failed to fetch the data from inventory");
        return inventory;
    }

    public Stock getStocks(int shopId,int branchId,int productId,int variantId){
        Stock stock = inventoryDAO.getStocks(shopId,branchId,productId,variantId);
        if(stock==null) throw new RuntimeException("failed to fetch the data from inventory");
        return stock;
    }

}
