package com.personal.razorpay.payment.gateway;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public PaymentResult initiate(PaymentRequest paymentRequest) {

        PaymentAdapter adapter = paymentAdapters.get(paymentRequest.method());
        if(adapter == null) {
             throw new IllegalArgumentException("No Payment Adapter registered for method " + paymentRequest.method());
        }

        return adapter.initiate(paymentRequest);
    }
}
