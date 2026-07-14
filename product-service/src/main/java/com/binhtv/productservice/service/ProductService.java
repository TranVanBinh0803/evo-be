package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.model.mapper.ProductMapper;
import com.binhtv.productservice.reporsitory.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

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
    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        return productRepository.findAllWithBrandAndCategory(pageable)
                .map(productMapper::toDto);
    }
}