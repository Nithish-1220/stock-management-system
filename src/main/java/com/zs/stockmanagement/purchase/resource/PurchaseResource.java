package com.zs.stockmanagement.purchase.resource;

import com.zs.stockmanagement.purchase.dto.RequestPurchase;
import com.zs.stockmanagement.purchase.model.Purchase;
import com.zs.stockmanagement.purchase.service.PurchaseService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/purchases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PurchaseResource {
    private final PurchaseService purchaseService;
    private final int shopId;
    private final int branchId;

    public PurchaseResource(int shopId, int branchId) {
        purchaseService = new PurchaseService();
        this.shopId=shopId;
        this.branchId = branchId;
    }

    @GET
    public List<Purchase> getPurchases(){
        return purchaseService.getPurchases(shopId,branchId);
    }

    @GET
    @Path("/{purchase_id}")
    public Purchase getPurchases(@PathParam("purchase_id") int purchaseId){
        return purchaseService.getPurchases(shopId,branchId,purchaseId);
    }

    @POST
    public Purchase addPurchase(RequestPurchase requestPurchase){
        return purchaseService.addPurchase(shopId,branchId,requestPurchase);
    }

    @DELETE
    @Path("/{purchase_id}")
    public boolean deletePurchase(@PathParam("purchase_id") int purchaseId){
        System.out.println("delete purchase in resourse");
        return purchaseService.deletePurchase(shopId,branchId,purchaseId);
    }

    @PATCH
    @Path("/{purchase_id}")
    public Purchase updatePurchase(@PathParam("purchase_id") int purchaseId,RequestPurchase requestPurchase){
        return purchaseService.updatePurchase(shopId,branchId,purchaseId,requestPurchase);
    }


}
