package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.AllMerchantOrdersDTO;
import com.e_commerce.backend.dtos.MerchantDTO;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.service.IMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Tag(name = "Merchant", description = "Operations related to the Merchant")
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private IMerchantService merchantService;

    @PostMapping("/add-merchant")
    @Operation(summary = "Add merchant ", description = "Add merchant to the platform")
    public ResponseEntity<DefaultResponse> addMerchant(@Valid @RequestBody MerchantEntity merchant, BindingResult result) {
        DefaultResponse response;
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    error.put(err.getField(), err.getDefaultMessage())
            );
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Validation failed")
                    .errors(Optional.of(error))
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        log.info("Processing add merchant request for the merchant with name {}", merchant.getFirstName() + " " + merchant.getLastName());
        response = merchantService.addMerchant(merchant);

        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{companyName}")
    @Operation(summary = "Get merchant by company name")
    public ResponseEntity<MerchantDTO> getMerchant(
            @Parameter(description = "Get merchant by company name", example = "SK Enterprises") @PathVariable("companyName") String companyName
    ) {
        log.info("Processing get merchant by company name request for the merchant with company name {}", companyName);
        MerchantDTO response = merchantService.getMerchant(companyName);
        return response.getResponse().isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(response.getResponse().getHttpStatus().get()).body(response);
    }

    @GetMapping("/")
    @Operation(summary = "Testing endpoint")
    public String test() {
        return "Controller";
    }

    @GetMapping("/orders/all")
    @Operation(summary = "Get all orders of merchants")
    public ResponseEntity<AllMerchantOrdersDTO> getAllOrdersOfMerchant(@RequestHeader("Authorization") String authorizationHeader) {
        String token;

        token = Stream.of(authorizationHeader)
                .filter(Objects::nonNull)
                .filter(x -> x.startsWith("Bearer "))
                .map(x -> x.substring(7))
                .findFirst()
                .orElse("");

        DefaultResponse response;
        if (token.isEmpty()) {
            response = DefaultResponse.builder()
                    .success(false)
                    .message("Error in session please login")
                    .build();
            return ResponseEntity.badRequest().body(AllMerchantOrdersDTO.builder()
                    .response(response)
                    .build());
        }

        log.info("Processing get all orders of merchant");
        AllMerchantOrdersDTO allMerchantOrdersDTO = merchantService.getAllOrders(token);
        return allMerchantOrdersDTO.getResponse().isSuccess() ?
                ResponseEntity.ok(allMerchantOrdersDTO) :
                ResponseEntity.status(allMerchantOrdersDTO.getResponse().getHttpStatus().get()).body(allMerchantOrdersDTO);
    }
}
