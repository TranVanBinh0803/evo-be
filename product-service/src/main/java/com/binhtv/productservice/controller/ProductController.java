package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @RequestBody ProductRequestDto productRequestDto) {
        final ProductResponseDto productResponse = productService.create(productRequestDto);
        final ApiResponse<ProductResponseDto> response = new ApiResponse<>("Create product successful!",
                HttpStatus.CREATED.value(), productResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // @GetMapping
    // public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
    //     return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    // }

    // @GetMapping("/{productId}")
    // public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
    //     return new ResponseEntity<>(productService.getById(productId), HttpStatus.OK);
    // }
}
