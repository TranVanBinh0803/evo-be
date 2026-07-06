package com.binhtv.authservice.controller;

import com.binhtv.authservice.dto.ApiResponse;
import com.binhtv.authservice.dto.LoginRequest;
import com.binhtv.authservice.dto.LoginResponse;
import com.binhtv.authservice.security.jwt.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/auth/login"})
public class LoginController {

	private final JwtTokenService jwtTokenService;

	@PostMapping
	@Operation(tags = "Login Service", description = "You must log in with the correct information to successfully obtain the token information.")
	public ResponseEntity<ApiResponse<LoginResponse>> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {

		final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
		final ApiResponse<LoginResponse> response = new ApiResponse<>("Login successful!", HttpStatus.OK.value(), loginResponse);

		return ResponseEntity.ok(response);
	}

}
