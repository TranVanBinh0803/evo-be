package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.dto.PageResponse;
import com.binhtv.productservice.model.dto.support.ApiResponses;
import com.binhtv.productservice.model.enums.ProductSortField;
import com.binhtv.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private static final int MAX_PAGE_SIZE = 100;

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponse = productService.create(productRequestDto);
        return ApiResponses.created("Create product successful!", productResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponseDto>>> getAllProducts(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) List<UUID> brandIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        PageResponse<ProductResponseDto> products = PageResponse.from(
                productService.getProducts(query, categoryId, brandIds, minPrice, maxPrice, pageable));

        return ApiResponses.ok("Get products successful!", products);
    }

    @GetMapping("/batch")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByIds(@RequestParam List<UUID> ids) {
        return ApiResponses.ok("Get products successful!", productService.getByIds(ids));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable UUID productId) {
        ProductResponseDto product = productService.getById(productId);
        return ApiResponses.ok("Get product successful!", product);
    }

    private Pageable buildPageable(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative.");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and %d.".formatted(MAX_PAGE_SIZE));
        }

        ProductSortField sortField = ProductSortField.from(sortBy);
        return PageRequest.of(page, size, Sort.by(direction, sortField.getProperty()));
    }
}
