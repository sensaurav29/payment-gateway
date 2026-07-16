package com.personal.razorpay.payment.statemachine;

import com.personal.razorpay.common.enums.PaymentActor;
import com.personal.razorpay.common.enums.PaymentEvent;
import com.personal.razorpay.common.enums.PaymentStatus;
import com.personal.razorpay.payment.entity.Payment;
import com.personal.razorpay.payment.entity.PaymentTransitionLog;
import com.personal.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine  paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);


        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM)
                .occuredAt(LocalDateTime.now())
                .build();
        payment.setStatus(next);
        paymentTransitionLogRepository.save(log);

        return next;
    }


}
