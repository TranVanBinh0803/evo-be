package com.binhtv.apigateway.controller;

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
    public ResponseEntity<String> productFallback() {
        return fallbackResponse("Product service is unavailable. Please try later...");
    }

    @PostMapping("/product")
    public ResponseEntity<String> productPostFallback() {
        return fallbackResponse("Product service is unavailable. Please try later...");
    }

    @GetMapping("/upload")
    public ResponseEntity<String> uploadFallback() {
        return fallbackResponse("Upload service is unavailable. Please try later...");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPostFallback() {
        return fallbackResponse("Upload service is unavailable. Please try later...");
    }

    @GetMapping("/order")
    public ResponseEntity<String> orderFallback() {
        return fallbackResponse("Order service is unavailable. Please try later...");
    }

    @PostMapping("/order")
    public ResponseEntity<String> orderPostFallback() {
        return fallbackResponse("Order service is unavailable. Please try later...");
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authFallback() {
        return fallbackResponse("Auth service is unavailable. Please try later...");
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authPostFallback() {
        return fallbackResponse("Auth service is unavailable. Please try later...");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userFallback() {
        return fallbackResponse("User service is unavailable. Please try later...");
    }

    @PostMapping("/user")
    public ResponseEntity<String> userPostFallback() {
        return fallbackResponse("User service is unavailable. Please try later...");
    }

    private ResponseEntity<String> fallbackResponse(String message) {
        return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}