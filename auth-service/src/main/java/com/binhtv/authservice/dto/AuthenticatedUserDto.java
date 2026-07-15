package com.binhtv.authservice.dto;

import com.binhtv.authservice.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {
	private UUID id;

	private String username;

	private String email;

	private String password;

	private UserRole userRole;

}
