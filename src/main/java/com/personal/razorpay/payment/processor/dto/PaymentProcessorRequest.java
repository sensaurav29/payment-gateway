package com.personal.razorpay.payment.processor.dto;

import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID processingId,
        UUID paymentId,
        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String, Object> methodDetails
) {


    public static PaymentProcessorRequest card(UUID paymentId, String pan,
                                               String expiry, Money amount,
                                               Map<String, Object> methodDetails) {
        return new PaymentProcessorRequest(
                UUID.randomUUID(),
                paymentId,
                PaymentMethod.CARD,
                amount,
                pan,
                expiry,
                methodDetails
        );
    }

    public static PaymentProcessorRequest nonCard(UUID paymentId,  Money amount,
                                                  PaymentMethod paymentMethod,
                                               Map<String, Object> methodDetails) {
        return new PaymentProcessorRequest(
                UUID.randomUUID(),
                paymentId,
                paymentMethod,
                amount,
                null,
                null,
                methodDetails
        );
    }
}
