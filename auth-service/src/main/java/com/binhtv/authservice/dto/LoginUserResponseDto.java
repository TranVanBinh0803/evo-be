package com.binhtv.authservice.dto;

import com.binhtv.authservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserResponseDto {
    private String username;
    private String email;
    private UserRole userRole;
}