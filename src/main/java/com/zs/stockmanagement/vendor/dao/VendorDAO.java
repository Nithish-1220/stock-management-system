package com.zs.stockmanagement.vendor.dao;

import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.utils.DBController;
import com.zs.stockmanagement.vendor.model.Vendor;

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
            throw new DataBaseException(e.getMessage());
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
            throw new DataBaseException(e.getMessage());
        }
    }

    public Vendor addVendor(Vendor vendor) {
        String insertQuery = "insert into vendors(vendor_name, address_id, vendor_phone_number)  values(?,?,?);";
        Integer vendorId = null;
        try (Connection connection = DBController.getConnection()) {

            try (PreparedStatement insertPs = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertPs.setString(1, vendor.getVendorName());
                insertPs.setInt(2, vendor.getVendorAddress().getAddressId());
                insertPs.setString(3, vendor.getVendorPhoneNumber());
                insertPs.executeUpdate();
                try (ResultSet insertRs = insertPs.getGeneratedKeys()) {
                    if (insertRs.next()) {
                        vendorId = insertRs.getInt(1);
                    } else {
                        throw new RuntimeException("vendor Id is null");
                    }
                }
            }
            vendor.setVendorId(vendorId);
            return vendor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO addVendors
    public List<Vendor> addVendors() {
        return null;
    }

    //TODO deleteVendor
    public boolean deleteVendor(int vendorId) {
        String getAddressQuery = "select address_id from vendors where vendor_id = ?;";
        String vendorQuery = "delete from vendors where vendor_id = ?;";
        String deleteAddressQuery = "delete from address where address_id = ?";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Integer addressId = null;
                try (PreparedStatement addressPs = connection.prepareStatement(getAddressQuery)) {
                    addressPs.setInt(1, vendorId);
                    try (ResultSet addressRs = addressPs.executeQuery()) {
                        if (addressRs.next()) {
                            addressId = addressRs.getInt("address_id");
                        }
                    }
                }

                try (PreparedStatement vendorPs = connection.prepareStatement(vendorQuery)) {
                    vendorPs.setInt(1, vendorId);
                    vendorPs.executeUpdate();
                }

                if (addressId != null) {
                    try (PreparedStatement deleteAddresses = connection.prepareStatement(deleteAddressQuery)) {
                        deleteAddresses.setInt(1, addressId);
                        deleteAddresses.executeUpdate();
                    }
                }
                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw new DataBaseException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    //Todo getVendors by corresponding shop_id and branch_id
    public List<Vendor> getVendors(int shopId, int branchId) {
        return null;
    }

    //Todo getVendors by corresponding shop_id and branch_id and vendorId
    public Vendor getVendors(int shopId, int branchId, int vendorId) {
        return null;
    }

    public void updateVendor(Vendor vendor) {

        StringBuilder query = new StringBuilder("UPDATE vendors SET ");
        List<Object> values = new ArrayList<>();

        if (vendor.getVendorName() != null) {
            query.append("vendor_name = ?, ");
            values.add(vendor.getVendorName());
        }

        if (vendor.getVendorPhoneNumber() != null) {
            query.append("vendor_phone_number = ?, ");
            values.add(vendor.getVendorPhoneNumber());
        }

        if (values.isEmpty()) {
            return;
        }
        query.setLength(query.length() - 2);

        query.append(" WHERE vendor_id = ?");
        values.add(vendor.getVendorId());

        try (Connection connection = DBController.getConnection();
             PreparedStatement ps = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
}
