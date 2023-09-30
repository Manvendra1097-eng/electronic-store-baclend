package com.m2code.services;

import com.m2code.dtos.AddToCartRequest;
import com.m2code.dtos.CartDto;
import com.m2code.dtos.ProductDto;

public interface CartService {
    ProductDto addToCart(String userId, AddToCartRequest request);

    CartDto getCart(String userId);

    void clearCart(String userId);

    void removeFromCart(String userId, String productId);
}
