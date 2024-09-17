package com.e_commerce.backend.controller;

import com.e_commerce.backend.CartDTOResponse;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<DefaultResponse> addProductToCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId, @RequestParam int quantity) {
        String token;
        DefaultResponse response;
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
    public ResponseEntity<DefaultResponse> removeProductFromCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long productId) {
        String token;
        DefaultResponse response;
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

        return !response.isSuccess()? ResponseEntity.status(response.getHttpStatus().get()).body(response): ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<DefaultResponse> clearCart(@RequestHeader("Authorization") String authorizationHeader) {
        String token;
        DefaultResponse response;
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
    public ResponseEntity<CartDTOResponse> getCart(@RequestHeader("Authorization") String authorizationHeader) {
        String token;

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
