package com.binhtv.authservice.security.service;

import com.binhtv.authservice.model.UserRole;
import com.binhtv.authservice.dto.AuthenticatedUserDto;
import com.binhtv.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String EMAIL_OR_PASSWORD_INVALID = "Invalid email or password.";

	private final UserService authService;

	@Override
	public UserDetails loadUserByUsername(String email) {

		final AuthenticatedUserDto authenticatedUser = authService.findAuthenticatedUserByEmail(email);

		if (Objects.isNull(authenticatedUser)) {
			throw new UsernameNotFoundException(EMAIL_OR_PASSWORD_INVALID);
		}

		final String authenticatedEmail = authenticatedUser.getEmail();
		final String authenticatedPassword = authenticatedUser.getPassword();
		final UserRole userRole = authenticatedUser.getUserRole();
		final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

		return new User(authenticatedEmail, authenticatedPassword, Collections.singletonList(grantedAuthority));
	}
}
