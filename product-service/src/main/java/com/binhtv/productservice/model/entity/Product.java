package com.binhtv.productservice.model.entity;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "products")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private BigDecimal price;
    private String imgUrl;
    private Integer discount;
    private Integer quantity;
    private String description;
    private String origin;

    @Column(nullable = false)
    private Double averageRating;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private Long soldCount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "fk_products_brand"))
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_products_category"))
    private Category category;

    @PrePersist
    void prePersist() {
        if (origin == null || origin.isBlank()) origin = "Việt Nam";
        if (averageRating == null) averageRating = 0D;
        if (reviewCount == null) reviewCount = 0;
        if (soldCount == null) soldCount = 0L;
        if (createdAt == null) createdAt = Instant.now();
    }
}
