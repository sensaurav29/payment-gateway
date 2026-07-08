package com.personal.razorpay.payment.service;

import com.personal.razorpay.payment.dto.request.PaymentInitRequest;
import com.personal.razorpay.payment.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public interface PaymentService {

    PaymentResponse initiate (UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}
