package com.m2code.services;

import com.m2code.dtos.OrderDto;
import com.m2code.dtos.OrderUpdateRequest;
import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.PlaceOrderRequest;
import com.m2code.entities.Order;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(String userId, PlaceOrderRequest orderRequest);

    Order getOrderById(String orderId);

    void removeOrder(String orderId);

    List<OrderDto> getOrderByUser(String userId);

    PageableResponse<OrderDto> getAllOrder(int page, int size, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, OrderUpdateRequest request);
}
