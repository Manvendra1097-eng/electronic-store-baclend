package com.m2code.services.impl;

import com.m2code.dtos.AddToCartRequest;
import com.m2code.dtos.CartDto;
import com.m2code.entities.Cart;
import com.m2code.entities.CartItem;
import com.m2code.entities.Product;
import com.m2code.entities.User;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.repositories.CartItemRepository;
import com.m2code.repositories.CartRepository;
import com.m2code.repositories.ProductRepository;
import com.m2code.repositories.UserRepository;
import com.m2code.services.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDto addToCart(String userId, AddToCartRequest request) {
        User user = getUserBYId(userId);
        String productId = request.getProductId();
        int quantity = request.getQuantity();
        Cart cart = getOrCreateCartForUser(user);
        Product product = getProductById(productId);
        CartItem cartItem = cartItemRepository.findByProductAndCart(product, cart).orElse(null);
        if (cartItem == null) {
            cartItem = CartItem.builder().cart(cart).product(product).quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice()).build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cartItem.getTotalPrice() + quantity * product.getDiscountedPrice());
        }
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        Cart savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart, CartDto.class);
    }

    private Product getProductById(String productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not registed"));
    }

    @Override
    public CartDto getCart(String userId) {
        User user = getUserBYId(userId);
        Cart cart = getOrCreateCartForUser(user);
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public void clearCart(String userId) {
        User user = getUserBYId(userId);
        Cart cart = getOrCreateCartForUser(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto removeFromCart(String userId, String productId) {
        User user = getUserBYId(userId);
        Cart cart = getOrCreateCartForUser(user);
        Product product = getProductById(productId);
        CartItem cartItem = cartItemRepository.findByProductAndCart(product, cart).orElse(null);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }

        Cart dbCart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart is not " +
                "available "));
        return modelMapper.map(dbCart, CartDto.class);
    }

    @Override
    public Integer getCartItemNumber(Principal principal) {
        String email = principal.getName();
        User user = getUserBYEmail(email);
        Integer length = 0;
        Cart cart = user.getCart();
        if (!Objects.isNull(cart)) {
            List<CartItem> cartItems = user.getCart().getCartItems();
            if (!Objects.isNull(cartItems))
                length = cartItems.size();
        }
        return length;
    }

    private User getUserBYId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not available for id: " + userId));
    }

    private User getUserBYEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User is not available " +
                "for id: " + email));
    }


    private Cart getOrCreateCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            cart = Cart.builder().cartId(UUID.randomUUID().toString().replace("-", "")).user(user).build();
            cart = cartRepository.save(cart);
        }
        return cart;
    }
}
