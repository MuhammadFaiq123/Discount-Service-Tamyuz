package com.assignment.tamyuz.discount.service.persistence.repository;

import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> filter(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null)
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));

            if (minPrice != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));

            if (maxPrice != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

