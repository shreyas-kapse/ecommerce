package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.MerchantEntity;
import com.e_commerce.backend.service.IMerchantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        response = merchantService.addMerchant(merchant);

        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{companyName}")
    public ResponseEntity<Object> getMerchant(@RequestParam(required = false) String email, @PathVariable String companyName) {
        Object object = merchantService.getMerchant(email, companyName);
        if (object.getClass() == DefaultResponse.class) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(object);
        }
        return ResponseEntity.ok(object);
    }

    @GetMapping("/")
    public String test() {
        return "Controller";
    }
}
