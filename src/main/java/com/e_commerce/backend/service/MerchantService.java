package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.enity.MerchantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantService implements IMerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public DefaultResponse addMerchant(MerchantEntity merchant) {
        if(merchantRepository.findByMerchantPhoneNo(merchant.getMerchantPhoneNo()).isPresent()){
            return DefaultResponse.builder()
                    .success(false)
                    .message("Phone number already exists")
                    .build();
        }
        if(merchantRepository.findByEmail(merchant.getEmail()).isPresent()){
            return DefaultResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }
        merchantRepository.save(merchant);
        return DefaultResponse.builder()
                .success(true)
                .message("Merchant added successfully")
                .build();
    }

    @Override
    public Object getMerchant(String email, String companyName) {
        Optional<MerchantEntity> merchant = merchantRepository.findByEmail(email);
        if(merchant.isPresent() && merchant.get().getCompanyName().equalsIgnoreCase(companyName)){
            return merchant;
        }
        return DefaultResponse.builder().message("No merchant found").build();
    }
}
