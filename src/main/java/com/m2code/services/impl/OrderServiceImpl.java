package com.m2code.services.impl;

import com.m2code.dtos.*;
import com.m2code.entities.*;
import com.m2code.exception.BadApiRequestException;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.helper.Util;
import com.m2code.repositories.CartRepository;
import com.m2code.repositories.OrderRepository;
import com.m2code.repositories.UserRepository;
import com.m2code.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(String userId, PlaceOrderRequest orderRequest) {
        User user = getUserByUserId(userId);
        Cart cart = getOrCreateCartForUser(user);
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Can't create order,cart is empty");
        }


        Order order = Order.builder()
                .orderId(Util.getId())
                .user(user)
                .billingAddress(orderRequest.getBillingAddress())
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .orderDate(new Date(System.currentTimeMillis()))
                .orderStatus(ORDER_STATUS.PENDING)
                .paymentStatus(PAYMENT_STATUS.NOT_PAID)
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            return OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .order(order).build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Long amount = orderItems.stream().map(OrderItem::getTotalPrice).reduce(0l, Long::sum);
        order.setOrderAmount(amount);
        Order placedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return modelMapper.map(placedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = getOrderById(orderId);
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrderByUser(String userId) {
        List<Order> orders = getOrder(userId);
        return orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public PageableResponse<OrderDto> getAllOrder(int pageN, int size, String sortBy, String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Util.getPageableResponse(page, OrderDto.class);
    }

    private static Sort getSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    private User getUserByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not registed"));
    }

    private Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private List<Order> getOrder(String userId) {
        User user = getUserByUserId(userId);
        return orderRepository.findByUser(user);
    }

    private Cart getOrCreateCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            cart = Cart.builder().cartId(UUID.randomUUID().toString().replace("-", "")).user(user).build();
            cart = cartRepository.save(cart);
        }
        return cart;
    }
}
