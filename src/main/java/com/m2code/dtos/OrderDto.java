package com.m2code.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String orderId;
    private Date orderDate = new Date(System.currentTimeMillis());
    private Date deliveredDate;
    private long orderAmount;
    private String billingAddress;
    private String billingName;
    private String billingPhone;
    private ORDER_STATUS orderStatus = ORDER_STATUS.PENDING;
    private PAYMENT_STATUS paymentStatus = PAYMENT_STATUS.NOT_PAID;
    private List<OrderItemDto> orderItems;
    private UserDto user;
}
