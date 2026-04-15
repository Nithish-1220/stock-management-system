package com.zs.stockmanagement.productHelper.resource;


import com.zs.stockmanagement.productHelper.service.ProductHelperService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

@Path("/product-attributes")
@Produces(MediaType.APPLICATION_JSON)
public class ProductHelperResource {

    private final ProductHelperService productHelperService = new ProductHelperService();

    @GET
    public Response getAttributes(){
        return  Response.ok(productHelperService.getAttributes()).build();
    }

    @GET
    @Path("/{variant_id}")
    public Response getAttributesAndValues(@PathParam("variant_id")int variantId){
        return  Response.ok(productHelperService.getAttributesAndValues(variantId)).build();
    }

}
