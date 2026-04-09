package com.zs.stockmanagement.product.dto;

import com.zs.stockmanagement.product.model.Variant;

import java.util.List;

public class RequestProduct {

    private String productName;
    private String categoryName;
    private String brandName;
    private String model;

    public RequestProduct() {
    }

    public RequestProduct(String productName, String categoryName, String brandName, String model) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.model = model;
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @Override
    public String toString() {
        return "Product{" +
                "brandName='" + brandName + '\'' +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", model='" + model + '\'' +
                '}';
    }


}
