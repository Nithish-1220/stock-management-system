package com.zs.stockmanagement.customer.dto;

public class RequestCustomer {

    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;

    public RequestCustomer() {}

    public RequestCustomer(String customerName, String customerEmail, String customerPhoneNumber) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerEmail='" + customerEmail + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                '}';
    }

}
