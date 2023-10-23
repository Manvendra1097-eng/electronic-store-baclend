package com.m2code.services;

import com.m2code.dtos.AddToCartRequest;
import com.m2code.dtos.CartDto;

import java.security.Principal;

public interface CartService {
    CartDto addToCart(String userId, AddToCartRequest request);

    CartDto getCart(String userId);

    void clearCart(String userId);

    CartDto removeFromCart(String userId, String productId);

    Integer getCartItemNumber(Principal principal);
}
