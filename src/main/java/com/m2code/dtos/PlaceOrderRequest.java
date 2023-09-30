package com.m2code.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceOrderRequest {
    @NotEmpty(message = "Address is required")
    private String billingAddress;
    @NotEmpty(message = "Name is required for billing")
    private String billingName;
    @NotEmpty(message = "Phone number is required for billing")
    private String billingPhone;
}
