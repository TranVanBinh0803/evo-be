package com.binhtv.sendsmsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "newsletter_subscribers")
@Getter @NoArgsConstructor
public class NewsletterSubscriber {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    private Instant subscribedAt;

    public NewsletterSubscriber(String email) {
        this.email = email.toLowerCase();
        this.subscribedAt = Instant.now();
    }
}
