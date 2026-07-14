package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(
            value = """
                    select p from Product p
                    join fetch p.brand
                    join fetch p.category
                    """,
            countQuery = """
                    select count(p) from Product p
                    """)
    Page<Product> findAllWithBrandAndCategory(Pageable pageable);

    @Query("""
            select p from Product p
            join fetch p.brand
            join fetch p.category
            where p.id = :id
            """)
    Optional<Product> findByIdWithBrandAndCategory(UUID id);
}