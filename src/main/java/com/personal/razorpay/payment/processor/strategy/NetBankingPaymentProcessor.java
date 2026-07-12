package com.personal.razorpay.payment.processor.strategy;

import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.payment.processor.PaymentProcessor;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {


    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest paymentProcessorRequest) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = paymentProcessorRequest.methodDetails() != null ?
                paymentProcessorRequest.methodDetails().get("BANK").toString() : null;

        //simulation for failed transactions (marked as failed by the bank)
        if (BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizeUtil.randomBase64(16);

//        String redirectRef = "http://REDIRECT_BANK.com/" + processorRef;


        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
