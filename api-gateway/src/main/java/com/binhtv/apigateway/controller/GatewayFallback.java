package com.binhtv.apigateway.controller;

import com.binhtv.apigateway.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallbacks")
public class GatewayFallback {

    @GetMapping("/product")
    public ResponseEntity<ApiResponse<Void>> productFallback() {
        return fallbackResponse("Product service is unavailable. Please try later...");
    }

    @PostMapping("/product")
    public ResponseEntity<ApiResponse<Void>> productPostFallback() {
        return fallbackResponse("Product service is unavailable. Please try later...");
    }

    @GetMapping("/article")
    public ResponseEntity<ApiResponse<Void>> articleFallback() {
        return fallbackResponse("Article service is unavailable. Please try later...");
    }

    @PostMapping("/article")
    public ResponseEntity<ApiResponse<Void>> articlePostFallback() {
        return fallbackResponse("Article service is unavailable. Please try later...");
    }

    @GetMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadFallback() {
        return fallbackResponse("Upload service is unavailable. Please try later...");
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadPostFallback() {
        return fallbackResponse("Upload service is unavailable. Please try later...");
    }

    @GetMapping("/order")
    public ResponseEntity<ApiResponse<Void>> orderFallback() {
        return fallbackResponse("Order service is unavailable. Please try later...");
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse<Void>> orderPostFallback() {
        return fallbackResponse("Order service is unavailable. Please try later...");
    }

    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<Void>> authFallback() {
        return fallbackResponse("Auth service is unavailable. Please try later...");
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<Void>> authPostFallback() {
        return fallbackResponse("Auth service is unavailable. Please try later...");
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Void>> userFallback() {
        return fallbackResponse("User service is unavailable. Please try later...");
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<Void>> userPostFallback() {
        return fallbackResponse("User service is unavailable. Please try later...");
    }

    @GetMapping("/notification")
    public ResponseEntity<ApiResponse<Void>> notificationFallback() {
        return fallbackResponse("Notification service is unavailable. Please try later...");
    }

    @PostMapping("/notification")
    public ResponseEntity<ApiResponse<Void>> notificationPostFallback() {
        return fallbackResponse("Notification service is unavailable. Please try later...");
    }

    private ResponseEntity<ApiResponse<Void>> fallbackResponse(String message) {
        ApiResponse<Void> response = new ApiResponse<>(message, HttpStatus.SERVICE_UNAVAILABLE.value(), null);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
