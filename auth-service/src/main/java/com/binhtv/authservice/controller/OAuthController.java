package com.binhtv.authservice.controller;

import com.binhtv.authservice.dto.ApiResponse;
import com.binhtv.authservice.dto.LoginResponse;
import com.binhtv.authservice.dto.OAuthExchangeRequest;
import com.binhtv.authservice.service.OAuthLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthLoginService loginService;

    @PostMapping("/exchange")
    public ResponseEntity<ApiResponse<LoginResponse>> exchange(@Valid @RequestBody OAuthExchangeRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                "OAuth login successful!", HttpStatus.OK.value(), loginService.exchange(request.code())));
    }
}
