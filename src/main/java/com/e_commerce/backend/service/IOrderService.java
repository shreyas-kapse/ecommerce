package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;

public interface IOrderService {
    DefaultResponse placeOrder(String token);
}