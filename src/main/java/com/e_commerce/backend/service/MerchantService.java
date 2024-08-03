package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.MerchantEntity;
import org.springframework.stereotype.Service;

@Service
public class MerchantService implements IMerchantService {
    @Override
    public DefaultResponse addMerchant(MerchantEntity merchant) {
         return DefaultResponse.builder()
                .success(true)
                .message("Operation successful")
                .build();
    }
}
