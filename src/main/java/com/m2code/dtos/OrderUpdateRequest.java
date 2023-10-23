package com.m2code.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateRequest {
    private String orderStatus;
    private String paymentStatus;
    private String billingName;
    private String billingPhone;
    private String billingAddress;
    private Date deliveredDate;
}
