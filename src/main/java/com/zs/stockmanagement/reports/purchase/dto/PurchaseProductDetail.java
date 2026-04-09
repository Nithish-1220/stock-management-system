package com.zs.stockmanagement.reports.purchase.dto;

import java.util.List;

public class PurchaseProductDetail {
    private int productId;
    private String productName;
    private List<PurchaseVariantDetail> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public List<PurchaseVariantDetail> getVariants() { return variants; }
    public void setVariants(List<PurchaseVariantDetail> variants) { this.variants = variants; }
}