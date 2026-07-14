package com.binhtv.productservice.model.mapper;

import com.binhtv.productservice.model.dto.BrandRequestDto;
import com.binhtv.productservice.model.dto.BrandResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public Brand toEntity(BrandRequestDto request) {
        return Brand.builder()
                .name(request.getName())
                .build();
    }

    public BrandResponseDto toDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}