package com.binhtv.authservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

	@NotEmpty(message = "Email can not be empty!")
	private String email;

	@NotEmpty(message = "Password can not be empty!")
	private String password;

}
