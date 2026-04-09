package com.zs.stockmanagement.address.dao;

import com.zs.stockmanagement.address.model.Address;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dao {
    public static class AddressDAO {

        public int getCountryIdByName(String countryName){
            String countryQuery = "select country_id from country where country_name = ?;";
            try {
                Connection connection = DBController.getConnection();
                PreparedStatement countryPs = connection.prepareStatement(countryQuery);
                countryPs.setString(1,countryName);
                ResultSet countryRs =  countryPs.executeQuery();
                if(countryRs.next()){
                    return countryRs.getInt("country_id");
                }
                return -1;
            }catch(SQLException e){
                System.err.println(e.getMessage());
                return -1;
            }
        }

        public int getSateIdByName(String stateName){
            String stateQuery = "select state_id from state where state_name = ?;";
            try {
                Connection connection = DBController.getConnection();
                PreparedStatement statePs = connection.prepareStatement(stateQuery);
                statePs.setString(1,stateName);
                ResultSet stateRs =  statePs.executeQuery();
                if(stateRs.next()){
                    return stateRs.getInt("state_id");
                }
                return -1;
            }catch(SQLException e){
                System.err.println(e.getMessage());
                return -1;
            }
        }

        public int getPincodeIdByPincodeNumber(String  pincodeNumber){
            String pincodeQuery = "select pincode_id from pincode where pincode_number = ?;";
            try {
                Connection connection = DBController.getConnection();
                PreparedStatement pincodePs = connection.prepareStatement(pincodeQuery);
                pincodePs.setString(1,pincodeNumber);
                ResultSet pincodeRs =  pincodePs.executeQuery();
                if(pincodeRs.next()){
                    return pincodeRs.getInt("pincode_id");
                }
                return -1;
            }catch(SQLException e){
                System.err.println(e.getMessage());
                return -1;
            }
        }

        public int addAddress(String doorNumber,String StreetName,String cityName,String cityType,int stateId,int countryId,int pincode){
            String addressQuery = "INSERT INTO address(door_number,street_name,city_name,city_type,state_id,country_id,pincode_id)" +
                    " VALUES (?,?,?,?,?,?,?);";
            try{
                Connection connection = DBController.getConnection();
                PreparedStatement addressPs = connection.prepareStatement(addressQuery,PreparedStatement.RETURN_GENERATED_KEYS);
                addressPs.setString(1,doorNumber);
                addressPs.setString(2,StreetName);
                addressPs.setString(3,cityName);
                addressPs.setString(4,cityType);
                addressPs.setInt(5,stateId);
                addressPs.setInt(6,countryId);
                addressPs.setInt(7,pincode);

                addressPs.executeUpdate();

                ResultSet addressRs = addressPs.getGeneratedKeys();
                if(addressRs.next()){
                    return addressRs.getInt(1);
                }
                return -1;
            }catch(SQLException e){
                System.err.println(e.getMessage());
                return -1;
            }
        }

        public Address getAddress(int addressId){
            String addressQuery = "select a.door_number, a.street_name, a.city_name, a.city_type, s.state_name, c.country_name, p.pincode_number from address a " +
                    "LEFT JOIN state s ON a.state_id = s.state_id " +
                    "LEFT JOIN country c ON a.country_id = c.country_id " +
                    "LEFT JOIN pincode p ON a.pincode_id = p.pincode_id " +
                    "where address_id = ?;";
           try{
               Connection connection = DBController.getConnection();
               PreparedStatement addressPs = connection.prepareStatement(addressQuery);
               addressPs.setInt(1,addressId);
               ResultSet addressRs = addressPs.executeQuery();
               Address address = new Address();
               address.setAddressId(addressId);
               if(addressRs.next()){
                   address.setDoorNumber(addressRs.getString("door_number"));
                   address.setStreetName(addressRs.getString("street_name"));
                   address.setCityName(addressRs.getString("city_name"));
                   address.setCityType(addressRs.getString("city_type"));
                   address.setStateName(addressRs.getString("state_name"));
                   address.setCountryName(addressRs.getString("country_name"));
                   address.setPincode(addressRs.getString("pincode_number"));
               }
               return address;
           }catch(SQLException e){
               System.err.println(e.getMessage());
               return null;
           }

        }
    }
}
