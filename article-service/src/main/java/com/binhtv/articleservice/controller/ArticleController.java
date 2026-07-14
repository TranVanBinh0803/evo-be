package com.binhtv.articleservice.controller;

import com.binhtv.articleservice.dto.ApiResponse;
import com.binhtv.articleservice.dto.ArticleRequestDto;
import com.binhtv.articleservice.dto.ArticleResponseDto;
import com.binhtv.articleservice.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ArticleResponseDto>>> getArticles() {
        List<ArticleResponseDto> articles = articleService.getArticles();
        ApiResponse<List<ArticleResponseDto>> response = new ApiResponse<>(
                "Get articles successful!",
                HttpStatus.OK.value(),
                articles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ArticleResponseDto>>> getFeaturedArticles() {
        List<ArticleResponseDto> articles = articleService.getFeaturedArticles();
        ApiResponse<List<ArticleResponseDto>> response = new ApiResponse<>(
                "Get featured articles successful!",
                HttpStatus.OK.value(),
                articles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleResponseDto>> getArticleById(@PathVariable UUID articleId) {
        ArticleResponseDto article = articleService.getById(articleId);
        ApiResponse<ArticleResponseDto> response = new ApiResponse<>(
                "Get article successful!",
                HttpStatus.OK.value(),
                article);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ArticleResponseDto>> getArticleBySlug(@PathVariable String slug) {
        ArticleResponseDto article = articleService.getBySlug(slug);
        ApiResponse<ArticleResponseDto> response = new ApiResponse<>(
                "Get article successful!",
                HttpStatus.OK.value(),
                article);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponseDto>> createArticle(
            @Valid @RequestBody ArticleRequestDto articleRequestDto) {
        ArticleResponseDto article = articleService.create(articleRequestDto);
        ApiResponse<ArticleResponseDto> response = new ApiResponse<>(
                "Create article successful!",
                HttpStatus.CREATED.value(),
                article);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
