package com.m2code.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String productId;

    @NotEmpty(message = "Title is required")
    private String title;
    private String description;

    @NotNull(message = "Price is required")
    private long price;
    private long discountedPrice;
    private Date addedAt;

    @NotNull(message = "Quantity is required")
    private int quantity;
    private boolean live;
    private boolean stock;
    private String productImage;
}
