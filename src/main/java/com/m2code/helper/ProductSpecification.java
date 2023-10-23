package com.m2code.helper;

import com.m2code.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> titleProductSpecification(String keyword) {
        return ((root, query, criteriaBuilder) -> {

            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("category.title"), "%" + keyword + "%")
            );

        });
    }
}
