package com.binhtv.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.binhtv.productservice.model.dto.CategoryRequestDto;
import com.binhtv.productservice.model.dto.CategoryResponseDto;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.reporsitory.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .imgUrl(categoryRequestDto.getImgUrl())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    private CategoryResponseDto mapToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .build();
    }
}