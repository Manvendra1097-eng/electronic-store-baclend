package com.m2code.controller;

import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.OrderDto;
import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.PlaceOrderRequest;
import com.m2code.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{userId}")
    @Operation(summary = "place order")
    public ResponseEntity<OrderDto> placeOrder(@Valid @RequestBody PlaceOrderRequest orderRequest, @PathVariable String userId) {
        OrderDto orderDto = orderService.createOrder(userId, orderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "remove order")
    public ResponseEntity<ApiResponseMessage<String>> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        return new ResponseEntity<>(ApiResponseMessage.<String>builder()
                .message("Order removed successfully").success(true)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "get order by user")
    public ResponseEntity<List<OrderDto>> getOrderByUser(@PathVariable String userId) {
        List<OrderDto> orderByUser = orderService.getOrderByUser(userId);
        return new ResponseEntity<>(orderByUser, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "get all order")
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrder(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "orderDate") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir
    ) {
        PageableResponse<OrderDto> pageableResponse = orderService.getAllOrder(page, size, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
}
