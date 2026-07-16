package com.personal.razorpay.payment.processor.strategy;

import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.payment.processor.PaymentProcessor;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpiPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest paymentProcessorRequest) {

        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = paymentProcessorRequest.methodDetails() != null ?
                paymentProcessorRequest.methodDetails().get("vpa").toString() : null;

        //simulation for failed transactions (marked as failed by the bank)
        if (VPA_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("UPI_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "UPI_PROCESSOR_" + RandomizeUtil.randomBase64(16);
;
        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
