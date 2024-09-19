package com.e_commerce.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDTO {
    @NotBlank(message = "user name is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Invalid number")
    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City name is required")
    private String city;

    @NotBlank(message = "State name is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country name is required")
    private String country;
}
