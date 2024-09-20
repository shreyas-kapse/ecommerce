package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.ProductsDTO;
import com.e_commerce.backend.enity.ProductEntity;
import com.e_commerce.backend.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Tag(name = "Product ", description = "Operations related to the product")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/add")
    @Operation(summary = "Add product", description = "Add product on the platform / List product on the platform")
    public ResponseEntity<DefaultResponse> addProduct(@Valid @RequestBody ProductEntity productEntity, BindingResult result, @RequestHeader("Authorization") String authorizationHeader) {
        DefaultResponse response;
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    error.put(err.getField(), err.getDefaultMessage())
            );
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Validation failed")
                    .errors(Optional.of(error))
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        String token = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        if (token.isEmpty()) {
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }
        log.info("Processing add product request for product with name {} and brand name {}", productEntity.getProductName(), productEntity.getBrand());
        response = productService.addProduct(productEntity, token);

        return !response.isSuccess() ? ResponseEntity.status(response.getHttpStatus().get()).body(response) : ResponseEntity.ok(response);
    }

    @GetMapping("/merchant/{merchantName}")
    @Operation(summary = "Get merchant by merhcant name")
    public ResponseEntity<ProductsDTO> getProductsByMerchantName(
            @Parameter(description = "Merchant Name") @PathVariable("merchantName") String merchantName
    ) {
        log.info("Processing get merchant by merchant name request for the merchant with name {}", merchantName);
        ProductsDTO productsDTO = productService.getProductsByMerchantName(merchantName);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get products by brand name", description = "List all products of given brand")
    public ResponseEntity<ProductsDTO> getProductsByBrandName(
            @Parameter(description = "Brand name") @PathVariable("brand") String brand
    ) {
        log.info("Processing get products by brand name request for {} brand", brand);
        ProductsDTO productsDTO = productService.getProductsByBrandName(brand);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/category/{categoryName}")
    @Operation(summary = "Get products by category name", description = "List all products of given category")
    public ResponseEntity<ProductsDTO> getProductsByCategoryName(
            @Parameter(description ="Category Name") @PathVariable("categoryName") String categoryName
    ) {
        log.info("Processing get products by category name request for {} category", categoryName);
        ProductsDTO productsDTO = productService.getProductsByCategoryName(categoryName);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public ResponseEntity<ProductsDTO> getProductsById(
            @Parameter(description = "Product Id") @PathVariable("id") String id
    ) {
        log.info("Processing get product by product id {}", id);
        ProductsDTO productsDTO = productService.getProductById(id);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }

}
