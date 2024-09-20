package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.LoginDTO;
import com.e_commerce.backend.dtos.RegisterUserDTO;
import com.e_commerce.backend.service.ILoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Tag(name = "Authentication", description = "Authentication APIs")
@RestController
public class Login {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "Login to platform", description = "Login API")
    public ResponseEntity<DefaultResponse> login(@RequestBody LoginDTO user) {
        log.info("Processing login request for the {}",user.getUsername());
        DefaultResponse response = loginService.loginUser(user);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register on platform", description = "Register API")
    public ResponseEntity<DefaultResponse> register(@Valid @RequestBody RegisterUserDTO registerUserDTO, BindingResult result) {
        DefaultResponse response;
        log.info("Processing register user request for the {}",registerUserDTO.getUsername());
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(err -> error.put(err.getField(), err.getDefaultMessage()));
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Validation failed")
                    .errors(Optional.of(error))
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        response = loginService.registerUser(registerUserDTO);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
}
