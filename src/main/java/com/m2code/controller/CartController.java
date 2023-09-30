package com.m2code.controller;

import com.m2code.dtos.AddToCartRequest;
import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.CartDto;
import com.m2code.dtos.ProductDto;
import com.m2code.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{userId}")
    @Operation(summary = "add products to cart")
    public ResponseEntity<ApiResponseMessage<String>> addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest, @PathVariable String userId) {
        ProductDto productDto = cartService.addToCart(userId, addToCartRequest);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Product " + productDto.getTitle() + " with quantity " + addToCartRequest.getQuantity() + " is added to cart successfully").success(true).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "get cart details")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCart(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/{userId}/clear")
    @Operation(summary = "clear cart for user")
    public ResponseEntity<ApiResponseMessage<String>> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Cart is cleared successfully").success(true).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/product/{productId}")
    @Operation(summary = "remove products from cart")
    public ResponseEntity<ApiResponseMessage<String>> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        cartService.removeFromCart(userId, productId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Product is removed successfully").success(true).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

}
