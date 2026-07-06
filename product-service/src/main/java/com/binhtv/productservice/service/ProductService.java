package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDto getById(UUID id);
    ProductResponseDto create(ProductRequestDto productRequestDto);
    List<ProductResponseDto> getProducts();
}

