package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.AllMerchantOrdersDTO;
import com.e_commerce.backend.dtos.MerchantDTO;
import com.e_commerce.backend.enity.MerchantEntity;

public interface IMerchantService {
    public DefaultResponse addMerchant(MerchantEntity merchant);
    public MerchantDTO getMerchant(String companyName);
    public AllMerchantOrdersDTO getAllOrders(String token);
}
