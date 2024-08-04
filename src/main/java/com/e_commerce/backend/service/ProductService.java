package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.enity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public DefaultResponse addProduct(ProductEntity productEntity, String email) {
        Optional<MerchantEntity> merchant = merchantRepository.findByEmail(email);
        if(!merchant.isPresent()){
            DefaultResponse errorResponse = DefaultResponse.builder()
                    .success(false)
                    .message("Merchant not found")
                    .build();
            return errorResponse;
        }
        MerchantEntity merchantEntity = merchant.get();
        productEntity.setMerchant(merchantEntity);
        productRepository.save(productEntity);
        DefaultResponse response = DefaultResponse.builder()
                .success(true)
                .message("Product added successfully")
                .build();
        return response;
    }
}