package com.zs.stockmanagement.shop.dto;

import com.zs.stockmanagement.address.dto.ResponseAddress;
import com.zs.stockmanagement.address.model.Address;

public class ResponseBranch {
    private String branchName;
    private ResponseAddress responseAddress;

    public ResponseBranch() {
    }

    public ResponseBranch(String branchName, ResponseAddress responseAddress) {
        this.branchName = branchName;
        this.responseAddress = responseAddress;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ResponseAddress getResponseAddress() {
        return responseAddress;
    }

    public void setResponseAddress(ResponseAddress responseAddress) {
        this.responseAddress = responseAddress;
    }
}
