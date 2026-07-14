package com.binhtv.articleservice.repository;

import com.binhtv.articleservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findAllByOrderByPublishedAtDesc();

    List<Article> findByFeaturedTrueOrderByPublishedAtDesc();

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
