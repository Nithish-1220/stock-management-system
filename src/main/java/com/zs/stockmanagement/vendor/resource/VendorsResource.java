package com.zs.stockmanagement.vendor.resource;

import com.zs.stockmanagement.vendor.dto.RequestVendor;
import com.zs.stockmanagement.vendor.dto.ResponseVendor;
import com.zs.stockmanagement.vendor.model.Vendor;
import com.zs.stockmanagement.vendor.service.VendorService;
import jakarta.validation.metadata.ReturnValueDescriptor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigInteger;
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

    @POST
    public Response addVendors(RequestVendor requestVendor){
        try{
            Vendor result = vendorService.addVendor(requestVendor);
            return Response.ok(result).build();
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @PATCH
    public Response updateVendors(Vendor vendor){
        try{
            Vendor result = vendorService.updateVendor(vendor);
            return Response.ok(result).build();
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{vendor_id}")
    public Response deleteVendors(@PathParam("vendor_id") int vendorId){
        try{
            Boolean result = vendorService.deleteVendor(vendorId);
            return Response.ok(result).build();
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
