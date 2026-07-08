package com.personal.razorpay.payment.config;

import com.personal.razorpay.common.enums.PaymentMethod;
import com.personal.razorpay.payment.gateway.PaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.CardPaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.NetBankingAdapter;
import com.personal.razorpay.payment.gateway.adapters.UpiPaymentAdapter;
import com.personal.razorpay.payment.gateway.adapters.WalletPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final NetBankingAdapter netBankingAdapter;
    private final WalletPaymentAdapter walletPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymemtAdapters() {


        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI, upiPaymentAdapter,
                PaymentMethod.WALLET, walletPaymentAdapter
        );
    }
}
