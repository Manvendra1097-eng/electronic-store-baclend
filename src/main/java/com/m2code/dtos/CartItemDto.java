package com.m2code.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private long totalPrice;
}
