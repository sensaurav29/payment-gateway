package com.personal.razorpay.payment.gateway.adapters;

import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.personal.razorpay.vault.service.VaultService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {

        String token = (String) paymentRequest.methodDetails().get("token");

        PaymentProcessorResponse response  = vaultService.charge(paymentRequest.paymentId(), token,
                paymentRequest.amount(), paymentRequest.methodDetails());


        return switch (response){
            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.errorCode()
                    , failure.errorDescription());
            case PaymentProcessorResponse.Pending  pending-> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
