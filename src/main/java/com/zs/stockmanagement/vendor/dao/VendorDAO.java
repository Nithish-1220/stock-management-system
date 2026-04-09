package com.zs.stockmanagement.vendor.dao;

import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.vendor.model.Vendor;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO {

    public List<Vendor> getVendors() {
        String vendorQuery = "select v.vendor_id, v.vendor_name,v.vendor_phone_number,a.address_id, a.door_number, a.street_name, a.city_name, a.city_type, s.state_name, c.country_name, p.pincode_number " +
                "from vendors v " +
                "left join address a on v.address_id = a.address_id " +
                "LEFT JOIN state s ON a.state_id = s.state_id " +
                "LEFT JOIN country c ON a.country_id = c.country_id " +
                "LEFT JOIN pincode p ON a.pincode_id = p.pincode_id;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement vendorPs = connection.prepareStatement(vendorQuery);
             ResultSet vendorRs = vendorPs.executeQuery()) {

            List<Vendor> vendors = new ArrayList<>();
            while (vendorRs.next()) {
                Vendor vendor = new Vendor();
                vendor.setVendorId(vendorRs.getInt("vendor_id"));
                vendor.setVendorName(vendorRs.getString("vendor_name"));
                vendor.setVendorPhoneNumber(vendorRs.getString("vendor_phone_number"));

                Address address = new Address();
                address.setAddressId(vendorRs.getInt("address_id"));
                address.setDoorNumber(vendorRs.getString("door_number"));
                address.setStreetName(vendorRs.getString("street_name"));
                address.setCityName(vendorRs.getString("city_name"));
                address.setCityType(vendorRs.getString("city_type"));
                address.setStateName(vendorRs.getString("state_name"));
                address.setCountryName(vendorRs.getString("country_name"));
                address.setPincode(vendorRs.getString("pincode_number"));

                vendor.setVendorAddress(address);
                vendors.add(vendor);
            }
            return vendors;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public Vendor getVendors(int vendorId) {
        String vendorQuery = "select v.vendor_id, v.vendor_name,v.vendor_phone_number,a.address_id, a.door_number, a.street_name, a.city_name, a.city_type, s.state_name, c.country_name, p.pincode_number " +
                "from vendors v " +
                "left join address a on v.address_id = a.address_id " +
                "LEFT JOIN state s ON a.state_id = s.state_id " +
                "LEFT JOIN country c ON a.country_id = c.country_id " +
                "LEFT JOIN pincode p ON a.pincode_id = p.pincode_id " +
                "WHERE v.vendor_id = ?";

        try (Connection connection = DBController.getConnection();
             PreparedStatement vendorPs = connection.prepareStatement(vendorQuery)) {
            vendorPs.setInt(1, vendorId);

            try (ResultSet vendorRs = vendorPs.executeQuery()) {
                Vendor vendor = new Vendor();

                if (vendorRs.next()) {
                    vendor.setVendorId(vendorRs.getInt("vendor_id"));
                    vendor.setVendorName(vendorRs.getString("vendor_name"));
                    vendor.setVendorPhoneNumber(vendorRs.getString("vendor_phone_number"));

                    Address address = new Address();
                    address.setAddressId(vendorRs.getInt("address_id"));
                    address.setDoorNumber(vendorRs.getString("door_number"));
                    address.setStreetName(vendorRs.getString("street_name"));
                    address.setCityName(vendorRs.getString("city_name"));
                    address.setCityType(vendorRs.getString("city_type"));
                    address.setStateName(vendorRs.getString("state_name"));
                    address.setCountryName(vendorRs.getString("country_name"));
                    address.setPincode(vendorRs.getString("pincode_number"));

                    vendor.setVendorAddress(address);
                }
                return vendor;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //TODO addVendor
    public Vendor addVendor(){
        return  null;
    }

    //TODO addVendors
    public List<Vendor> addVendors(){
        return null;
    }

    //TODO deleteVendor
    public boolean deleteVendor(){
        return false;
    }

    //Todo getVendors by corresponding shop_id and branch_id
    public List<Vendor> getVendors(int shopId,int branchId){
        return null;
    }

    //Todo getVendors by corresponding shop_id and branch_id and vendorId
    public Vendor getVendors(int shopId,int branchId,int vendorId){
        return null;
    }

    //TODO update vendor with shop_id,branch_id,vendor_id,data
    public Vendor updateVendor(){
        return null;
    }
}
