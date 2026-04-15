package com.zs.stockmanagement.productHelper.service;

import com.zs.stockmanagement.productHelper.dao.ProductHelper;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.Map;

public class ProductHelperService {

    private final ProductHelper productHelper = new ProductHelper();

    public Map<Integer,String> getAttributes(){
        return productHelper.getAttributes();
    }

    public Map<String,String> getAttributesAndValues(int variantId){
        return productHelper.getAttributesAndValues(variantId);
    }

}
