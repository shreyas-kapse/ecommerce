package com.e_commerce.backend.controller;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Tag(name = "Order", description = "Operations related to the order")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/place")
    @Operation(summary = "Place order")
    public ResponseEntity<DefaultResponse> placeOrder(@RequestHeader("Authorization") String authorizationHeader) {
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
            return ResponseEntity.internalServerError().body(response);
        }

        log.info("Processing place order request");
        response = orderService.placeOrder(token);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(response.getHttpStatus().get()).body(response);
    }
}
