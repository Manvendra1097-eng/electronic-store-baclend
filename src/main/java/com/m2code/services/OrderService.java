package com.m2code.services;

import com.m2code.dtos.OrderDto;
import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.PlaceOrderRequest;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(String userId, PlaceOrderRequest orderRequest);

    void removeOrder(String orderId);

    List<OrderDto> getOrderByUser(String userId);

    PageableResponse<OrderDto> getAllOrder(int page, int size, String sortBy, String sortDir);
}
