package com.zs.stockmanagement.reports.sales.dto;

import java.util.List;

public class SalesProductDetail {
    private int productId;
    private String productName;
    private List<SalesVariantDetail> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public List<SalesVariantDetail> getVariants() { return variants; }
    public void setVariants(List<SalesVariantDetail> variants) { this.variants = variants; }
}