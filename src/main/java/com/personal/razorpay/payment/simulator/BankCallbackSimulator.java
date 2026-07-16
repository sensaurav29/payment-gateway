package com.personal.razorpay.payment.simulator;

import com.personal.razorpay.common.enums.ChaosMode;
import com.personal.razorpay.common.enums.PaymentStatus;
import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.payment.entity.Payment;
import com.personal.razorpay.payment.repository.PaymentRepository;
import com.personal.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

//    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
    public void processCallbacks() {

        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);

        List<Payment> candidates = paymentRepository
                .findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

        log.info("Simulating payments for {} payments", candidates.size());

        if (candidates.isEmpty()) return;

        for (Payment payment: candidates) {
            simulateCallback(payment);
        }

    }

    private void simulateCallback(Payment payment) {
        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig =
                simulatorConfig.configFor(payment.getMethod());

        LocalDateTime dueAt = dueAt(payment, methodSimulatorConfig);

        if(LocalDateTime.now().isBefore(dueAt))
        {
            return;
        }

        ChaosMode chaosMode = simulatorConfig.getChaosMode();
        switch(chaosMode){
            case SUCCESS -> resolve(payment, true);
            case FAILURE -> resolve(payment, false);
            case TIMEOUT -> {
                // Do nothing, simulate timeout
                log.info("Bank Callback Simulator: Payment Timed Out");
            }
            case NORMAL, SLOW -> {
                boolean result  = shouldApprove(payment, methodSimulatorConfig);
                if(result){
                    resolve(payment, true);
                }else {
                    resolve(payment, false);
                }
            }
        }
    }

    private void resolve(Payment payment, boolean approved) {
        if(approved){
          String bankReference = "SIM_BANK_REF_" + RandomizeUtil.randomBase64(8);

          paymentService.resolveAuthorization(payment.getId(), true,  bankReference, null, null);
        }else{
            paymentService.resolveAuthorization(payment.getId(), false, null,
                    "SIM_ERROR", "Simulated Failure");
        }
    }

    private boolean shouldApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig) {

        int bucket = Math.abs(payment.getId().hashCode()) % 100;
        return bucket < methodSimulatorConfig.getSuccessRate();
    }

    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodConfig) {
        int range = methodConfig.getMaxDelaySeconds() - methodConfig.getMinDelaySeconds();
        int delaySeconds = methodConfig.getMinDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range + 1);

        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW)
        {
            delaySeconds += 2;
        }

        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }

}










