package com.personal.razorpay.payment.service.impls;

import com.personal.razorpay.common.enums.OrderStatus;
import com.personal.razorpay.common.enums.PaymentEvent;
import com.personal.razorpay.common.enums.PaymentStatus;
import com.personal.razorpay.common.exceptions.BusinessRuleViolationException;
import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.payment.dto.request.PaymentInitRequest;
import com.personal.razorpay.payment.dto.response.PaymentResponse;
import com.personal.razorpay.payment.entity.OrderRecord;
import com.personal.razorpay.payment.entity.Payment;
import com.personal.razorpay.payment.gateway.PaymentGatewayRouter;
import com.personal.razorpay.payment.gateway.dto.PaymentRequest;
import com.personal.razorpay.payment.gateway.dto.PaymentResult;
import com.personal.razorpay.payment.mapper.PaymentMapper;
import com.personal.razorpay.payment.repository.OrderRepository;
import com.personal.razorpay.payment.repository.PaymentRepository;
import com.personal.razorpay.payment.service.PaymentService;
import com.personal.razorpay.payment.statemachine.PaymentTransitionService;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if(order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE",
                    "Order cannot accept payment in status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                order.getId(), merchantId,
                order.getAmount(), request.method(),
                request.methodDetails());
        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure -> {
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
            case PaymentResult.Success success -> {

            }
        }

        payment = paymentRepository.save(payment);
        orderRepository.save(order);


        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult result = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

        if (result instanceof PaymentResult.Success success) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment successfully captured, paymentId={}", paymentId);
        }
        else if(result instanceof PaymentResult.Failure failure) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
        }

        payment = paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }
}
