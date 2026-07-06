package com.binhtv.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "USER_PROFILES")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true)
	private UUID accountId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String username;

	public UserProfile(UUID accountId, String email, String username) {
		this.accountId = accountId;
		this.email = email;
		this.username = username;
	}
}
