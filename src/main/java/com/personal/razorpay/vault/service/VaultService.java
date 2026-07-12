package com.personal.razorpay.vault.service;


import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.personal.razorpay.vault.dto.request.TokenizeRequest;
import com.personal.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
