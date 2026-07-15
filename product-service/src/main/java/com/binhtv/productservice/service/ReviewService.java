package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.PageResponse;
import com.binhtv.productservice.model.dto.ReviewRequestDto;
import com.binhtv.productservice.model.dto.ReviewResponseDto;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.model.entity.ProductReview;
import com.binhtv.productservice.reporsitory.ProductRepository;
import com.binhtv.productservice.reporsitory.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository reviewRepository;
    private final OrderPurchaseClient purchaseClient;

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponseDto> getReviews(UUID productId, Pageable pageable) {
        return PageResponse.from(reviewRepository.findByProductId(productId, pageable).map(this::toDto));
    }

    @Transactional
    public ReviewResponseDto create(UUID productId, UUID accountId, String reviewerName, ReviewRequestDto request) {
        if (reviewRepository.existsByProductIdAndAccountId(productId, accountId)) {
            throw new IllegalArgumentException("You have already reviewed this product.");
        }
        if (!purchaseClient.hasPurchased(accountId, productId)) {
            throw new IllegalArgumentException("Only verified buyers can review this product.");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setAccountId(accountId);
        review.setReviewerName(reviewerName);
        review.setRating(request.rating());
        review.setComment(request.comment());
        review = reviewRepository.save(review);
        refreshRating(product);
        return toDto(review);
    }

    @Transactional
    public ReviewResponseDto update(UUID productId, UUID accountId, ReviewRequestDto request) {
        ProductReview review = requireOwnReview(productId, accountId);
        review.setRating(request.rating());
        review.setComment(request.comment());
        refreshRating(review.getProduct());
        return toDto(review);
    }

    @Transactional
    public void delete(UUID productId, UUID accountId) {
        ProductReview review = requireOwnReview(productId, accountId);
        Product product = review.getProduct();
        reviewRepository.delete(review);
        reviewRepository.flush();
        refreshRating(product);
    }

    private ProductReview requireOwnReview(UUID productId, UUID accountId) {
        return reviewRepository.findByProductIdAndAccountId(productId, accountId)
                .orElseThrow(() -> new NoSuchElementException("Review not found."));
    }

    private void refreshRating(Product product) {
        product.setReviewCount(Math.toIntExact(reviewRepository.countByProductId(product.getId())));
        product.setAverageRating(reviewRepository.averageRating(product.getId()));
    }

    private ReviewResponseDto toDto(ProductReview review) {
        return new ReviewResponseDto(review.getId(), review.getProduct().getId(), review.getAccountId(),
                review.getReviewerName(), review.getRating(), review.getComment(), true,
                review.getCreatedAt(), review.getUpdatedAt());
    }
}
