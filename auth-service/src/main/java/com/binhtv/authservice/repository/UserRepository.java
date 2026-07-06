package com.binhtv.authservice.repository;

import com.binhtv.authservice.model.User;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

	User findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
