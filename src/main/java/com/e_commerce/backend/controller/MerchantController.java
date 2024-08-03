package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.service.IMerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private IMerchantService merchantService;
    @PostMapping("/add-merchant")
    public ResponseEntity<DefaultResponse> addMerchant(@Valid @RequestBody MerchantEntity merchant, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> error = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    error.put(err.getField(), err.getDefaultMessage())
            );
            DefaultResponse errorResponse = DefaultResponse.builder()
                    .success(false)
                    .message("Validation failed")
                    .errors(Optional.of(error))
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(merchantService.addMerchant(merchant));
    }

}
