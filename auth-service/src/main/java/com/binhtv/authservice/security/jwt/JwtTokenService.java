package com.binhtv.authservice.security.jwt;

import com.binhtv.authservice.mapper.UserMapper;
import com.binhtv.authservice.service.UserService;
import com.binhtv.authservice.model.User;
import com.binhtv.authservice.dto.AuthenticatedUserDto;
import com.binhtv.authservice.dto.LoginRequest;
import com.binhtv.authservice.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final UserService authService;

	private final JwtTokenManager jwtTokenManager;

	private final AuthenticationManager authenticationManager;

	public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		final String email = loginRequest.getEmail();
		final String password = loginRequest.getPassword();

		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

		authenticationManager.authenticate(authenticationToken);

		final AuthenticatedUserDto authenticatedUserDto = authService.findAuthenticatedUserByEmail(email);

		final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
		final String token = jwtTokenManager.generateToken(user);

		log.info("{} has successfully logged in!", user.getEmail());

		return new LoginResponse(token);
	}

}
