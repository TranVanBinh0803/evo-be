package com.binhtv.productservice.reporsitory;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binhtv.productservice.model.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

}
