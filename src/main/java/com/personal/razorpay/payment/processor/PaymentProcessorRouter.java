package com.personal.razorpay.payment.processor;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private Map<PaymentMethod, PaymentProcessor> paymentProcessors;


    public PaymentProcessorResponse charge(PaymentProcessorRequest paymentProcessorRequest) {

        PaymentProcessor paymentProcessor  = paymentProcessors.get(paymentProcessorRequest.method());
        if(paymentProcessor == null) {
            throw new IllegalArgumentException("No Payment processor registered for method: "
                    + paymentProcessorRequest.method());
        }
        return paymentProcessor
                .charge(paymentProcessorRequest);

    }
}
