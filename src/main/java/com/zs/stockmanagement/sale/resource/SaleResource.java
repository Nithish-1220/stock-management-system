package com.zs.stockmanagement.sale.resource;

import com.zs.stockmanagement.sale.dto.AddRequestSale;
import com.zs.stockmanagement.sale.model.Sale;
import com.zs.stockmanagement.sale.service.SalesService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
public class SaleResource {
    private final SalesService salesService;
    private final int shopId;
    private final int branchId;

    public SaleResource(int shopId, int branchId) {
        salesService = new SalesService();
        this.shopId = shopId;
        this.branchId = branchId;
    }

    @GET
    public List<Sale> getSales() {
        return salesService.getSales(shopId, branchId);
    }

    @GET
    @Path("/{Sale_id}")
    public Sale getSales(@PathParam("Sale_id") int SaleId) {
        return salesService.getSales(shopId, branchId, SaleId);
    }

    @POST
    public Sale addSale(AddRequestSale sale) {
        return salesService.addSale(shopId, branchId, sale);
    }

    @DELETE
    @Path("/{saleId}")
    public boolean deleteSale(@PathParam("saleId") int saleId) {
        return salesService.deleteSale(shopId, branchId, saleId);
    }

    @PUT
    @Path("/{saleId}")
    public boolean updateSale(@PathParam("saleId") int saleId, AddRequestSale requestSale) {
        return salesService.updateSale(shopId, branchId, saleId, requestSale);
    }
}

