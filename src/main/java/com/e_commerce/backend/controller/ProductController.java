package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.ProductsDTO;
import com.e_commerce.backend.enity.ProductEntity;
import com.e_commerce.backend.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/add")
    public ResponseEntity<DefaultResponse> addProduct(@Valid @RequestBody ProductEntity productEntity, BindingResult result, @RequestHeader("Authorization") String authorizationHeader) {
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    error.put(err.getField(), err.getDefaultMessage())
            );
            DefaultResponse errorResponse = DefaultResponse.builder()
                    .success(false)
                    .message("Validation failed")
                    .errors(Optional.of(error))
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String token = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        if (token.isEmpty()) {
            DefaultResponse errorResponse = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.internalServerError().body(errorResponse);
        }

        DefaultResponse response = productService.addProduct(productEntity, token);

        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus().get()).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/merchant/{merchantName}")
    public ResponseEntity<ProductsDTO> getProductsByMerchantName(@PathVariable String merchantName) {
        ProductsDTO productsDTO = productService.getProductsByMerchantName(merchantName);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<ProductsDTO> getProductsByBrandName(@PathVariable String brand) {
        ProductsDTO productsDTO = productService.getProductsByBrandName(brand);
        if (!productsDTO.getResponse().isSuccess()) {
            return ResponseEntity.status(productsDTO.getResponse().getHttpStatus().get()).body(productsDTO);
        }
        return ResponseEntity.ok(productsDTO);
    }
}
