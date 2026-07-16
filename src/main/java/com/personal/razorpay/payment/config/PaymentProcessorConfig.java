package com.personal.razorpay.payment.config;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.processor.PaymentProcessor;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.personal.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.personal.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.personal.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;


    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor
        );
    }
}
