package com.personal.razorpay.payment.gateway;

import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest paymentRequest);

    PaymentResult capture(UUID paymentId);
}
