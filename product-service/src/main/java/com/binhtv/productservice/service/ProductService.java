package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.dto.ProductStockItemRequest;
import com.binhtv.productservice.model.dto.ProductStockRequestDto;
import com.binhtv.productservice.model.dto.ProductStockResponse;
import com.binhtv.productservice.model.dto.ProductStockResponseDto;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public ProductStockResponseDto checkProductsStock(ProductStockRequestDto request) {
        Map<UUID, Integer> requestedQuantities = request.getItems().stream()
                .collect(Collectors.toMap(
                        ProductStockItemRequest::getProductId,
                        ProductStockItemRequest::getQuantity,
                        Math::addExact,
                        LinkedHashMap::new));

        Map<UUID, Product> productsById = productRepository.findAllById(requestedQuantities.keySet()).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<ProductStockResponse> stockResponses = requestedQuantities.entrySet().stream()
                .map(entry -> toStockResponse(
                        entry.getKey(),
                        entry.getValue(),
                        productsById.get(entry.getKey())))
                .toList();

        return ProductStockResponseDto.builder()
                .allInStock(stockResponses.stream().allMatch(ProductStockResponse::isInStock))
                .productStockResponses(stockResponses)
                .build();
    }

    private ProductStockResponse toStockResponse(UUID productId, int requestedQuantity, Product product) {
        int availableQuantity = product == null || product.getQuantity() == null
                ? 0
                : product.getQuantity();

        return ProductStockResponse.builder()
                .productId(productId)
                .requestedQuantity(requestedQuantity)
                .availableQuantity(availableQuantity)
                .inStock(product != null && availableQuantity >= requestedQuantity)
                .build();
    }
}
