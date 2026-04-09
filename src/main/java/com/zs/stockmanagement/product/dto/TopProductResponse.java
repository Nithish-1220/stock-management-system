package com.zs.stockmanagement.product.dto;

import java.util.List;

public class TopProductResponse {
    private int productId;
    private String productName;
    private List<TopVariantSales> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public List<TopVariantSales> getVariants() { return variants; }
    public void setVariants(List<TopVariantSales> variants) { this.variants = variants; }
}