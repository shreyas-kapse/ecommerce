package com.e_commerce.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DefaultResponse {
    private boolean success;
    private String message;

    @Builder.Default
    private Optional<Map<String, String>> errors = Optional.empty();

    @Builder.Default
    private Optional<Map<String, String>> data = Optional.empty();

    private Optional<HttpStatus> httpStatus;
}
