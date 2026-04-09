package com.zs.stockmanagement.shop.model;

public class Shop {
    private int shopId ;
    private String shopName;
    private String shopOwnerName;

    public Shop(int shopId, String shopName, String shopOwnerName) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopOwnerName = shopOwnerName;
    }

    public Shop() {
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    @Override
    public String toString() {
        return "Shop{" +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopOwnerName='" + shopOwnerName + '\'' +
                '}';
    }
}
