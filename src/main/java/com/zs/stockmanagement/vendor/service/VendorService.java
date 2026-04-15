package com.zs.stockmanagement.vendor.service;

import com.zs.stockmanagement.address.dao.AddressDAO;
import com.zs.stockmanagement.address.dto.RequestAddress;
import com.zs.stockmanagement.address.dto.ResponseAddress;
import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.vendor.dao.VendorDAO;
import com.zs.stockmanagement.vendor.dto.RequestVendor;
import com.zs.stockmanagement.vendor.dto.ResponseVendor;
import com.zs.stockmanagement.vendor.model.Vendor;

import java.util.List;

public class VendorService {

    private final VendorDAO vendorDAO;
    private final AddressDAO addressDAO;

    public VendorService() {
        this.vendorDAO = new VendorDAO();
        this.addressDAO = new AddressDAO();
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

    public Vendor addVendor(RequestVendor requestVendor){

        RequestAddress requestAdd = requestVendor.getVendorAddress();
        int stateId = addressDAO.getStateIdByName(requestVendor.getVendorAddress().getStateName());
        int countryId = addressDAO.getCountryIdByName(requestVendor.getVendorAddress().getCountryName());
        int pincodeId = addressDAO.getPincodeIdByPincodeNumber(requestVendor.getVendorAddress().getPincode());
        int addressId = addressDAO.addAddress(requestVendor.getVendorAddress().getDoorNumber(),
                requestVendor.getVendorAddress().getStreetName(), requestVendor.getVendorAddress().getCityName(),
                requestVendor.getVendorAddress().getCityType(), stateId, countryId, pincodeId);
        Address address = new Address(addressId,requestAdd.getDoorNumber(),requestAdd.getStreetName(),requestAdd.getCityName(),requestAdd.getCityType(),requestAdd.getStateName(),requestAdd.getCountryName(),requestAdd.getPincode());
        Vendor vendor = new Vendor(address,requestVendor.getVendorName(), requestVendor.getVendorPhoneNumber());
        Vendor addedVendor = vendorDAO.addVendor(vendor);
        if(addedVendor==null){
            throw new RuntimeException("addedVendor is null");
        }
        return addedVendor;
    }
    public Vendor updateVendor(Vendor vendor) {

        Vendor existingVendor = vendorDAO.getVendors(vendor.getVendorId());

        if (existingVendor == null) {
            throw new RuntimeException("Vendor not found with id: " + vendor.getVendorId());
        }

        if (vendor.getVendorName() != null) {
            existingVendor.setVendorName(vendor.getVendorName());
        }

        if (vendor.getVendorPhoneNumber() != null) {
            existingVendor.setVendorPhoneNumber(vendor.getVendorPhoneNumber());
        }

        if (vendor.getVendorAddress() != null) {
            System.out.println(vendor.getVendorAddress());
            Address patchAddress = vendor.getVendorAddress();
            patchAddress.setAddressId(existingVendor.getVendorAddress().getAddressId());

            addressDAO.updateAddress(patchAddress);
        }

        vendorDAO.updateVendor(existingVendor);

        return existingVendor;
    }

    public boolean deleteVendor(int vendorId){
        return vendorDAO.deleteVendor(vendorId);
    }
}