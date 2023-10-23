package com.m2code.controller;

import com.m2code.dtos.PaymentResponse;
import com.m2code.entities.Order;
import com.m2code.repositories.OrderRepository;
import com.m2code.services.OrderService;
import com.m2code.services.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "Payment")
public class PaymentController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    private final UserService userService;

    @Value("${razorpay.secret.key}")
    private String api_key;
    @Value("${razorpay.secret.id}")
    private String api_id;


    @PostMapping("/{orderId}")
    @Operation(summary = "create order")
    public ResponseEntity<PaymentResponse> paymentRequest(@PathVariable String orderId) throws RazorpayException,
            IOException {
        Order order = orderService.getOrderById(orderId);
        RazorpayClient client = new RazorpayClient(api_id, api_key);
        JSONObject orderRequest = new JSONObject();
        int orderAmount = (int) order.getOrderAmount();
        orderRequest.put("amount", orderAmount * 100);
        orderRequest.put("currency", "INR");

        JSONObject notes = new JSONObject();
        notes.put("orderId", order.getOrderId());
        orderRequest.put("notes", notes);
        orderRequest.put("callback_url", "http://localhost:3000/payment");
        orderRequest.put("callback_method", "get");


        PaymentLink paymentLink = client.paymentLink.create(orderRequest);
        System.out.println(paymentLink);
        JSONObject jsonObject = paymentLink.get("notes");
        PaymentResponse response = PaymentResponse.builder()
                .paymentId(paymentLink.get("id"))
                .url(paymentLink.get("short_url"))
                .orderId(jsonObject.getString("orderId"))
                .key(api_key)
                .build();
        return ResponseEntity.ok().body(response);
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<ApiResponseMessage<String>> updateOrder(@PathVariable String orderId,
//                                                                  @RequestParam String payment_id) throws RazorpayException {
//        Order order = orderService.getOrderById(orderId);
//        RazorpayClient client = new RazorpayClient(api_id, api_key);
//        Payment payment = client.payments.fetch(payment_id);
//        if (payment.get("status").equals("captured")) {
//            order.setPaymentStatus(PAYMENT_STATUS.PAID);
//            order.setOrderStatus(ORDER_STATUS.DISPATCHED);
//            orderRepository.save(order);
//        }
//        ApiResponseMessage<String> responseMessage =
//                ApiResponseMessage.<String>builder().message("payment received").success(true).build();
//        return ResponseEntity.accepted().body(responseMessage);
//    }

}
