package com.binhtv.authservice.repository;

import com.binhtv.authservice.model.AuthProvider;
import com.binhtv.authservice.model.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, UUID> {
    Optional<OAuthAccount> findByProviderAndProviderSubject(AuthProvider provider, String providerSubject);
}
