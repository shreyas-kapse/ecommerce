package com.e_commerce.backend.service;

import com.e_commerce.backend.AccountStatus;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.EmailService;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.Role;
import com.e_commerce.backend.enity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService implements ILoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public DefaultResponse loginUser(UserEntity user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                Map<String, String> data = new HashMap<>();
                data.put("token", jwtService.generateToken(user.getUsername()));

                emailService.sendMail("test@yopmail.com","New login request","New login request detected","");
                return DefaultResponse.builder()
                        .success(true)
                        .data(Optional.of(data))
                        .build();
            }
        } catch (AuthenticationException ex) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }

        return DefaultResponse.builder()
                .success(false)
                .message("Error occurred in login")
                .build();
    }

    @Override
    public DefaultResponse registerUser(RegisterUserDTO registerUserDTO) {
        try {
            Optional<UserEntity> user = userRepository.findUserByUsername(registerUserDTO.getUsername());
            if (user.isPresent()) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("User already exits with this username")
                        .build();
            }
            UserEntity userEntity = UserEntity.builder()
                    .firstName(registerUserDTO.getFirstName())
                    .lastName(registerUserDTO.getLastName())
                    .accountStatus(AccountStatus.PENDING.name())
                    .addressLine1(registerUserDTO.getAddressLine1())
                    .addressLine2(registerUserDTO.getAddressLine2())
                    .city(registerUserDTO.getCity())
                    .postalCode(registerUserDTO.getPostalCode())
                    .country(registerUserDTO.getCountry())
                    .state(registerUserDTO.getState())
                    .isEmailVerified(false)
                    .phoneNumber(registerUserDTO.getPhoneNumber())
                    .username(registerUserDTO.getUsername())
                    .password(new BCryptPasswordEncoder(12).encode(registerUserDTO.getPassword()))
                    .role(Role.USER.name())
                    .build();
            userRepository.save(userEntity);

            return DefaultResponse.builder()
                    .success(true)
                    .message("User register successfully")
                    .build();

        } catch (Exception exception) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while registering the user")
                    .build();
        }
    }
}
