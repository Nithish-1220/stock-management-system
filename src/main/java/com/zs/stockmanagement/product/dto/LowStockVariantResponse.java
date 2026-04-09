package com.zs.stockmanagement.product.dto;

import java.util.Map;

public class LowStockVariantResponse {
    private int variantId;
    private int quantity;
    private Map<String, String> attributes;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}