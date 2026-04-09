package com.zs.stockmanagement.shop.dao;

import com.zs.stockmanagement.address.dao.dao;
import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.shop.model.Branch;
import com.zs.stockmanagement.shop.model.Shop;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    private dao.AddressDAO addressDAO;

    public ShopDAO() {
        addressDAO = new dao.AddressDAO();
    }

    public List<Shop> getShops() {
        String shopQuery = "select * from shops;";
        try (Connection connection = DBController.getConnection();
             PreparedStatement shopPs = connection.prepareStatement(shopQuery);
             ResultSet shopRs = shopPs.executeQuery()) {

            List<Shop> shops = new ArrayList<>();
            while (shopRs.next()) {
                Shop shop = new Shop();
                shop.setShopId(shopRs.getInt("shop_id"));
                shop.setShopName(shopRs.getString("shop_name"));
                shop.setShopOwnerName(shopRs.getString("shop_owner_name"));
                shops.add(shop);
            }
            return shops;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Shop getShops(int shopId) {
        String shopQuery = "select * from shops where shop_id = ?;";
        try (Connection connection = DBController.getConnection();
             PreparedStatement shopPs = connection.prepareStatement(shopQuery)) {

            shopPs.setInt(1, shopId);
            try (ResultSet shopRs = shopPs.executeQuery()) {
                Shop shop = new Shop();
                while (shopRs.next()) {
                    shop.setShopId(shopRs.getInt("shop_id"));
                    shop.setShopName(shopRs.getString("shop_name"));
                    shop.setShopOwnerName(shopRs.getString("shop_owner_name"));
                }
                return shop;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Branch> getBranches(int shopId) {
        String branchQuery = "select b.branch_id,b.shop_id,b.branch_name,a.address_id, a.door_number, a.street_name, a.city_name, a.city_type, s.state_name, c.country_name, p.pincode_number "
                + "from branches b "
                + "left join address a on b.address_id = a.address_id "
                + " LEFT JOIN state s ON a.state_id = s.state_id "
                + " LEFT JOIN country c ON a.country_id = c.country_id "
                + " LEFT JOIN pincode p ON a.pincode_id = p.pincode_id "
                + "WHERE shop_id = ? ";

        try (Connection connection = DBController.getConnection();
             PreparedStatement branchPs = connection.prepareStatement(branchQuery)) {

            branchPs.setInt(1, shopId);
            try (ResultSet branchRs = branchPs.executeQuery()) {
                List<Branch> branches = new ArrayList<>();
                while (branchRs.next()) {
                    Branch branch = new Branch();
                    branch.setBranchId(branchRs.getInt("branch_id"));
                    branch.setBranchName(branchRs.getString("branch_name"));

                    Address address = new Address();
                    address.setAddressId(branchRs.getInt("address_id"));
                    address.setDoorNumber(branchRs.getString("door_number"));
                    address.setStreetName(branchRs.getString("street_name"));
                    address.setCityName(branchRs.getString("city_name"));
                    address.setCityType(branchRs.getString("city_type"));
                    address.setStateName(branchRs.getString("state_name"));
                    address.setCountryName(branchRs.getString("country_name"));
                    address.setPincode(branchRs.getString("pincode_number"));

                    branch.setAddress(address);
                    branches.add(branch);
                }
                return branches;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Branch getBranches(int shopId, int branchId) {
        String branchQuery = "select b.branch_id,b.shop_id,b.branch_name,a.address_id, a.door_number, a.street_name, a.city_name, a.city_type, s.state_name, c.country_name, p.pincode_number "
                + "from branches b "
                + "left join address a on b.address_id = a.address_id "
                + " LEFT JOIN state s ON a.state_id = s.state_id "
                + " LEFT JOIN country c ON a.country_id = c.country_id "
                + " LEFT JOIN pincode p ON a.pincode_id = p.pincode_id "
                + "WHERE shop_id = ? AND branch_id = ?";

        try (Connection connection = DBController.getConnection();
             PreparedStatement branchPs = connection.prepareStatement(branchQuery)) {

            branchPs.setInt(1, shopId);
            branchPs.setInt(2, branchId);
            try (ResultSet branchRs = branchPs.executeQuery()) {
                Branch branch = new Branch();
                while (branchRs.next()) {
                    branch.setBranchId(branchRs.getInt("branch_id"));
                    branch.setBranchName(branchRs.getString("branch_name"));

                    Address address = new Address();
                    address.setAddressId(branchRs.getInt("address_id"));
                    address.setDoorNumber(branchRs.getString("door_number"));
                    address.setStreetName(branchRs.getString("street_name"));
                    address.setCityName(branchRs.getString("city_name"));
                    address.setCityType(branchRs.getString("city_type"));
                    address.setStateName(branchRs.getString("state_name"));
                    address.setCountryName(branchRs.getString("country_name"));
                    address.setPincode(branchRs.getString("pincode_number"));

                    branch.setAddress(address);
                }
                return branch;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Shop addShop(Shop shop, Branch branch) {
        System.out.println("---------------------");
        int stateId = addressDAO.getSateIdByName(branch.getAddress().getStateName());
        int countryId = addressDAO.getCountryIdByName(branch.getAddress().getCountryName());
        int pincodeId = addressDAO.getPincodeIdByPincodeNumber(branch.getAddress().getPincode());
        int addressId = addressDAO.addAddress(branch.getAddress().getDoorNumber(),
                branch.getAddress().getStreetName(), branch.getAddress().getCityName(),
                branch.getAddress().getCityType(), stateId, countryId, pincodeId);

        Integer shopId = null;
        Integer branchId = null;

        String shopQuery = "INSERT INTO shops(shop_name, shop_owner_name) values (?,?);";
        String branchQuery = "INSERT INTO branches(shop_id, branch_name, address_id) VALUES (?,?,?);";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement shopPs = connection.prepareStatement(shopQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    shopPs.setString(1, shop.getShopName());
                    shopPs.setString(2, shop.getShopOwnerName());
                    shopPs.executeUpdate();
                    try (ResultSet shopRs = shopPs.getGeneratedKeys()) {
                        if (shopRs.next()) {
                            shopId = shopRs.getInt(1);
                        } else {
                            connection.rollback();
                            throw new SQLException("shopId is null");
                        }
                    }
                }

                try (PreparedStatement branchPs = connection.prepareStatement(branchQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    branchPs.setInt(1, shopId);
                    branchPs.setString(2, branch.getBranchName());
                    branchPs.setInt(3, addressId);
                    branchPs.executeUpdate();
                    try (ResultSet branchRs = branchPs.getGeneratedKeys()) {
                        if (branchRs.next()) {
                            branchId = branchRs.getInt(1);
                        }else{
                            connection.rollback();
                            throw new SQLException("branchId is null");
                        }
                    }
                }

                connection.commit();
                return new Shop(shopId, shop.getShopName(), shop.getShopOwnerName());

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Shop> addShop() {
        return null;
    }

    public boolean deleteShop(int shopId) {
        String getAddressQuery = "select b.address_id from branches b where branch_id in (select branch_id from branches where b.shop_id = ?);";
        String shopQuery = "delete from shops where shop_id = ?";
        String deleteAddressQuery = "delete from address where address_id = ?";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                List<Integer> addresses = new ArrayList<>();
                try (PreparedStatement addressPs = connection.prepareStatement(getAddressQuery)) {
                    addressPs.setInt(1, shopId);
                    try (ResultSet addressRs = addressPs.executeQuery()) {
                        while (addressRs.next()) {
                            addresses.add(addressRs.getInt("address_id"));
                        }
                    }
                }

                try (PreparedStatement shopPs = connection.prepareStatement(shopQuery)) {
                    shopPs.setInt(1, shopId);
                    shopPs.executeUpdate();
                }

                try (PreparedStatement deleteAddresses = connection.prepareStatement(deleteAddressQuery)) {
                    for (int addressId : addresses) {
                        deleteAddresses.setInt(1, addressId);
                        deleteAddresses.executeUpdate();
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Branch addBranch(int shopId, Branch branch) {
        int stateId = addressDAO.getSateIdByName(branch.getAddress().getStateName());
        int countryId = addressDAO.getCountryIdByName(branch.getAddress().getCountryName());
        int pincodeId = addressDAO.getPincodeIdByPincodeNumber(branch.getAddress().getPincode());

        int addressId = addressDAO.addAddress(branch.getAddress().getDoorNumber(), branch.getAddress().getStreetName(),
                branch.getAddress().getCityName(), branch.getAddress().getCityType(), stateId, countryId, pincodeId);

        Integer branchId = null;
        String branchQuery = "INSERT INTO branches(shop_id, branch_name, address_id) VALUES (?,?,?);";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement branchPs = connection.prepareStatement(branchQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    branchPs.setInt(1, shopId);
                    branchPs.setString(2, branch.getBranchName());
                    branchPs.setInt(3, addressId);
                    branchPs.executeUpdate();
                    try (ResultSet branchRs = branchPs.getGeneratedKeys()) {
                        if (branchRs.next()) {
                            branchId = branchRs.getInt(1);
                        } else {
                            throw new SQLException("branch id is null");
                        }
                    }
                }

                connection.commit();
                return new Branch(branchId, branch.getBranchName(), addressDAO.getAddress(addressId));

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
                return null;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Branch> addBranches() {
        return null;
    }

    public boolean deleteBranch(int shopId, int branchId) {
        String getAddressQuery = "select b.address_id from branches b where branch_id = ?;";
        String branchQuery = "delete from branches where shop_id = ? AND branch_id = ?;";
        String deleteAddressQuery = "delete from address where address_id = ?";

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Integer addressId = null;
                try (PreparedStatement addressPs = connection.prepareStatement(getAddressQuery)) {
                    addressPs.setInt(1, branchId);
                    try (ResultSet addressRs = addressPs.executeQuery()) {
                        if (addressRs.next()) {
                            addressId = addressRs.getInt("address_id");
                        }
                    }
                }

                try (PreparedStatement branchPs = connection.prepareStatement(branchQuery)) {
                    branchPs.setInt(1, shopId);
                    branchPs.setInt(2, branchId);
                    branchPs.executeUpdate();
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
                System.err.println(e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}