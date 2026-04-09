package com.zs.stockmanagement.product.dto;

import java.util.List;

public class ProductFlowResponse {

    private int productId;
    private String productName;
    private List<VariantFlowResponse> variants;

    public ProductFlowResponse(int productId, String productName, List<VariantFlowResponse> variants) {
        this.productId = productId;
        this.productName = productName;
        this.variants = variants;
    }

    public ProductFlowResponse() {

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<VariantFlowResponse> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantFlowResponse> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "ProductFlowResponse{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", variants=" + variants +
                '}';
    }
}