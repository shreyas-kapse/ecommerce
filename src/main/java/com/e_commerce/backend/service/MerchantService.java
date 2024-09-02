package com.e_commerce.backend.service;

import com.e_commerce.backend.AccountStatus;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.Role;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.enity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantService implements IMerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

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
        merchant.setAccountStatus(AccountStatus.PENDING.name());
        merchantRepository.save(merchant);

        UserEntity user = UserEntity.builder()
                .username(merchant.getEmail())
                .firstName(merchant.getFirstName())
                .lastName(merchant.getLastName())
                .addressLine1(merchant.getOfficeAddress())
                .city(merchant.getCity())
                .country(merchant.getCountry())
                .state(merchant.getState())
                .phoneNumber(merchant.getMerchantPhoneNo().toString())
                .isEmailVerified(false)
                .postalCode(merchant.getPinCode().toString())
                .password(new BCryptPasswordEncoder(12).encode("abc"))
                .accountStatus(AccountStatus.PENDING.name())
                .role(Role.MERCHANT.name())
                .build();
        userRepository.save(user);

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
