package com.binhtv.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "USER_FAVORITES", uniqueConstraints =
        @UniqueConstraint(name = "uk_user_favorite_product", columnNames = {"accountId", "productId"}))
@Getter
@NoArgsConstructor
public class UserFavorite {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID accountId;
    private UUID productId;
    private Instant createdAt;

    public UserFavorite(UUID accountId, UUID productId) {
        this.accountId = accountId;
        this.productId = productId;
        this.createdAt = Instant.now();
    }
}
