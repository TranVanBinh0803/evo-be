package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.BrandRequestDto;
import com.binhtv.productservice.model.dto.BrandResponseDto;
import com.binhtv.productservice.service.BrandService;
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
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDto>> createBrand(
            @Valid @RequestBody BrandRequestDto brandRequestDto) {
        final BrandResponseDto brandResponse = brandService.create(brandRequestDto);
        final ApiResponse<BrandResponseDto> response = new ApiResponse<>(
                "Create brand successful!",
                HttpStatus.CREATED.value(),
                brandResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDto>>> getAllBrands() {
        final List<BrandResponseDto> brands = brandService.getBrands();
        final ApiResponse<List<BrandResponseDto>> response = new ApiResponse<>(
                "Get brands successful!",
                HttpStatus.OK.value(),
                brands);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(@PathVariable UUID brandId) {
        final BrandResponseDto brand = brandService.getById(brandId);
        final ApiResponse<BrandResponseDto> response = new ApiResponse<>(
                "Get brand successful!",
                HttpStatus.OK.value(),
                brand);

        return ResponseEntity.ok(response);
    }
}