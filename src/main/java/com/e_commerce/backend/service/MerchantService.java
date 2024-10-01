package com.e_commerce.backend.service;

import com.e_commerce.backend.AccountStatus;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.EmailService;
import com.e_commerce.backend.Repository.MerchantRepository;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.Role;
import com.e_commerce.backend.dtos.AllMerchantOrdersDTO;
import com.e_commerce.backend.dtos.AllOrdersOfMerchant;
import com.e_commerce.backend.dtos.MerchantDTO;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.enity.UserEntity;
import com.e_commerce.backend.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MerchantService implements IMerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Override
    public DefaultResponse addMerchant(MerchantEntity merchant) {
        try {
            String merchantName = merchant.getFirstName() + " " + merchant.getLastName();
            log.info("Processing add merchant request for merhchant with name {} and company name {}", merchantName, merchant.getCompanyName());
            if (merchantRepository.findByMerchantPhoneNo(merchant.getMerchantPhoneNo()).isPresent()) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("Phone number already exists")
                        .build();
            }
            if (merchantRepository.findByEmail(merchant.getEmail()).isPresent()) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("Email already exists")
                        .build();
            }
            merchant.setAccountStatus(AccountStatus.PENDING.name());
            merchantRepository.save(merchant);

            String password = passwordGenerator.generatePassword(12);
            emailService.merchantAccountCreationMail(merchant.getEmail(),
                    "Your Merchant Account is Ready - Login Details Enclosed",
                    "admin@ecommerce.com",
                    merchant.getFirstName() + " " + merchant.getLastName(),
                    merchant.getEmail(),
                    password);

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
                    .password(new BCryptPasswordEncoder(12).encode(password))
                    .accountStatus(AccountStatus.PENDING.name())
                    .role(Role.MERCHANT.name())
                    .build();
            userRepository.save(user);

            log.info("Successfully processed add merchant request for merchant with name {}", merchantName);

            return DefaultResponse.builder()
                    .success(true)
                    .message("Merchant added successfully, Login credentials have been sent via email.")
                    .build();
        } catch (Exception exception) {
            log.error("Error occurred while processing add merchant request with error {} ", exception.getMessage());
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while processing add merchant request")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public MerchantDTO getMerchant(String companyName) {
        try {
            log.info("Processing get merchant request for company name {}", companyName);
            Optional<MerchantEntity> merchant = merchantRepository.findByCompanyName(companyName);

            if (merchant.isPresent() && merchant.get().getCompanyName().equalsIgnoreCase(companyName)) {
                log.info("Successfully processed get merchant request for merchant with company name {}", companyName);
                return MerchantDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(true)
                                .build())
                        .merchant(merchant)
                        .build();
            }
            return MerchantDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("No merchant found")
                            .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                            .build())
                    .build();
        } catch (Exception exception) {
            log.error("Error occurred while processing get merchant request with error {} ", exception.getMessage());
            return MerchantDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error occurred while fetching merchant")
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }


    @Override
    public AllMerchantOrdersDTO getAllOrders(String token) {
        try {
            Object merchantId = jwtService.extractId(token);
            if (merchantId == null) {
                return AllMerchantOrdersDTO.builder()
                        .response(DefaultResponse.builder()
                                .success(false)
                                .message("No merchant found")
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            }
            log.info("Processing get all orders of merchant with id {} ", merchantId.toString());
            List<AllOrdersOfMerchant> allOrdersOfMerchants = merchantRepository.findAllByMerchantId(Long.parseLong(merchantId.toString())).get();
            log.info("Successfully processed get all orders of merchant request with merchant id {}", merchantId);

            return AllMerchantOrdersDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(true)
                            .build())
                    .allOrders(Optional.of(allOrdersOfMerchants))
                    .build();
        } catch (Exception exception) {
            log.error("Error occurred while processing get all orders of merchant request with error {} ", exception.getMessage());
            return AllMerchantOrdersDTO.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }


}
