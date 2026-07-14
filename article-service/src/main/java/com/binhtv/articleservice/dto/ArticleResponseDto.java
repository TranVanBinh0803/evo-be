package com.binhtv.articleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ArticleResponseDto {
    private UUID id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String imageUrl;
    private String author;
    private String tag;
    private Boolean featured;
    private Integer readMinutes;
    private LocalDateTime publishedAt;
}
