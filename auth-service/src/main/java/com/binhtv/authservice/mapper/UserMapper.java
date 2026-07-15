package com.binhtv.authservice.mapper;

import com.binhtv.authservice.model.User;
import com.binhtv.authservice.dto.AuthenticatedUserDto;
import com.binhtv.authservice.dto.RegistrationRequest;

public final class UserMapper {

	public static final UserMapper INSTANCE = new UserMapper();

	private UserMapper() {
	}

	public User convertToUser(RegistrationRequest registrationRequest) {

		if (registrationRequest == null) {
			return null;
		}

		final User user = new User();
		user.setEmail(registrationRequest.getEmail());
		user.setUsername(registrationRequest.getUsername());
		user.setPassword(registrationRequest.getPassword());

		return user;
	}

	public AuthenticatedUserDto convertToAuthenticatedUserDto(User user) {

		if (user == null) {
			return null;
		}

		final AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
		authenticatedUserDto.setId(user.getId());
		authenticatedUserDto.setUsername(user.getUsername());
		authenticatedUserDto.setEmail(user.getEmail());
		authenticatedUserDto.setPassword(user.getPassword());
		authenticatedUserDto.setUserRole(user.getUserRole());

		return authenticatedUserDto;
	}

	public User convertToUser(AuthenticatedUserDto authenticatedUserDto) {

		if (authenticatedUserDto == null) {
			return null;
		}

		final User user = new User();
		user.setUsername(authenticatedUserDto.getUsername());
		user.setEmail(authenticatedUserDto.getEmail());
		user.setPassword(authenticatedUserDto.getPassword());
		user.setUserRole(authenticatedUserDto.getUserRole());

		return user;
	}

}
