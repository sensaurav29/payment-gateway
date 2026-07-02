package com.personal.razorpay.payment.config;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.CardPaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.NetBankingAdapter;
import com.personal.razorpay.payment.gateway.adapters.UpiPaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.WalletPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymemtAdapters() {

        return Map.of(
                PaymentMethod.CARD, new CardPaymentAdapter(),
                PaymentMethod.NETBANKING, new NetBankingAdapter(),
                PaymentMethod.UPI, new UpiPaymentAdapter(),
                PaymentMethod.WALLET, new WalletPaymentAdapter()
        );
    }
}
