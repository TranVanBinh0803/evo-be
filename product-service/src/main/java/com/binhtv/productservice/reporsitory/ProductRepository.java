package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
                select p from Product p
                join fetch p.brand
                join fetch p.category
            """)
    List<Product> findAllWithBrandAndCategory();

    @Query("""
                select p from Product p
                join fetch p.brand
                join fetch p.category
                where p.id = :id
            """)
    Optional<Product> findByIdWithBrandAndCategory(UUID id);
}
