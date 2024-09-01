package com.e_commerce.backend.enity;

import com.e_commerce.backend.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "user name is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    private String role;

    private String firstName;

    private String lastName;

    @Builder.Default
    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;

    private String accountStatus;

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
