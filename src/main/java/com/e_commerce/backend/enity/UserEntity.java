package com.e_commerce.backend.enity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    private Long id;

    private String username;

    private String password;

    private String role;
}
