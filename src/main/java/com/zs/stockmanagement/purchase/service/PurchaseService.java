package com.zs.stockmanagement.purchase.service;

import com.zs.stockmanagement.purchase.dao.PurchaseDAO;
import com.zs.stockmanagement.purchase.dto.RequestPurchase;
import com.zs.stockmanagement.purchase.dto.RequestPurchaseItem;
import com.zs.stockmanagement.purchase.model.Purchase;
import com.zs.stockmanagement.purchase.model.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class PurchaseService {
    PurchaseDAO purchaseDAO = new PurchaseDAO();

    public List<Purchase> getPurchases(int shopId, int branchId) {
        return purchaseDAO.getPurchases(shopId, branchId);
    }

    public Purchase getPurchases(int shopId, int branchId, int purchaseId) {
        return purchaseDAO.getPurchases(shopId, branchId, purchaseId);
    }

    public Purchase addPurchase(int shopId, int branchId, RequestPurchase requestPurchase) {
        List<PurchaseItem> items = new ArrayList<>();
        for (RequestPurchaseItem item : requestPurchase.getRequestPurchaseItems()) {
            PurchaseItem purchaseItem = new PurchaseItem(item.getProductId(),item.getProductName(),item.getVariantId(), item.getQuantity(), item.getCostPrice(), item.getTotalAmount());
            items.add(purchaseItem);
        }
        Purchase purchase = new Purchase(items, requestPurchase.getTotalAmount(), requestPurchase.getVendorId());
        Purchase addedPurchase = purchaseDAO.addPurchase(shopId, branchId, purchase);
        if (addedPurchase == null) {
            throw new RuntimeException("added failed");
        }
        return addedPurchase;
    }

    public boolean deletePurchase(int shopId, int branchId, int purchaseId) {
        System.out.println("delete purchase in service");
        return purchaseDAO.deletePurchase(shopId, branchId, purchaseId);
    }

    public Purchase updatePurchase(int shopId, int branchId, int purchaseID, RequestPurchase requestPurchase) {
        List<PurchaseItem> items = new ArrayList<>();
        for (RequestPurchaseItem item : requestPurchase.getRequestPurchaseItems()) {
            PurchaseItem purchaseItem = new PurchaseItem(item.getProductId(),item.getProductName(),item.getVariantId(), item.getQuantity(), item.getCostPrice(), item.getTotalAmount());
            items.add(purchaseItem);
        }
        Purchase purchase = new Purchase(items, requestPurchase.getTotalAmount(), requestPurchase.getVendorId());
        return purchaseDAO.updatePurchase(shopId, branchId, purchaseID, purchase);
    }
}
