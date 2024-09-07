package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;

public interface ICartService {
    DefaultResponse addProductToCart(Long productId, int quantity, String token);

    DefaultResponse removeProductFromCart(Long productId, String token);

    DefaultResponse clearCart(String token);
}