package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.CategoryRequestDto;
import com.binhtv.productservice.model.dto.CategoryResponseDto;
import com.binhtv.productservice.model.dto.support.ApiResponses;
import com.binhtv.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponse = categoryService.create(categoryRequestDto);
        return ApiResponses.created("Create category successful!", categoryResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getCategories();
        return ApiResponses.ok("Get categories successful!", categories);
    }
}