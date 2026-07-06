package com.binhtv.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest {

	@Email(message = "Please enter a valid email!")
	@NotEmpty(message = "Email can not be empty!")
	private String email;

	@NotEmpty(message = "Username can not be empty!")
	private String username;

	@NotEmpty(message = "Password can not be empty!")
	private String password;

}
