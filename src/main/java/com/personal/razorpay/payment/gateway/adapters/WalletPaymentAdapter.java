package com.personal.razorpay.payment.gateway.adapters;

import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

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
