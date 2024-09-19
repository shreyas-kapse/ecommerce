package com.e_commerce.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "user name is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;
}