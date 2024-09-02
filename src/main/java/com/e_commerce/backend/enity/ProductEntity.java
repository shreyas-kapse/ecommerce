package com.e_commerce.backend.enity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Product name can not be blank")
    @Size(max = 15, min = 3, message = "Product name can not be less than 3 and more than 15 characters")
    private String productName;

    @NotNull(message = "Product price can not be null")
    @Digits(integer = 7, fraction = 2)
    @Min(value = 0, message = "Stock quantity can not be less than zero")
    private Long price;

    @NotNull(message = "Product quantity can not be null")
    @Digits(integer = 7, fraction = 0)
    private Long quantity;

    @NotBlank(message = "Product category can not be blank")
    @Size(min = 3, max = 15, message = "Product name can not be more than 15 characters")
    private String category;

    @NotBlank(message = "Brand name can not be blank")
    @Size(max = 15, min = 3, message = "Brand name can not be more than 15 characters")
    private String brand;

    @NotNull(message = "Available status is required")
    private Boolean available;

    @ManyToOne
    @JsonBackReference
    private MerchantEntity merchant;
}