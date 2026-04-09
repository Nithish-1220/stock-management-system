package com.zs.stockmanagement.reports.profit.dto;

import java.util.List;

public class ProfitProductDetail {
    private int productId;
    private String productName;
    private List<ProfitVariantDetail> variants;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public List<ProfitVariantDetail> getVariants() { return variants; }
    public void setVariants(List<ProfitVariantDetail> variants) { this.variants = variants; }
}