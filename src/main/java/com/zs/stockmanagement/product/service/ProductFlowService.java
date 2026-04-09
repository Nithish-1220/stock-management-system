package com.zs.stockmanagement.product.service;

import com.zs.stockmanagement.product.dao.ProductFlowDAO;
import com.zs.stockmanagement.product.dto.LowStockProductResponse;
import com.zs.stockmanagement.product.dto.ProductFlowResponse;
import com.zs.stockmanagement.product.dto.TopProductResponse;

import java.util.List;

public class ProductFlowService {

    private final ProductFlowDAO dao = new ProductFlowDAO();

    public List<ProductFlowResponse> getProductFlow(int shopId, int branchId) {
        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId");
        }
        return dao.getProductFlow(shopId, branchId);
    }

    public ProductFlowResponse getProductFlowByProductId(int shopId, int branchId, int productId) {
        if (shopId <= 0 || branchId <= 0 || productId <= 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId/ productId");
        }
        return dao.getProductFlowById(shopId, branchId, productId);
    }

    public ProductFlowResponse getProductFlowByProductAndVariantId(int shopId, int branchId, int productId, int variantId) {
        if (shopId <= 0 || branchId <= 0 || productId <= 0 || variantId <= 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId/ productId / variantId");
        }
        return dao.getProductFlowByProductIdAndVariantId(shopId, branchId, productId, variantId);
    }


    public List<LowStockProductResponse> getLowStockProducts(int shopId, int branchId, int threshold) {
        if (shopId <= 0 || branchId <= 0 || threshold < 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId/ productId / variantId");
        }
        return dao.getLowStockProducts(shopId, branchId, threshold);
    }

    public TopProductResponse getTopSellingProduct(int shopId, int branchId) {
        if (shopId <= 0 || branchId <= 0) {
            throw new IllegalArgumentException("Invalid shopId / branchId/ productId / variantId");
        }
        return dao.getTopSellingProduct(shopId, branchId);
    }

}