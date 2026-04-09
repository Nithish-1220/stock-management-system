package com.zs.stockmanagement.vendor.resource;

import com.zs.stockmanagement.vendor.dto.ResponseVendor;
import com.zs.stockmanagement.vendor.model.Vendor;
import com.zs.stockmanagement.vendor.service.VendorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/vendors")
@Produces(MediaType.APPLICATION_JSON)
public class VendorsResource {
    private final VendorService vendorService;

    public VendorsResource(){
        vendorService = new VendorService();
    }

    @GET
    public List<Vendor> getVendors(){
        return vendorService.getVendors();
    }

    @GET
    @Path("/{vendor_id}")
    public ResponseVendor getVendors(@PathParam("vendor_id") int vendorId){
        return vendorService.getVendors(vendorId);
    }

}
