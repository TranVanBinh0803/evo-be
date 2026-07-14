package com.binhtv.productservice.model.dto.support;

import com.binhtv.productservice.model.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResponses {
    private ApiResponses() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return build(message, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return build(message, HttpStatus.CREATED, data);
    }

    private static <T> ResponseEntity<ApiResponse<T>> build(String message, HttpStatus status, T data) {
        ApiResponse<T> response = new ApiResponse<>(message, status.value(), data);
        return ResponseEntity.status(status).body(response);
    }
}