package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.ProductsDTO;
import com.e_commerce.backend.enity.ProductEntity;

public interface IProductService {
    public DefaultResponse addProduct(ProductEntity productEntity, String token);

    ProductsDTO getProductsByMerchantName(String companyName);
}