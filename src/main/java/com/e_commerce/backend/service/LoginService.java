package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    @Override
    public DefaultResponse loginUser(UserEntity user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                Map<String, String> data = new HashMap<>();
                data.put("token", jwtService.generateToken(user.getUsername()));

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
}
