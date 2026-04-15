package com.zs.stockmanagement.address.dto;

public class RequestAddress {
    private String doorNumber;
    private String streetName;
    private String cityName;
    private String cityType;
    private String stateName;
    private String countryName;
    private String pincode;

    public RequestAddress() {
    }

    public RequestAddress(String doorNumber, String streetName, String cityName, String cityType, String stateName, String countryName, String pincode) {
        this.cityName = cityName;
        this.cityType = cityType;
        this.countryName = countryName;
        this.doorNumber = doorNumber;
        this.pincode = pincode;
        this.stateName = stateName;
        this.streetName = streetName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
