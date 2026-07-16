package com.personal.razorpay.payment.controller;

import com.personal.razorpay.payment.dto.request.CreateOrderRequest;
import com.personal.razorpay.payment.dto.response.OrderResponse;
import com.personal.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    UUID merchantId = UUID.fromString("86b50608-5e23-47b5-bb63-a2345a60c6ae"); // TODO: use API KEY and secret for securely verifying the merchantId;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(merchantId, request));
    }


}
