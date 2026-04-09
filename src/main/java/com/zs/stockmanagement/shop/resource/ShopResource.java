package com.zs.stockmanagement.shop.resource;

import com.zs.stockmanagement.inventory.resource.InventoryResource;
import com.zs.stockmanagement.purchase.resource.PurchaseResource;
import com.zs.stockmanagement.reports.profit.resource.ReportResource;
import com.zs.stockmanagement.sale.resource.SaleResource;
import com.zs.stockmanagement.shop.dto.ResponseBranch;
import com.zs.stockmanagement.shop.dto.ResponseShop;
import com.zs.stockmanagement.shop.dto.ShopCreationRequest;
import com.zs.stockmanagement.shop.model.Branch;
import com.zs.stockmanagement.shop.model.Shop;
import com.zs.stockmanagement.shop.service.ShopService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.zs.stockmanagement.customer.resource.CustomerResource;
import jakarta.ws.rs.core.Response;

import java.util.List;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/stock-management-system/api", description = "Local Tomcat Server")
        }
)
@Tag(name = "Shops", description = "Shop management APIs")

@Path("/shops")
@Produces(MediaType.APPLICATION_JSON)
public class ShopResource {
    private final ShopService shopService;

    public ShopResource() {
        this.shopService = new ShopService();
    }

    @Operation(summary = "Get all shops")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of shops returned")
    })
    @GET
    public List<Shop> getShops(){
        return shopService.getShops();
    }

    @Operation(summary = "Get shop by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop found"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @GET
    @Path("/{shop_id}")
    public ResponseShop getShops(@PathParam("shop_id") int shopId){
        return shopService.getShops(shopId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Shop addShop(ShopCreationRequest request) {
        return shopService.addShop(request.getShop(), request.getBranch());
    }

    @DELETE
    @Path("/{shop_id}")
    public Response deleteShop(@PathParam("shop_id") int shopId) {
        boolean isDeleted = shopService.deleteShop(shopId);
        if (isDeleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{shop_id}/branches")
    public List<Branch> getBranches(@PathParam("shop_id") int shopId){
        return shopService.getBranches(shopId);
    }

    @GET
    @Path("/{shop_id}/branches/{branch_id}")
    public ResponseBranch getBranches(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId){
        return shopService.getBranches(shopId, branchId);
    }

    @POST
    @Path("/{shop_id}/branches")
    @Consumes(MediaType.APPLICATION_JSON)
    public Branch addBranch(@PathParam("shop_id") int shopId, Branch branch) {
        return shopService.addBranch(shopId, branch);
    }


    @DELETE
    @Path("/{shop_id}/branches/{branch_id}")
    public Response deleteBranch(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        boolean isDeleted = shopService.deleteBranch(shopId, branchId);
        if (isDeleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

//    ---------------- PRODUCTS -------------------
    @Path("/{shop_id}/branches/{branch_id}/products")
    public com.zs.stockmanagement.product.resource.ProductResource getProductsResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new com.zs.stockmanagement.product.resource.ProductResource(shopId,branchId);
    }

//    ------------------ CUSTOMERS --------------------
    @Path("/{shop_id}/branches/{branch_id}/customers")
    public CustomerResource getCustomersResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new CustomerResource(shopId,branchId);
    }

//    ----------------- STOCKS --------------------------
    @Path("/{shop_id}/branches/{branch_id}/stocks")
    public InventoryResource getInventoryResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new InventoryResource(shopId,branchId);
    }

//    ------------------ PURCHASES ------------------------
    @Path("/{shop_id}/branches/{branch_id}/purchases")
    public PurchaseResource getPurchasesResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new PurchaseResource(shopId,branchId);
    }

//    ------------------- SALES ---------------------
    @Path("/{shop_id}/branches/{branch_id}/sales")
    public SaleResource getSaleResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new SaleResource(shopId,branchId);
    }

//    ---------------------- REPORTS ----------------
    @Path("/{shop_id}/branches/{branch_id}/reports")
    public ReportResource getReportResource(@PathParam("shop_id") int shopId, @PathParam("branch_id") int branchId) {
        return new ReportResource(shopId,branchId);
    }


}
