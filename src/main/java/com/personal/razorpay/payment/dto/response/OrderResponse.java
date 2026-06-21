package com.personal.razorpay.payment.dto.response;

import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.OrderStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse (
        //Id of the order in our system
        UUID id,
        UUID merchantId,
        String receipt,
        Money amount,
        OrderStatus status,
        Integer attempts,
        Map<String, Object> notes,
        LocalDateTime expiresAt,
        LocalDateTime createdAt

){
}
