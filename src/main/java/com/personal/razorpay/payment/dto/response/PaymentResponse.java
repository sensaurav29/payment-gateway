package com.personal.razorpay.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.common.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt

) {
}
