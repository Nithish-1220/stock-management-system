package com.zs.stockmanagement.shop.model;

import com.zs.stockmanagement.address.model.Address;

public class Branch {

    private int branchId;
    private String branchName;
    private Address address;

    public Branch() {
    }

    public Branch(int branchId, String branchName,Address responseAddress) {
        this.address = responseAddress;
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
