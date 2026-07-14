package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.BrandRequestDto;
import com.binhtv.productservice.model.dto.BrandResponseDto;
import com.binhtv.productservice.model.dto.support.ApiResponses;
import com.binhtv.productservice.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        BrandResponseDto brandResponse = brandService.create(brandRequestDto);
        return ApiResponses.created("Create brand successful!", brandResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDto>>> getAllBrands() {
        List<BrandResponseDto> brands = brandService.getBrands();
        return ApiResponses.ok("Get brands successful!", brands);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(@PathVariable UUID brandId) {
        BrandResponseDto brand = brandService.getById(brandId);
        return ApiResponses.ok("Get brand successful!", brand);
    }
}