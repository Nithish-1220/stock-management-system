package com.zs.stockmanagement.customer.service;

import com.zs.stockmanagement.customer.dao.CustomerDAO;
import com.zs.stockmanagement.customer.dto.RequestCustomer;
import com.zs.stockmanagement.customer.exceptions.InvalidInput;
import com.zs.stockmanagement.customer.model.Customer;

import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getCustomers(int shopId, int branchId) {

        if (shopId <= 0 || branchId <= 0) throw new InvalidInput("Invalid shopId / branchId");
        List<Customer> customers = customerDAO.getCustomers(shopId, branchId);
        if (customers == null) throw new RuntimeException("Unable to fetch customers data");
        return customers;

    }

    public RequestCustomer getCustomerById(int shopId, int branchId, int customerId) {

        if (customerId <= 0) throw new InvalidInput("Invalid customerId");
        Customer customer = customerDAO.getCustomers(shopId, branchId, customerId);
        if (customer == null) throw new RuntimeException("unable to fetch the data of customer");
        return new RequestCustomer(customer.getCustomerName(),customer.getCustomerEmail(),customer.getCustomerPhoneNumber());

    }

    public Customer addCustomer(int shopId, int branchId, RequestCustomer requestCustomer) {

        Customer savedCustomer = customerDAO.addCustomer(shopId, branchId,
                new Customer(requestCustomer.getCustomerName(), requestCustomer.getCustomerEmail(), requestCustomer.getCustomerPhoneNumber()));
        if (savedCustomer == null) throw new RuntimeException("Customer creation failed");
        return savedCustomer;

    }

    public Customer updateCustomer(int shopId, int branchId, int customerId, RequestCustomer requestCustomer) {

        Customer updatedCustomer = customerDAO.updateCustomer(shopId, branchId, customerId,
                new Customer(requestCustomer.getCustomerName(), requestCustomer.getCustomerEmail(), requestCustomer.getCustomerPhoneNumber()));
        if (updatedCustomer == null) throw new RuntimeException("Customer update failed");
        return updatedCustomer;

    }

}