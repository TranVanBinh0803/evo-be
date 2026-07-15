package com.binhtv.authservice.dto;

import com.binhtv.authservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginUserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private UserRole userRole;
}
