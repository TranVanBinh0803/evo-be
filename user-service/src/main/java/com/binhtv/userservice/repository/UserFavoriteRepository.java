package com.binhtv.userservice.repository;

import com.binhtv.userservice.model.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UUID> {
    List<UserFavorite> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
    Optional<UserFavorite> findByAccountIdAndProductId(UUID accountId, UUID productId);
}
