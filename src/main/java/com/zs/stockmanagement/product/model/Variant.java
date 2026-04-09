package com.zs.stockmanagement.product.model;

import java.util.*;

public class Variant {
    private int variantId ;
    private Map<String,String> attributes ;
    private double price;
    private double mrp;

    public Variant() {
        attributes = new HashMap<>();
    }

    public Variant(double mrp,double price,Map<String, String> attributes) {
        this.mrp = mrp;
        this.price = price;
        this.attributes = attributes;
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

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public Variant(Map<String, String> attributes, double mrp, double price, int variantId) {
        this.attributes = attributes;
        this.mrp = mrp;
        this.price = price;
        this.variantId = variantId;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "attributes=" + attributes +
                ", variantId=" + variantId +
                ", price=" + price +
                ", mrp=" + mrp +
                '}';
    }
}
