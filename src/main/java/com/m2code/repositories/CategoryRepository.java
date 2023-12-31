package com.m2code.repositories;

import com.m2code.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findAll(Pageable pageable);
}
