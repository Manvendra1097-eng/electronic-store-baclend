package com.m2code.repositories;

import com.m2code.entities.Category;
import com.m2code.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByTitleContaining(String keyword, Pageable pageable);


}
