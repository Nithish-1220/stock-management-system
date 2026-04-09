package com.zs.stockmanagement.customer.resource;

import com.zs.stockmanagement.customer.dto.RequestCustomer;
import com.zs.stockmanagement.customer.model.Customer;
import com.zs.stockmanagement.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService;
    private final int shopId;
    private final int branchId;

    public CustomerResource() {
        this.customerService = new CustomerService();
        this.shopId = 0;
        this.branchId = 0;
    }

    public CustomerResource(int shopId, int branchId) {
        this.customerService = new CustomerService();
        this.shopId = shopId;
        this.branchId = branchId;
    }

    @GET
    public List<Customer> getAllCustomers() {
        return customerService.getCustomers(shopId, branchId);
    }

    @GET
    @Path("/{customer_id}")
    public RequestCustomer getCustomerById(@PathParam("customer_id") int customerId) {
        return customerService.getCustomerById(shopId, branchId, customerId);
    }

    @POST
    public Customer createCustomer(RequestCustomer requestCustomer) {
        return customerService.addCustomer(shopId, branchId, requestCustomer);
    }

    @PATCH
    @Path("/{customer_id}")
    public Customer updateCustomer(@PathParam("customer_id") int customerId,
                                   RequestCustomer requestCustomer) {
        return customerService.updateCustomer(shopId, branchId, customerId, requestCustomer);
    }

    //TODO add this method
//    @DELETE
//    @Path("/{customer_id}")
//    public void deleteCustomer(@PathParam("customer_id") int customerId) {
//        customerService.deleteCustomer(shopId, branchId, customerId);
//    }
}