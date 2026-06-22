package com.personal.razorpay.payment.service.impls;

import com.personal.razorpay.common.enums.OrderStatus;
import com.personal.razorpay.common.exceptions.BusinessRuleViolationException;
import com.personal.razorpay.common.exceptions.DuplicateResourceException;
import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.payment.dto.request.CreateOrderRequest;
import com.personal.razorpay.payment.dto.response.OrderResponse;
import com.personal.razorpay.payment.dto.response.PaymentResponse;
import com.personal.razorpay.payment.entity.OrderRecord;
import com.personal.razorpay.payment.entity.Payment;
import com.personal.razorpay.payment.mapper.OrderMapper;
import com.personal.razorpay.payment.mapper.PaymentMapper;
import com.personal.razorpay.payment.repository.OrderRepository;
import com.personal.razorpay.payment.repository.PaymentRepository;
import com.personal.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:20}")
    private int defaultExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPILCATE", "Order with receipt already exists");
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())

                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null
                        ? request.expiresAt()
                        : LocalDateTime.now().plusMinutes(defaultExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        // TODO: Send/publish kafka event here

        return orderMapper.toResponse(order);

    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(merchantId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId)

    {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(merchantId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_BE_CANCELLED",
                    "Order cannot be cancelled with order status: {}"+ order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        return orderMapper.toResponse(order);
    }


    @Override
    public List<PaymentResponse> listOfPayments(UUID merchantId, UUID orderId) {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(merchantId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));

        List< Payment> payments = paymentRepository.findByOrder_Id(order);

        return paymentMapper.toResponseList(payments);
//        return payments.stream()
//                .map(
//                        payment -> paymentMapper.toResponse(payment)
//                )
//                .collect(Collectors.toList());

    }
}
