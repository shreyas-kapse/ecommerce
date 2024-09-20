package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.ProductsDTO;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.enity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
            log.info("Processing add product request for merchant with id {}", merchantEntity.getId());

            productEntity.setMerchant(merchantEntity);
            productRepository.save(productEntity);

            log.info("Successfully processed add product request for merchant with id {}", merchantEntity.getId());
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product added successfully")
                    .build();
        } catch (Exception exception) {
            log.error("Error occurred while processing add product request with error {} ", exception.getMessage());
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while adding new product")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public ProductsDTO getProductsByMerchantName(String merchantName) {
        try {
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
            log.info("Processing get products by merchant name request for merchant with merchant name {}", merchantName);

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

            log.info("Successfully processed get products by merchant name request for merchant with merchant name {}", merchantName);
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(true)
                            .build())
                    .products(Optional.of(products.get()))
                    .totalProducts(Optional.of(products.get().size()))
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing get products by merchant name request with merchant name {} and error {}", merchantName, e.getMessage());
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .message("Error occurred while processing get products by merchant name")
                            .build())
                    .build();
        }
    }

    @Override
    public ProductsDTO getProductsByBrandName(String brandName) {
        try {
            Optional<List<ProductEntity>> products = productRepository.findAllByBrand(brandName);
            log.info("Processing get products by brand name request for brand name {}", brandName);

            if (products.get().isEmpty()) {
                return ProductsDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No products found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }
            log.info("Successfully processed get products by brand name request for brand name {}", brandName);

            return ProductsDTO.builder()
                    .totalProducts(Optional.of(products.get().size()))
                    .products(Optional.of(products.get()))
                    .response(DefaultResponse.builder()
                            .success(true)
                            .httpStatus(Optional.of(HttpStatus.OK))
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing get products by brand name request with brand name {} and error {}", brandName, e.getMessage());
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
            log.info("Processing get products by category name request for category name {}", categoryName);

            if (products.get().isEmpty()) {
                return ProductsDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No products found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }

            log.info("Successfully processed get products by category name request for category name {}", categoryName);
            return ProductsDTO.builder()
                    .totalProducts(Optional.of(products.get().size()))
                    .products(Optional.of(products.get()))
                    .response(DefaultResponse.builder()
                            .success(true)
                            .httpStatus(Optional.of(HttpStatus.OK))
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing get products by category name request with category name {} and error {}", categoryName, e.getMessage());
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
    public ProductsDTO getProductById(String id) {
        try {
            Optional<ProductEntity> product = productRepository.findById(Long.valueOf(id));
            log.info("Processing get product by id request with id {}", id);

            if (product.isEmpty()) {
                return ProductsDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No product found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }

            log.info("Successfully processed get product by id request with id {}", id);
            return ProductsDTO.builder()
                    .products(Optional.of(List.of(product.get())))
                    .response(DefaultResponse.builder()
                            .success(true)
                            .httpStatus(Optional.of(HttpStatus.OK))
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing get product by id with id {} and error {}", id, e.getMessage());
            return ProductsDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error occurred while fetching the record")
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }
}