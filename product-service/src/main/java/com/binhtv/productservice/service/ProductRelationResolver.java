package com.binhtv.productservice.service;

import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.reporsitory.BrandRepository;
import com.binhtv.productservice.reporsitory.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRelationResolver {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public Brand requireBrand(UUID brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("Brand not found."));
    }

    public Category requireCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found."));
    }
}