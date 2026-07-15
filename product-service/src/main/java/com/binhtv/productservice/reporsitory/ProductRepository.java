package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p join fetch p.brand join fetch p.category where p.id in :ids")
    List<Product> findAllByIdForUpdate(List<UUID> ids);
    @Override
    @EntityGraph(attributePaths = {"brand", "category"})
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    @Query("""
            select p from Product p
            join fetch p.brand
            join fetch p.category
            where p.id in :ids
            """)
    List<Product> findAllByIdsWithBrandAndCategory(List<UUID> ids);

    @Query("""
            select p from Product p
            join fetch p.brand
            join fetch p.category
            where p.id = :id
            """)
    Optional<Product> findByIdWithBrandAndCategory(UUID id);
}
