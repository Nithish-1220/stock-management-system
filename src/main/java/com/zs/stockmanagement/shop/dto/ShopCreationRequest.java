package com.zs.stockmanagement.shop.dto;

import com.zs.stockmanagement.shop.model.Branch;
import com.zs.stockmanagement.shop.model.Shop;

public class ShopCreationRequest {
    private Shop shop;
    private Branch branch;

    public ShopCreationRequest() {}

    public Shop getShop() { return shop; }
    public void setShop(Shop shop) { this.shop = shop; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }
}