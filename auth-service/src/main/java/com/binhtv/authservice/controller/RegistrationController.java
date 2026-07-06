package com.binhtv.authservice.controller;

import com.binhtv.authservice.dto.ApiResponse;
import com.binhtv.authservice.dto.RegistrationRequest;
import com.binhtv.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/auth/register"})
public class RegistrationController {

	private final UserService authService;

	@PostMapping
	@Operation(tags = "Register Service", description = "You can register to the system by sending information in the appropriate format.")
	public ResponseEntity<ApiResponse<Void>> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {

		final String message = authService.registration(registrationRequest);
		final ApiResponse<Void> response = new ApiResponse<>(message, HttpStatus.CREATED.value(), null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
