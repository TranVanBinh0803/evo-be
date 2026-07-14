package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.CategoryRequestDto;
import com.binhtv.productservice.model.dto.CategoryResponseDto;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.mapper.CategoryMapper;
import com.binhtv.productservice.reporsitory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toEntity(categoryRequestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }
}