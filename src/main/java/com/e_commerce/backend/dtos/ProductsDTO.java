package com.e_commerce.backend.dtos;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductsDTO {
    private DefaultResponse response;
    private Optional<Integer> totalProducts;
    private Optional<List<ProductEntity>> products;
}
