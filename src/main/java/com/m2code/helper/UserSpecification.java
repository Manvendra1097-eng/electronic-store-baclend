package com.m2code.helper;

import com.m2code.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> nameUserSpecification(String name) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("name")), "%" + name.toLowerCase() + "%"
        );
    }
}
