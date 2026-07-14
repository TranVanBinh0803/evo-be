package com.binhtv.articleservice.service;

import com.binhtv.articleservice.dto.ArticleRequestDto;
import com.binhtv.articleservice.dto.ArticleResponseDto;
import com.binhtv.articleservice.model.Article;
import com.binhtv.articleservice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<ArticleResponseDto> getArticles() {
        return articleRepository.findAllByOrderByPublishedAtDesc().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ArticleResponseDto> getFeaturedArticles() {
        return articleRepository.findByFeaturedTrueOrderByPublishedAtDesc().stream()
                .map(this::mapToDto)
                .toList();
    }

    public ArticleResponseDto getById(UUID id) {
        return articleRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Article not found."));
    }

    public ArticleResponseDto getBySlug(String slug) {
        return articleRepository.findBySlug(slug)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Article not found."));
    }

    public ArticleResponseDto create(ArticleRequestDto request) {
        if (articleRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Article slug already exists.");
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(request.getAuthor())
                .tag(request.getTag())
                .featured(request.getFeatured())
                .readMinutes(request.getReadMinutes())
                .publishedAt(request.getPublishedAt())
                .build();

        return mapToDto(articleRepository.save(article));
    }

    private ArticleResponseDto mapToDto(Article article) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .summary(article.getSummary())
                .content(article.getContent())
                .imageUrl(article.getImageUrl())
                .author(article.getAuthor())
                .tag(article.getTag())
                .featured(article.getFeatured())
                .readMinutes(article.getReadMinutes())
                .publishedAt(article.getPublishedAt())
                .build();
    }
}
