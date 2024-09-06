package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<DefaultResponse> addProductToCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId, @RequestParam int quantity) {
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
        DefaultResponse response = cartService.addProductToCart(productId, quantity, token);
        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus().get()).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<DefaultResponse> removeProductFromCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId) {
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

        DefaultResponse response = cartService.removeProductFromCart(productId, token);
        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus().get()).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
