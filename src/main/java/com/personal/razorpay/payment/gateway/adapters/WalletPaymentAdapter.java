package com.personal.razorpay.payment.gateway.adapters;

import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {

        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
