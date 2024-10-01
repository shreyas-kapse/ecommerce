package com.e_commerce.backend.dtos;

import com.e_commerce.backend.DefaultResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class AllMerchantOrdersDTO {

    private DefaultResponse response;
    private Optional<List<AllOrdersOfMerchant>> allOrders;
}
