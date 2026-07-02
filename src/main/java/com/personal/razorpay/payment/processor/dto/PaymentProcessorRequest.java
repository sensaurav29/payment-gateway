package com.personal.razorpay.payment.processor.dto;

import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        Map<String, Object> methodDetails
) {



}
