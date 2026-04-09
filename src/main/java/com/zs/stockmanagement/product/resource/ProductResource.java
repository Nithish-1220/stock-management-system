package com.zs.stockmanagement.product.resource;

import com.zs.stockmanagement.product.dto.AddProductRequest;
import com.zs.stockmanagement.product.dto.RequestProduct;
import com.zs.stockmanagement.product.dto.RequestVariant;
import com.zs.stockmanagement.product.model.Product;
import com.zs.stockmanagement.product.model.Variant;
import com.zs.stockmanagement.product.service.ProductService;
import com.zs.stockmanagement.product.dto.LowStockProductResponse;
import com.zs.stockmanagement.product.dto.ProductFlowResponse;
import com.zs.stockmanagement.product.dto.TopProductResponse;
import com.zs.stockmanagement.product.service.ProductFlowService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final ProductService productService;
    private final int shopId;
    private final int branchId;

    public ProductResource(int shopId, int branchId) {
        productService = new ProductService();
        this.shopId=shopId;
        this.branchId = branchId;
    }

    @GET
    public List<Product> getProducts(){
        return productService.getProducts(shopId,branchId);
    }

    @GET
    @Path("/{product_id}")
    public Product getProduct(@PathParam("product_id") int productId){
        return productService.getProduct(shopId,branchId,productId);
    }

    @GET
    @Path("/{product_id}/variants")
    public List<Variant> getVariants(@PathParam("product_id") int productId){
        return productService.getVariants(shopId,branchId,productId);
    }

    @GET
    @Path("/{product_id}/variants/{variant_id}")
    public Variant getVariant(@PathParam("product_id") int productId,@PathParam("variant_id") int variantId){
        return productService.getVariant(shopId,branchId,productId,variantId);
    }

    @POST
    public Product addProduct(AddProductRequest request){
        return productService.addProduct(shopId,branchId,request.getProduct(),request.getVariants());
    }

    @POST
    @Path("/{product_id}/variants")
    public Variant addVariant(@PathParam("product_id") int productId,RequestVariant requestVariant){
        return productService.addVariant(shopId,branchId,productId,requestVariant);
    }

    @PATCH
    @Path("/{product_id}")
    public Product updateProduct(@PathParam("product_id") int productId,RequestProduct requestProduct){
        return productService.updateProduct(shopId,branchId,productId,requestProduct);
    }

    @PATCH
    @Path("/{product_id}/variants/{variant_id}")
    public Variant updateVariant(@PathParam("product_id") int productId,@PathParam("variant_id") int variantId,RequestVariant requestVariant){
        return productService.updateVariant(shopId,branchId,productId,variantId,requestVariant);
    }

    @Path("/flow")
    @GET
    public List<ProductFlowResponse> getProductFlow() {
        ProductFlowService service = new ProductFlowService();
        return service.getProductFlow(shopId, branchId);
    }

    @Path("{product_id}/flow/")
    @GET
    public ProductFlowResponse getProductFlow(@PathParam("product_id") int product_id ) {
        ProductFlowService service = new ProductFlowService();
        return service.getProductFlowByProductId(shopId, branchId,product_id);
    }

    @Path("{product_id}/variants/{variant_id}/flow/")
    @GET
    public ProductFlowResponse getProductFlow(@PathParam("product_id") int product_id,@PathParam("variant_id")int variant_id ) {
        ProductFlowService service = new ProductFlowService();
        return service.getProductFlowByProductAndVariantId(shopId, branchId,product_id,variant_id);
    }

    @Path("/low_stock/{threshold_value}")
    @GET
    public List<LowStockProductResponse> getProductLowStock(@PathParam("threshold_value") int thresholdValue) {
        ProductFlowService service = new ProductFlowService();
        return service.getLowStockProducts(shopId, branchId,thresholdValue);
    }

    @Path("/top_selling_product")
    @GET
    public TopProductResponse getTopSellingProduct() {
        ProductFlowService service = new ProductFlowService();
        return service.getTopSellingProduct(shopId, branchId);
    }

}
