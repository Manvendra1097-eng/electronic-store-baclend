package com.m2code.repositories;

import com.m2code.entities.Cart;
import com.m2code.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
