package com.binhtv.authservice.dto;

import com.binhtv.authservice.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

	private String username;

	private String email;

	private String password;

	private UserRole userRole;

}
