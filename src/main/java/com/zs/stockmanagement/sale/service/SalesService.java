package com.zs.stockmanagement.sale.service;

import com.zs.stockmanagement.sale.dao.SalesDAO;
import com.zs.stockmanagement.sale.dto.AddRequestSale;
import com.zs.stockmanagement.sale.dto.AddRequestSaleItem;
import com.zs.stockmanagement.sale.model.Sale;
import com.zs.stockmanagement.sale.model.SaleItem;

import java.util.ArrayList;
import java.util.List;

public class SalesService {
    SalesDAO salesDAO = new SalesDAO();

    public List<Sale> getSales(int shopId, int branchId) {
        return salesDAO.getSales(shopId, branchId);
    }

    public Sale getSales(int shopId, int branchId, int salesId) {
        return salesDAO.getSales(shopId, branchId, salesId);
    }

    public Sale addSale(int shopId, int branchId, AddRequestSale requestSale) {
        List<SaleItem> items = new ArrayList<>();
        for (AddRequestSaleItem item : requestSale.getSaleItems()) {
            SaleItem i = new SaleItem(item.getVariantId(), item.getSellingPrice(), item.getQuantity(), item.getTotalAmount());
            items.add(i);
        }
        Sale sale = new Sale(requestSale.getCustomerId(), items, requestSale.getTotalAmount());
        Sale addedSale = salesDAO.addSale(shopId, branchId, sale);
        System.out.println(addedSale);
        if (addedSale == null) {
            throw new RuntimeException("addedSale is null");
        }
        return addedSale;
    }

    public boolean deleteSale(int shopId, int branchId, int saleId) {

        boolean deleted = salesDAO.deleteSale(shopId, branchId, saleId);
        if (!deleted) {
            throw new RuntimeException("Sale delete failed");
        }
        return true;

    }

    public boolean updateSale(int shopId, int branchId, int saleId, AddRequestSale requestSale) {

        if (requestSale == null) {
            return false;
        }
        List<SaleItem> items = new ArrayList<>();
        for (AddRequestSaleItem item : requestSale.getSaleItems()) {
            SaleItem saleItem = new SaleItem(
                    item.getVariantId(),
                    item.getSellingPrice(),
                    item.getQuantity(),
                    item.getTotalAmount()
            );
            items.add(saleItem);
        }
        Sale sale = new Sale(
                requestSale.getCustomerId(),
                items,
                requestSale.getTotalAmount()
        );
        sale.setSaleId(saleId);
        boolean updated = salesDAO.updateSale(shopId, branchId, sale);
        if (!updated) {
            throw new RuntimeException("Sale update failed");
        }
        return true;

    }


}
