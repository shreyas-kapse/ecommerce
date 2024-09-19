package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.MerchantEntity;

public interface IMerchantService {
    public DefaultResponse addMerchant(MerchantEntity merchant);
    public Object getMerchant(String companyName);
}
