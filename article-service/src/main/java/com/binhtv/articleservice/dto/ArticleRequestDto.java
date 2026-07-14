package com.binhtv.articleservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleRequestDto {
    @NotBlank(message = "Title is required.")
    @Size(max = 180, message = "Title must not exceed 180 characters.")
    private String title;

    @NotBlank(message = "Slug is required.")
    @Size(max = 220, message = "Slug must not exceed 220 characters.")
    private String slug;

    @NotBlank(message = "Summary is required.")
    @Size(max = 500, message = "Summary must not exceed 500 characters.")
    private String summary;

    @NotBlank(message = "Content is required.")
    private String content;

    @NotBlank(message = "Image URL is required.")
    private String imageUrl;

    @NotBlank(message = "Author is required.")
    @Size(max = 120, message = "Author must not exceed 120 characters.")
    private String author;

    @NotBlank(message = "Tag is required.")
    @Size(max = 80, message = "Tag must not exceed 80 characters.")
    private String tag;

    @NotNull(message = "Featured flag is required.")
    private Boolean featured;

    @NotNull(message = "Read minutes is required.")
    @Min(value = 1, message = "Read minutes must be at least 1.")
    private Integer readMinutes;

    private LocalDateTime publishedAt;
}
