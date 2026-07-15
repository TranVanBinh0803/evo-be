package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.model.mapper.ProductMapper;
import com.binhtv.productservice.reporsitory.ProductRepository;
import com.binhtv.productservice.reporsitory.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRelationResolver relationResolver;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductResponseDto getById(UUID id) {
        return productRepository.findByIdWithBrandAndCategory(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Brand brand = relationResolver.requireBrand(productRequestDto.getBrandId());
        Category category = relationResolver.requireCategory(productRequestDto.getCategoryId());
        Product product = productMapper.toEntity(productRequestDto, brand, category);

        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(String query, UUID categoryId, List<UUID> brandIds,
            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        List<UUID> normalizedBrandIds = brandIds == null || brandIds.isEmpty() ? null : brandIds;
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price must not exceed maximum price.");
        }
        return productRepository.findAll(
                        ProductSpecifications.withFilters(
                                normalizedQuery, categoryId, normalizedBrandIds, minPrice, maxPrice),
                        pageable)
                .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return productRepository.findAllByIdsWithBrandAndCategory(ids).stream().map(productMapper::toDto).toList();
    }
}
