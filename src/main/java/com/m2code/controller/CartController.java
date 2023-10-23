package com.m2code.controller;

import com.m2code.dtos.AddToCartRequest;
import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.CartDto;
import com.m2code.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{userId}")
    @Operation(summary = "add products to cart")
    public ResponseEntity<CartDto> addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest,
                                             @PathVariable String userId) {
        CartDto cartDto = cartService.addToCart(userId, addToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
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
    public ResponseEntity<CartDto> removeFromCart(@PathVariable String userId,
                                                  @PathVariable String productId) {
        CartDto cartDto = cartService.removeFromCart(userId, productId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }


    @GetMapping("cartlength")
    @Operation(summary = "get number of cart items")
    public ResponseEntity<ApiResponseMessage<Integer>> getCartLength(Principal principal) {
        Integer length = cartService.getCartItemNumber(principal);
        ApiResponseMessage<Integer> apiResponseMessage =
                ApiResponseMessage.<Integer>builder().message(length).success(true).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

}
