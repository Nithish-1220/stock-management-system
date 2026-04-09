package com.zs.stockmanagement.product.model;

import java.util.*;

public class Product {
    private int productId;
    private String productName;
    private String categoryName;
    private String brandName;
    private String model;

    public Product() {
    }

    public Product(String productName,String categoryName,String brandName, String model) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.model = model;
        this.productName = productName;
    }

    public Product(int productId, String productName,String categoryName,String brandName, String model) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.model = model;
        this.productId = productId;
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

    public Product(String brandName, String categoryName, String model, int productId, String productName, List<Variant> variants) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.model = model;
        this.productId = productId;
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "brandName='" + brandName + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
