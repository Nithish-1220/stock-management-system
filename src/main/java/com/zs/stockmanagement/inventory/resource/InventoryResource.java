package com.zs.stockmanagement.inventory.resource;

import com.zs.stockmanagement.inventory.model.Stock;
import com.zs.stockmanagement.inventory.service.InventoryService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/stocks")
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private final InventoryService inventoryService;
    private final int shopId;
    private final int branchId;

    public InventoryResource(int shopId, int branchId) {
        inventoryService = new InventoryService();
        this.shopId = shopId;
        this.branchId = branchId;
    }

    @GET
    public List<Stock> getStocks() {
        return inventoryService.getStocks(shopId, branchId);
    }

    @GET
    @Path("/products/{product_id}/variants/{variant_id}")
    public Response getStocks(@PathParam("product_id") int productId, @PathParam("variant_id") int variantId) {
        Stock stock = inventoryService.getStocks(shopId, branchId, productId, variantId);

        if (stock == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("stock not found for given variant").build();
        }
        return Response.ok(stock).build();
    }

}
