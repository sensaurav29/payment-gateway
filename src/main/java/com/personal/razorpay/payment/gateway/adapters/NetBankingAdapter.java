package com.personal.razorpay.payment.gateway.adapters;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;
import com.personal.razorpay.payment.processor.PaymentProcessor;
import com.personal.razorpay.payment.processor.PaymentProcessorRouter;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
 @RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;


    @Override
    public PaymentResult initiate(PaymentRequest paymentRequest) {

        log.info("initiate payment with NetBankingAdpater, paymentId={}", paymentRequest.paymentId());
        try {

            PaymentProcessorRequest processorRequest = PaymentProcessorRequest.nonCard(
                    paymentRequest.paymentId(),
                    paymentRequest.amount(),
                    PaymentMethod.NETBANKING,
                    paymentRequest.methodDetails()
            );

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter
                    .charge(processorRequest);


            return switch (paymentProcessorResponse){
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case  PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
                case PaymentProcessorResponse.Success success ->
                        new PaymentResult.Success(success.bankReference());

            };
        }catch (Exception e){
            log.warn("NetBanking failed, paymentId={}", paymentRequest.paymentId(), e);
            return new PaymentResult.Failure("NBK_FAILED", e.getMessage());
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("NBK_REF");
    }
}
