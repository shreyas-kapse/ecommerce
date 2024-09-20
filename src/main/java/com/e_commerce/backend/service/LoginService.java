package com.e_commerce.backend.service;

import com.e_commerce.backend.AccountStatus;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.EmailService;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.Role;
import com.e_commerce.backend.dtos.LoginDTO;
import com.e_commerce.backend.dtos.RegisterUserDTO;
import com.e_commerce.backend.enity.UserEntity;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public DefaultResponse loginUser(LoginDTO user) {
        try {
            log.info("Fetching user details for login");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                Map<String, String> data = new HashMap<>();
                data.put("token", jwtService.generateToken(user.getUsername()));

                String name = jwtService.extractFirstAndLastName(data.get("token"));
                String email = jwtService.extractUserName(data.get("token"));

                emailService.loginMail(email, "New login request", "New login request detected", "admin@ecommerce.com", name);
                log.info("Successfully processed login request for {}",user.getUsername());

                return DefaultResponse.builder()
                        .success(true)
                        .data(Optional.of(data))
                        .build();
            }
        } catch (AuthenticationException ex) {
            log.error("Error occurred while processing login request for {} user with error {}",user.getUsername(),ex.getMessage());
            return DefaultResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }
        log.error("Error occurred while processing login request for {} user",user.getUsername());
        return DefaultResponse.builder()
                .success(false)
                .message("Error occurred in login")
                .build();
    }

    @Override
    public DefaultResponse registerUser(RegisterUserDTO registerUserDTO) {
        try {
            log.info("Processing register user request");
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

            log.info("Successfully processed register user request for {}",registerUserDTO.getUsername());

            return DefaultResponse.builder()
                    .success(true)
                    .message("User register successfully")
                    .build();

        } catch (Exception exception) {
            log.error("Error occurred while processing register user request for {} user with error {}",registerUserDTO.getUsername(), exception.getMessage());
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while registering the user")
                    .build();
        }
    }
}
