package com.zs.stockmanagement.product.dto;

import com.zs.stockmanagement.product.model.Variant;

import java.util.List;

public class AddProductRequest {
    private RequestProduct product;
    private List<RequestVariant> variants;

    public RequestProduct getProduct() {
        return product;
    }

    public void setProduct(RequestProduct product) {
        this.product = product;
    }

    public List<RequestVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<RequestVariant> variants) {
        this.variants = variants;
    }
}