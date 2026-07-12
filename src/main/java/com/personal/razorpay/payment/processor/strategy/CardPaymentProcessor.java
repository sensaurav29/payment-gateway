package com.personal.razorpay.payment.processor.strategy;

import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.payment.processor.PaymentProcessor;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED="4000000000000002";
    public static final String PAN_CARD_EXPIRED="4000000000000069";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest paymentProcessorRequest) {

        String pan  = paymentProcessorRequest.pan();
        if(PAN_CARD_DECLINED.equals(pan)) {
            log.warn("CARD DECLINED");
            return new PaymentProcessorResponse.Failure("CARD_DECLINED", "Card declined by bank");
        }

        if (PAN_CARD_EXPIRED.equals(pan)) {
            log.warn("CARD EXPIRED");
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED", "Card expired");
        }

        String processorRef = "CARD_PROCESSOR_" + RandomizeUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
