package com.e_commerce.backend.dtos;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.MerchantEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MerchantDTO {
    private DefaultResponse response;
    private Optional<MerchantEntity> merchant;
}
