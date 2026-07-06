package com.binhtv.userservice.repository;

import com.binhtv.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

	boolean existsByAccountId(UUID accountId);

	Optional<UserProfile> findByAccountId(UUID accountId);
}
