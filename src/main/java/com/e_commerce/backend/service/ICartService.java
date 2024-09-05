package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;

public interface ICartService {
    public DefaultResponse addProductToCart(Long productId, int quantity, String token);
}