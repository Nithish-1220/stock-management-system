package com.zs.stockmanagement.product.dto;

public class TopVariantSales {
    private int variantId;
    private int salesCount;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    public int getSalesCount() { return salesCount; }
    public void setSalesCount(int salesCount) { this.salesCount = salesCount; }
}