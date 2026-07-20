package com.personal.razorpay.payment.controller;

import com.personal.razorpay.merchant.security.MerchantContext;
import com.personal.razorpay.payment.dto.request.PaymentInitRequest;
import com.personal.razorpay.payment.dto.response.PaymentResponse;
import com.personal.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final MerchantContext merchantContext;



    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody
                                                        PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantContext.getMerchantId(), request));
    }

    @PostMapping(path = "{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.capture(merchantContext.getMerchantId(), paymentId));
    }
}
