package com.zs.stockmanagement.product.service;

import com.zs.stockmanagement.product.dao.ProductDAO;
import com.zs.stockmanagement.product.dto.RequestProduct;
import com.zs.stockmanagement.product.dto.RequestVariant;
import com.zs.stockmanagement.product.model.Product;
import com.zs.stockmanagement.product.model.Variant;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();


    public List<Product> getProducts(int shopId, int branchId) {
        return productDAO.getProducts(shopId, branchId);
    }

    public Product getProduct(int shopId, int branchId, int productId) {
        return productDAO.getProducts(shopId, branchId, productId);
    }

    public List<Variant> getVariants(int shopId, int branchId, int productId) {
        return productDAO.getVariants(shopId, branchId, productId);
    }

    public Variant getVariant(int shopId, int branchId, int productId, int variantId) {
        return productDAO.getVariants(shopId, branchId, productId, variantId);
    }


    public Product addProduct(int shopId, int branchId, RequestProduct requestProduct, List<RequestVariant> requestVariants) {

        if (requestProduct == null || requestVariants == null || requestVariants.isEmpty()) {
            return null;
        }
        Product product = new Product(requestProduct.getProductName(), requestProduct.getCategoryName(), requestProduct.getBrandName(), requestProduct.getModel());
        List<Variant> variants = new ArrayList<>();
        for (RequestVariant requestVariant : requestVariants) {
            variants.add(new Variant(requestVariant.getMrp(), requestVariant.getPrice(), requestVariant.getAttributes()));
        }
        Product addedProduct = productDAO.addProduct(shopId, branchId, product, variants);
        if (addedProduct == null) throw new RuntimeException("failed added process");
        return addedProduct;
    }

    public Variant addVariant(int shopId, int branchId, int productId, RequestVariant requestVariant) {

        if (requestVariant == null) {
            return null;
        }
        Variant variant = new Variant(requestVariant.getMrp(), requestVariant.getPrice(), requestVariant.getAttributes());
        Variant addedVariant = productDAO.addVariant(shopId, branchId, productId, variant);
        if (addedVariant == null) throw new RuntimeException("failed added process");
        return addedVariant;

    }

    public Product updateProduct(int shopId, int branchId, int productId, RequestProduct requestProduct) {

        if (requestProduct == null) {
            return null;
        }
        Product product = new Product(requestProduct.getProductName(), requestProduct.getCategoryName(), requestProduct.getBrandName(), requestProduct.getModel());
        Product updatedProduct = productDAO.updateProduct(shopId, branchId, productId, product);
        if (updatedProduct == null) throw new RuntimeException("failed updated process");
        return updatedProduct;
    }

    public Variant updateVariant(int shopId, int branchId, int productId, int variantId, RequestVariant requestVariant) {

        if (requestVariant == null) {
            return null;
        }
        Variant variant = new Variant(requestVariant.getMrp(), requestVariant.getPrice(), requestVariant.getAttributes());
        Variant updatedVariant = productDAO.updateVariant(shopId, branchId, productId, variantId, variant);
        if (updatedVariant == null) throw new RuntimeException("failed updated process");
        return updatedVariant;
    }

    public boolean deleteProduct(int shopId, int branchId, int productId) {
        return productDAO.deleteProduct(shopId, branchId, productId);
    }

    public boolean deleteVariant(int shopId, int branchId, int productId, int variantId) {

        return productDAO.deleteVariant(shopId, branchId, productId, variantId);
    }

}