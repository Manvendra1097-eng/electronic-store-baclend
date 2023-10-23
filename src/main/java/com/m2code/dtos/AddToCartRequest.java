package com.m2code.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartRequest {
    @NotEmpty(message = "Product Id is required")
    private String productId;
    @NotNull(message = "Quantity is required")
    private int quantity;
}
