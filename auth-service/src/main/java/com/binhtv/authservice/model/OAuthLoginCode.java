package com.binhtv.authservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "oauth_login_codes")
@Getter
@Setter
@NoArgsConstructor
public class OAuthLoginCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String codeHash;
    private Instant expiresAt;
    private Instant usedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public OAuthLoginCode(String codeHash, Instant expiresAt, User user) {
        this.codeHash = codeHash;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
