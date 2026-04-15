package com.zs.stockmanagement.vendor.model;

import com.zs.stockmanagement.address.model.Address;

public class Vendor {
    private int vendorId;
    private String vendorName;
    private Address vendorAddress;
    private String vendorPhoneNumber;

    public Vendor(Address vendorAddress, int vendorId, String vendorName, String vendorPhoneNumber) {
        this.vendorAddress = vendorAddress;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public Vendor() {

    }

    public Vendor(Address address, String vendorName, String vendorPhoneNumber) {
        this.vendorAddress = address;
        this.vendorName = vendorName;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public Address getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(Address vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    @Override
    public String toString() {
        return "Vendor{" +
                "vendorAddress=" + vendorAddress +
                ", vendorId=" + vendorId +
                ", vendorName='" + vendorName + '\'' +
                ", vendorPhoneNumber='" + vendorPhoneNumber + '\'' +
                '}';
    }
}
