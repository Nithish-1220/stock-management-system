package com.zs.stockmanagement.customer.dao;

import com.zs.stockmanagement.customer.model.Customer;
import com.zs.stockmanagement.exceptions.DataBaseException;
import com.zs.stockmanagement.utils.DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getCustomers(int shopId, int branchId) {
        String customerQuery =
                "SELECT c.customer_id, c.customer_name, c.customer_email, c.customer_phone_number " +
                        "FROM customers c " +
                        "JOIN branches b ON c.branch_id = b.branch_id " +
                        "WHERE b.shop_id = ? AND b.branch_id = ?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement customerPs = connection.prepareStatement(customerQuery)) {

            customerPs.setInt(1, shopId);
            customerPs.setInt(2, branchId);

            try (ResultSet customerRs = customerPs.executeQuery()) {
                List<Customer> customers = new ArrayList<>();
                while (customerRs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(customerRs.getInt("customer_id"));
                    customer.setCustomerName(customerRs.getString("customer_name"));
                    customer.setCustomerEmail(customerRs.getString("customer_email"));
                    customer.setCustomerPhoneNumber(customerRs.getString("customer_phone_number"));
                    customers.add(customer);
                }
                return customers;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Customer getCustomers(int shopId, int branchId, int customerId) {
        String customerQuery =
                "SELECT c.customer_id, c.customer_name, c.customer_email, c.customer_phone_number " +
                        "FROM customers c " +
                        "JOIN branches b ON c.branch_id = b.branch_id " +
                        "WHERE b.shop_id = ? AND b.branch_id = ? AND c.customer_id = ?;";

        try (Connection connection = DBController.getConnection();
             PreparedStatement customerPs = connection.prepareStatement(customerQuery)) {

            customerPs.setInt(1, shopId);
            customerPs.setInt(2, branchId);
            customerPs.setInt(3, customerId);

            try (ResultSet customerRs = customerPs.executeQuery()) {
                Customer customer = new Customer();
                if (customerRs.next()) {
                    customer.setCustomerId(customerRs.getInt("customer_id"));
                    customer.setCustomerName(customerRs.getString("customer_name"));
                    customer.setCustomerEmail(customerRs.getString("customer_email"));
                    customer.setCustomerPhoneNumber(customerRs.getString("customer_phone_number"));
                }
                return customer;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    //TODO addCustomers
    public List<Customer> addCustomers() {
        return null;
    }

    public Customer addCustomer(int shopId, int branchId, Customer customer) {

        String validateQuery = "SELECT branch_id FROM branches WHERE branch_id = ? AND shop_id = ?";
        String insertQuery = "INSERT INTO customers (customer_name, customer_email, customer_phone_number, branch_id) " +
                "VALUES (?,?,?,?)";

        try (Connection connection = DBController.getConnection()) {

            try (PreparedStatement validatePs = connection.prepareStatement(validateQuery)) {
                validatePs.setInt(1, branchId);
                validatePs.setInt(2, shopId);
                try (ResultSet validateRs = validatePs.executeQuery()) {
                    if (!validateRs.next()) {
                        System.err.println("branchId " + branchId + " is not belongs corresponding shopId " + shopId);
                        return null;
                    }
                }
            }

            try (PreparedStatement insertPs = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertPs.setString(1, customer.getCustomerName());
                insertPs.setString(2, customer.getCustomerEmail());
                insertPs.setString(3, customer.getCustomerPhoneNumber());
                insertPs.setInt(4, branchId);
                insertPs.executeUpdate();

                try (ResultSet insertRs = insertPs.getGeneratedKeys()) {
                    if (insertRs.next()) {
                        customer.setCustomerId(insertRs.getInt(1));
                    }
                }
            }
            return customer;

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Customer updateCustomer(int shopId, int branchId, int customerId, Customer customer) {

        String checkQuery = """
                SELECT c.customer_id
                FROM customers c
                JOIN branches b ON c.branch_id = b.branch_id
                WHERE c.customer_id=? AND c.branch_id=? AND b.shop_id=?""";

        StringBuilder updateQuery = new StringBuilder("UPDATE customers SET ");
        List<Object> params = new ArrayList<>();

        if (customer.getCustomerName() != null) {
            updateQuery.append("customer_name = ?, ");
            params.add(customer.getCustomerName());
        }

        if (customer.getCustomerEmail() != null) {
            updateQuery.append("customer_email = ?, ");
            params.add(customer.getCustomerEmail());
        }

        if (customer.getCustomerPhoneNumber() != null) {
            updateQuery.append("customer_phone_number = ?, ");
            params.add(customer.getCustomerPhoneNumber());
        }

        if (params.isEmpty()) {
            throw new RuntimeException("No fields to update");
        }

        updateQuery.setLength(updateQuery.length() - 2);

        updateQuery.append(" WHERE customer_id = ? AND branch_id = ?");
        params.add(customerId);
        params.add(branchId);

        try (Connection connection = DBController.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
                    checkPs.setInt(1, customerId);
                    checkPs.setInt(2, branchId);
                    checkPs.setInt(3, shopId);
                    try (ResultSet rs = checkPs.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return null;
                        }
                    }
                }

                try (PreparedStatement updatePs = connection.prepareStatement(updateQuery.toString())) {
                    for (int i = 0; i < params.size(); i++) {
                        updatePs.setObject(i + 1, params.get(i));
                    }
                    int rows = updatePs.executeUpdate();
                    if (rows == 0) {
                        connection.rollback();
                        return null;
                    }
                }

                connection.commit();
                customer.setCustomerId(customerId);
                return customer;

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
}