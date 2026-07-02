package com.personal.razorpay.payment.gateway.dto;

import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(

        UUID paymentId,
        UUID orderId,
        UUID merhcantId,
        Money amount,
        PaymentMethod  method,
        Map<String, Object> methodDetails
) {
}
