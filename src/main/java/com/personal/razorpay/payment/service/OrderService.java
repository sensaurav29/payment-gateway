package com.personal.razorpay.payment.service;

import com.personal.razorpay.payment.dto.request.CreateOrderRequest;
import com.personal.razorpay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);
}
