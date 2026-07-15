package com.binhtv.sendsmsservice.repository;

import com.binhtv.sendsmsservice.model.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, UUID> {
    boolean existsByEmailIgnoreCase(String email);
}
