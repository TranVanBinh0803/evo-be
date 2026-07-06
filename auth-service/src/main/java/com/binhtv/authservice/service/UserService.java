package com.binhtv.authservice.service;

import com.binhtv.authservice.event.UserRegisteredEvent;
import com.binhtv.authservice.event.UserRegisteredProducer;
import com.binhtv.authservice.model.User;
import com.binhtv.authservice.model.UserRole;
import com.binhtv.authservice.repository.UserRepository;
import com.binhtv.authservice.dto.AuthenticatedUserDto;
import com.binhtv.authservice.dto.RegistrationRequest;
import com.binhtv.authservice.exceptions.RegistrationException;
import com.binhtv.authservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private static final String REGISTRATION_SUCCESSFUL = "Registered successfully!";
	private static final String EMAIL_ALREADY_EXISTS = "This email address is already being used!";

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserRegisteredProducer userRegisteredProducer;

	public User findByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	public String registration(RegistrationRequest registrationRequest) {

		validateUser(registrationRequest);

		final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUserRole(UserRole.USER);

		final User savedUser = userRepository.save(user);

		final String email = registrationRequest.getEmail();

		userRegisteredProducer.publish(new UserRegisteredEvent(savedUser.getId(), email, savedUser.getUsername()));

		log.info("{} registered successfully!", email);

		return REGISTRATION_SUCCESSFUL;
	}

	public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {

		final User user = findByEmail(email);

		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}

	public void validateUser(RegistrationRequest registrationRequest) {

		final String email = registrationRequest.getEmail();

		checkEmail(email);
	}

	private void checkEmail(String email) {

		final boolean existsByEmail = userRepository.existsByEmail(email);

		if (existsByEmail) {

			log.warn("Email: {} already being used!", email);

			throw new RegistrationException(EMAIL_ALREADY_EXISTS);
		}
	}
}
