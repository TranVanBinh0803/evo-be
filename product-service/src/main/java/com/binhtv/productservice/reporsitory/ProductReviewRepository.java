package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID> {
    Page<ProductReview> findByProductId(UUID productId, Pageable pageable);
    Optional<ProductReview> findByProductIdAndAccountId(UUID productId, UUID accountId);
    boolean existsByProductIdAndAccountId(UUID productId, UUID accountId);
    long countByProductId(UUID productId);

    @Query("select coalesce(avg(r.rating), 0) from ProductReview r where r.product.id = :productId")
    Double averageRating(UUID productId);
}
