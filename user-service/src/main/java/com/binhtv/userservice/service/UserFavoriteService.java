package com.binhtv.userservice.service;

import com.binhtv.userservice.model.UserFavorite;
import com.binhtv.userservice.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {
    private final UserFavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public List<UUID> getProductIds(UUID accountId) {
        return favoriteRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(UserFavorite::getProductId).toList();
    }

    @Transactional
    public void add(UUID accountId, UUID productId) {
        if (favoriteRepository.findByAccountIdAndProductId(accountId, productId).isEmpty()) {
            favoriteRepository.save(new UserFavorite(accountId, productId));
        }
    }

    @Transactional
    public void remove(UUID accountId, UUID productId) {
        favoriteRepository.findByAccountIdAndProductId(accountId, productId).ifPresent(favoriteRepository::delete);
    }
}
