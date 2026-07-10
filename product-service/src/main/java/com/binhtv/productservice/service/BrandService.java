package com.binhtv.productservice.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.binhtv.productservice.model.dto.BrandRequestDto;
import com.binhtv.productservice.model.dto.BrandResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.reporsitory.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public BrandResponseDto getById(UUID id) {
        return brandRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Brand not found."));
    }

    public BrandResponseDto create(BrandRequestDto brandRequestDto) {
        Brand brand = Brand.builder()
                .name(brandRequestDto.getName())
                .build();

        Brand savedBrand = brandRepository.save(brand);
        return mapToDto(savedBrand);
    }

    private BrandResponseDto mapToDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}