package com.zs.stockmanagement.product.dto;

import java.util.List;

public class LowStockProductResponse {
    private int productId;
    private String productName;
    private List<LowStockVariantResponse> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public List<LowStockVariantResponse> getVariants() { return variants; }
    public void setVariants(List<LowStockVariantResponse> variants) { this.variants = variants; }
}