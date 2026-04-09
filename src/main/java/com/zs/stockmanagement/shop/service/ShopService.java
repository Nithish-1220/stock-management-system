package com.zs.stockmanagement.shop.service;

import com.zs.stockmanagement.address.dto.ResponseAddress;
import com.zs.stockmanagement.shop.dao.ShopDAO;
import com.zs.stockmanagement.shop.dto.ResponseBranch;
import com.zs.stockmanagement.shop.dto.ResponseShop;
import com.zs.stockmanagement.shop.model.Branch;
import com.zs.stockmanagement.shop.model.Shop;

import java.util.List;

public class ShopService {

    private ShopDAO shopDAO;

    public ShopService() {
        this.shopDAO = new ShopDAO();
    }

    public List<Shop> getShops() {
        return shopDAO.getShops();
    }

    public ResponseShop getShops(int shopId) {
        Shop shop = shopDAO.getShops(shopId);
        if (shop == null || shop.getShopName() == null) {
            throw new RuntimeException("shop is null or not found");
        }
        return new ResponseShop(shop.getShopName(), shop.getShopOwnerName());
    }

    public List<Branch> getBranches(int shopId) {
        return shopDAO.getBranches(shopId);
    }

    public ResponseBranch getBranches(int shopId, int branchId) {
        Branch branch = shopDAO.getBranches(shopId, branchId);

        if (branch == null || branch.getBranchName() == null) {
            throw new RuntimeException("branch is null or not found");
        }

        ResponseAddress responseAddress = null;
        if (branch.getAddress() != null) {
            responseAddress = new ResponseAddress(
                    branch.getAddress().getDoorNumber(),
                    branch.getAddress().getStreetName(),
                    branch.getAddress().getCityName(),
                    branch.getAddress().getCityType(),
                    branch.getAddress().getStateName(),
                    branch.getAddress().getCountryName(),
                    branch.getAddress().getPincode()
            );
        }

        return new ResponseBranch(branch.getBranchName(), responseAddress);
    }

    public Shop addShop(Shop shop, Branch branch) {
        return shopDAO.addShop(shop, branch);
    }

    public boolean deleteShop(int shopId) {
        return shopDAO.deleteShop(shopId);
    }

    public Branch addBranch(int shopId, Branch branch) {
        return shopDAO.addBranch(shopId, branch);
    }

    public boolean deleteBranch(int shopId, int branchId) {
        return shopDAO.deleteBranch(shopId, branchId);
    }
}