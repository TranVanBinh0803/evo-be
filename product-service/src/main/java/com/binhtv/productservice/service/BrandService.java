package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.BrandRequestDto;
import com.binhtv.productservice.model.dto.BrandResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.mapper.BrandMapper;
import com.binhtv.productservice.reporsitory.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Transactional(readOnly = true)
    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BrandResponseDto getById(UUID id) {
        return brandRepository.findById(id)
                .map(brandMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Brand not found."));
    }

    @Transactional
    public BrandResponseDto create(BrandRequestDto brandRequestDto) {
        Brand brand = brandMapper.toEntity(brandRequestDto);
        return brandMapper.toDto(brandRepository.save(brand));
    }
}