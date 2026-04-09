package com.zs.stockmanagement.vendor.service;

import com.zs.stockmanagement.address.dto.ResponseAddress;
import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.vendor.dao.VendorDAO;
import com.zs.stockmanagement.vendor.dto.ResponseVendor;
import com.zs.stockmanagement.vendor.model.Vendor;

import java.util.List;

public class VendorService {

    private final VendorDAO vendorDAO;

    public VendorService() {
        this.vendorDAO = new VendorDAO();
    }

    public List<Vendor> getVendors() {
        return vendorDAO.getVendors();
    }

    public ResponseVendor getVendors(int vendorId) {
        Vendor vendor = vendorDAO.getVendors(vendorId);

        if (vendor == null) {
            throw new RuntimeException("vendor not found with id:" + vendorId);
        }

        ResponseVendor responseVendor = new ResponseVendor();
        responseVendor.setVendorName(vendor.getVendorName());
        responseVendor.setVendorPhoneNumber(vendor.getVendorPhoneNumber());

        if (vendor.getVendorAddress() != null) {
            Address address = vendor.getVendorAddress();
            ResponseAddress responseAddress = new ResponseAddress(
                    address.getDoorNumber(),
                    address.getStreetName(),
                    address.getCityName(),
                    address.getCityType(),
                    address.getStateName(),
                    address.getCountryName(),
                    address.getPincode()
            );
            responseVendor.setResponseVendorAddress(responseAddress);
        }

        return responseVendor;
    }
}