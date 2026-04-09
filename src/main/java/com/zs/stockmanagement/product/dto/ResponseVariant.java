package com.zs.stockmanagement.product.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseVariant {
    private double price;
    private double mrp;
    private Map<String,String> attributes;

    public ResponseVariant(Map<String, String> attributes, double mrp, double price) {
        this.attributes = attributes;
        this.mrp = mrp;
        this.price = price;
    }

    public ResponseVariant() {
        attributes= new HashMap<>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
