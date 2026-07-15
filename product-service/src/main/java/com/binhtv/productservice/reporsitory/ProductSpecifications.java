package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class ProductSpecifications {
    private ProductSpecifications() {
    }

    public static Specification<Product> withFilters(
            String query,
            UUID categoryId,
            List<UUID> brandIds,
            BigDecimal minPrice,
            BigDecimal maxPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null) {
                String pattern = "%" + query.toLowerCase(Locale.ROOT) + "%";
                Join<Product, Brand> brand = root.join("brand");
                Join<Product, Category> category = root.join("category");
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(brand.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(category.get("name")), pattern)));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (brandIds != null) {
                predicates.add(root.get("brand").get("id").in(brandIds));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
