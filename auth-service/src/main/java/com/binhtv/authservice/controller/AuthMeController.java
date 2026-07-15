package com.binhtv.authservice.controller;

import com.binhtv.authservice.dto.ApiResponse;
import com.binhtv.authservice.dto.AuthenticatedUserDto;
import com.binhtv.authservice.dto.LoginUserResponseDto;
import com.binhtv.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthMeController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginUserResponseDto>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByEmail(email);
        LoginUserResponseDto userResponse = new LoginUserResponseDto(
                authenticatedUser.getId(),
                authenticatedUser.getUsername(),
                authenticatedUser.getEmail(),
                authenticatedUser.getUserRole());
        ApiResponse<LoginUserResponseDto> response = new ApiResponse<>(
                "Get current user successful!",
                HttpStatus.OK.value(),
                userResponse);

        return ResponseEntity.ok(response);
    }
}
