package com.zs.stockmanagement.vendor.dto;

import com.zs.stockmanagement.address.dto.RequestAddress;
import com.zs.stockmanagement.address.model.Address;

public class RequestVendor {
    private String vendorName;
    private RequestAddress vendorAddress;
    private String vendorPhoneNumber;

    public RequestVendor() {
    }

    public RequestVendor(RequestAddress vendorAddress, String vendorName, String vendorPhoneNumber) {
        this.vendorAddress = vendorAddress;
        this.vendorName = vendorName;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public RequestAddress getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(RequestAddress vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }
}
