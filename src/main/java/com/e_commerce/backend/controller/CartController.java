package com.e_commerce.backend.controller;

import com.e_commerce.backend.dtos.CartDTOResponse;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Tag(name = "Cart ", description = "Operations related to cart")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/add")
    @Operation(summary = "Add product to the cart")
    public ResponseEntity<DefaultResponse> addProductToCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId, @RequestParam int quantity) {
        String token;
        DefaultResponse response;

        log.info("Processing add product request for product id {}",productId);

        token = Stream.of(authorizationHeader)
                .filter(Objects::nonNull)
                .filter(x -> x.startsWith("Bearer "))
                .map(x -> x.substring(7))
                .findFirst()
                .orElse("");

        if (token.isEmpty()) {
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        response = cartService.addProductToCart(productId, quantity, token);

        return !response.isSuccess() ? ResponseEntity.status(response.getHttpStatus().get()).body(response) : ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Delete product from cart", description = "Delete product from cart by product id")
    public ResponseEntity<DefaultResponse> removeProductFromCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId) {
        String token;
        DefaultResponse response;

        log.info("Processing remove product request for product id {}",productId);

        token = Stream.of(authorizationHeader)
                .filter(Objects::nonNull)
                .filter(x -> x.startsWith("Bearer "))
                .map(x -> x.substring(7))
                .findFirst()
                .orElse("");

        if (token.isEmpty()) {
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }

        response = cartService.removeProductFromCart(productId, token);

        return !response.isSuccess() ? ResponseEntity.status(response.getHttpStatus().get()).body(response) : ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Remove all products from cart")
    public ResponseEntity<DefaultResponse> clearCart(@RequestHeader("Authorization") String authorizationHeader) {
        String token;
        DefaultResponse response;

        log.info("Processing clear cart request ");

        token = Stream.of(authorizationHeader)
                .filter(Objects::nonNull)
                .filter(x -> x.startsWith("Bearer "))
                .map(x -> x.substring(7))
                .findFirst()
                .orElse("");

        if (token.isEmpty()) {
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.internalServerError().body(response);
        }
        response = cartService.clearCart(token);

        return !response.isSuccess() ? ResponseEntity.status(response.getHttpStatus().get()).body(response) : ResponseEntity.ok(response);

    }

    @GetMapping
    @Operation(summary = "Get cart items", description = "Get all cart items")
    public ResponseEntity<CartDTOResponse> getCart(@RequestHeader("Authorization") String authorizationHeader) {
        String token;

        log.info("Processing get cart items request ");

        token = Stream.of(authorizationHeader)
                .filter(Objects::nonNull)
                .filter(x -> x.startsWith("Bearer "))
                .map(x -> x.substring(7))
                .findFirst()
                .orElse("");

        CartDTOResponse cartDTOResponse;
        if (token.isEmpty()) {
            cartDTOResponse = CartDTOResponse.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error in session please login")
                            .build())
                    .build();
            return ResponseEntity.internalServerError().body(cartDTOResponse);
        }
        cartDTOResponse = cartService.getCart(token);

        return !cartDTOResponse.getResponse().isSuccess() ? ResponseEntity.status(cartDTOResponse.getResponse().getHttpStatus().get()).body(cartDTOResponse) : ResponseEntity.ok(cartDTOResponse);
    }
}
