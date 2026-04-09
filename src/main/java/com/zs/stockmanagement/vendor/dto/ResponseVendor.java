package com.zs.stockmanagement.vendor.dto;

import com.zs.stockmanagement.address.dto.ResponseAddress;
import com.zs.stockmanagement.address.model.Address;

public class ResponseVendor {
    private String vendorName;
    private ResponseAddress responseVendorAddress;
    private String vendorPhoneNumber;

    public ResponseVendor() {
    }

    public ResponseVendor(ResponseAddress responseVendorAddress, String vendorName, String vendorPhoneNumber) {
        this.responseVendorAddress = responseVendorAddress;
        this.vendorName = vendorName;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public ResponseAddress getResponseVendorAddress() {
        return responseVendorAddress;
    }

    public void setResponseVendorAddress(ResponseAddress responseVendorAddress) {
        this.responseVendorAddress = responseVendorAddress;
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
