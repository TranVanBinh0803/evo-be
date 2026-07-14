package com.binhtv.productservice.model.mapper;

import com.binhtv.productservice.model.dto.CategoryRequestDto;
import com.binhtv.productservice.model.dto.CategoryResponseDto;
import com.binhtv.productservice.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequestDto request) {
        return Category.builder()
                .name(request.getName())
                .imgUrl(request.getImgUrl())
                .build();
    }

    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .build();
    }
}