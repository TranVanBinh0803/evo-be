package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        final ProductResponseDto productResponse = productService.create(productRequestDto);
        final ApiResponse<ProductResponseDto> response = new ApiResponse<>(
                "Create product successful!",
                HttpStatus.CREATED.value(),
                productResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        final List<ProductResponseDto> products = productService.getProducts();
        final ApiResponse<List<ProductResponseDto>> response = new ApiResponse<>(
                "Get products successful!",
                HttpStatus.OK.value(),
                products);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable UUID productId) {
        final ProductResponseDto product = productService.getById(productId);
        final ApiResponse<ProductResponseDto> response = new ApiResponse<>(
                "Get product successful!",
                HttpStatus.OK.value(),
                product);

        return ResponseEntity.ok(response);
    }
}
