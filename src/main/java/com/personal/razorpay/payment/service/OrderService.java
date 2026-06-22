package com.personal.razorpay.payment.service;

import com.personal.razorpay.payment.dto.request.CreateOrderRequest;
import com.personal.razorpay.payment.dto.response.OrderResponse;
import com.personal.razorpay.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancelOrder(UUID merchantId, UUID orderId);

    List<PaymentResponse> listOfPayments(UUID merchantId, UUID orderId);
}
