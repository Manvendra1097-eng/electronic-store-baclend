package com.m2code.repositories;

import com.m2code.entities.Cart;
import com.m2code.entities.CartItem;
import com.m2code.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByProductAndCart(Product product, Cart cart);

    List<CartItem> findByCart(Cart cart);
}
