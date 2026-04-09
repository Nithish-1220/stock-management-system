package com.zs.stockmanagement.shop.dto;

public class ResponseShop {
    private String shopName;
    private String shopOwnerName;

    public ResponseShop() {
    }

    public ResponseShop(String shopName, String shopOwnerName) {
        this.shopName = shopName;
        this.shopOwnerName = shopOwnerName;
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
}
