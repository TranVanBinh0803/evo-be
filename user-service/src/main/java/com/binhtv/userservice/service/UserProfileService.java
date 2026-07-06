package com.binhtv.userservice.service;

import com.binhtv.userservice.dto.UserRegisteredEvent;
import com.binhtv.userservice.model.UserProfile;
import com.binhtv.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

	private final UserProfileRepository userProfileRepository;

	@Transactional
	public void createFrom(UserRegisteredEvent event) {

		if (userProfileRepository.existsByAccountId(event.getAccountId())) {
			log.info("User profile already exists for account {}", event.getAccountId());
			return;
		}

		final UserProfile userProfile = new UserProfile(event.getAccountId(), event.getEmail(), event.getUsername());
		userProfileRepository.save(userProfile);
		log.info("Created user profile for account {}", event.getAccountId());
	}

	public List<UserProfile> findAll() {

		return userProfileRepository.findAll();
	}

	public UserProfile findByAccountId(UUID accountId) {

		return userProfileRepository.findByAccountId(accountId).orElse(null);
	}
}
