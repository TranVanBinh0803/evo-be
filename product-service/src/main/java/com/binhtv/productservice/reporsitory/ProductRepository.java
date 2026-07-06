package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.Product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
