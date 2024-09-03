package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.ProductsDTO;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.enity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public DefaultResponse addProduct(ProductEntity productEntity, String token) {
        try {
            String email = jwtService.extractUserName(token);
            Optional<MerchantEntity> merchant = merchantRepository.findByEmail(email);
            if (merchant.isEmpty()) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("Merchant not found")
                        .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                        .build();
            }
            MerchantEntity merchantEntity = merchant.get();
            productEntity.setMerchant(merchantEntity);
            productRepository.save(productEntity);
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product added successfully")
                    .build();
        } catch (Exception exception) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while adding new product")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public ProductsDTO getProductsByMerchantName(String merchantName) {
        Optional<MerchantEntity> merchant = merchantRepository.findByCompanyName(merchantName);
        if (merchant.isEmpty()) {
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("No company found")
                            .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                            .build())
                    .build();
        }
        Optional<List<ProductEntity>> products = productRepository.findAllByMerchantId(merchant.get().getId());
        if (products.isEmpty()) {
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                            .message("No products found")
                            .build())
                    .build();
        }
        return ProductsDTO.builder()
                .response(DefaultResponse.builder()
                        .success(true)
                        .build())
                .products(Optional.of(products.get()))
                .totalProducts(Optional.of(products.get().size()))
                .build();
    }

    @Override
    public ProductsDTO getProductsByBrandName(String brandName) {
        try {
            Optional<List<ProductEntity>> products = productRepository.findAllByBrand(brandName);
            if (products.get().isEmpty()) {
                return ProductsDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No products found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }
            return ProductsDTO.builder()
                    .totalProducts(Optional.of(products.get().size()))
                    .products(Optional.of(products.get()))
                    .response(DefaultResponse.builder()
                            .success(true)
                            .httpStatus(Optional.of(HttpStatus.OK))
                            .build())
                    .build();
        } catch (Exception e) {
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error occurred while fetching the records")
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }

    @Override
    public ProductsDTO getProductsByCategoryName(String categoryName) {
        try {
            Optional<List<ProductEntity>> products = productRepository.findAllByCategory(categoryName);
            if (products.get().isEmpty()) {
                return ProductsDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No products found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }
            return ProductsDTO.builder()
                    .totalProducts(Optional.of(products.get().size()))
                    .products(Optional.of(products.get()))
                    .response(DefaultResponse.builder()
                            .success(true)
                            .httpStatus(Optional.of(HttpStatus.OK))
                            .build())
                    .build();
        } catch (Exception e) {
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error occurred while fetching the records")
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }
}