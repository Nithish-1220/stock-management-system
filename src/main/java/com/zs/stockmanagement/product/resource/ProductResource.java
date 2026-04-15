package com.zs.stockmanagement.product.resource;

import com.zs.stockmanagement.product.dto.*;
import com.zs.stockmanagement.product.model.Product;
import com.zs.stockmanagement.product.model.Variant;
import com.zs.stockmanagement.product.service.ProductFlowService;
import com.zs.stockmanagement.product.service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final ProductService productService;
    ProductFlowService service;
    private final int shopId;
    private final int branchId;

    public ProductResource(int shopId, int branchId) {
        productService = new ProductService();
        service = new ProductFlowService();
        this.shopId = shopId;
        this.branchId = branchId;
    }

    @GET
    public Response getProducts() {
        List<Product> result = productService.getProducts(shopId, branchId);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{product_id}")
    public Response getProduct(@PathParam("product_id") int productId) {
        Product product = productService.getProduct(shopId, branchId, productId);
        return Response.ok(product).build();
    }

    @GET
    @Path("/{product_id}/variants")
    public List<Variant> getVariants(@PathParam("product_id") int productId) {
        return productService.getVariants(shopId, branchId, productId);
    }

    @GET
    @Path("/{product_id}/variants/{variant_id}")
    public Variant getVariant(@PathParam("product_id") int productId, @PathParam("variant_id") int variantId) {
        return productService.getVariant(shopId, branchId, productId, variantId);
    }

    @POST
    public Response addProduct(AddProductRequest request) {
        Product result = productService.addProduct(shopId, branchId, request.getProduct(), request.getVariants());
        return Response.ok(result).build();
    }

    @POST
    @Path("/{product_id}/variants")
    public Response addVariant(@PathParam("product_id") int productId, RequestVariant requestVariant) {
        Variant result = productService.addVariant(shopId, branchId, productId, requestVariant);
        return Response.ok(result).build();
    }

    @PATCH
    @Path("/{product_id}")
    public Response updateProduct(@PathParam("product_id") int productId, RequestProduct requestProduct) {
        Product result = productService.updateProduct(shopId, branchId, productId, requestProduct);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/{product_id}")
    public Response deleteProduct(@PathParam("product_id") int productId) {
        boolean result = productService.deleteProduct(shopId, branchId, productId);
        if (result) return Response.status(Response.Status.NO_CONTENT).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @PATCH
    @Path("/{product_id}/variants/{variant_id}")
    public Response updateVariant(@PathParam("product_id") int productId, @PathParam("variant_id") int variantId, RequestVariant requestVariant) {
        Variant result = productService.updateVariant(shopId, branchId, productId, variantId, requestVariant);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/{product_id}/variants/{variant_id}")
    public Response deleteVariant(@PathParam("product_id") int productId, @PathParam("variant_id") int variantId) {
        boolean result = productService.deleteVariant(shopId, branchId, productId, variantId);
//        if (result) return Response.status(Response.Status.NO_CONTENT).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @Path("/flow")
    @GET
    public Response getProductFlow() {
        List<ProductFlowResponse> result = service.getProductFlow(shopId, branchId);
        return Response.ok(result).build();
    }

    @Path("{product_id}/flow/")
    @GET
    public Response getProductFlow(@PathParam("product_id") int product_id) {
        ProductFlowResponse result = service.getProductFlowByProductId(shopId, branchId, product_id);
        return Response.ok(result).build();
    }

    @Path("{product_id}/variants/{variant_id}/flow/")
    @GET
    public Response getProductFlow(@PathParam("product_id") int product_id, @PathParam("variant_id") int variant_id) {
        ProductFlowResponse result = service.getProductFlowByProductAndVariantId(shopId, branchId, product_id, variant_id);
        return Response.ok(result).build();
    }

    @Path("/low_stock/{threshold_value}")
    @GET
    public Response getProductLowStock(@PathParam("threshold_value") int thresholdValue) {
        List<LowStockProductResponse> result = service.getLowStockProducts(shopId, branchId, thresholdValue);
        return Response.ok(result).build();
    }

    @Path("/top_selling_product")
    @GET
    public Response getTopSellingProduct() {
        TopProductResponse result = service.getTopSellingProduct(shopId, branchId);
        return Response.ok(result).build();
    }

}
