package com.personal.razorpay.payment.service.impls;

import com.personal.razorpay.common.enums.OrderStatus;
import com.personal.razorpay.common.exceptions.DuplicateResourceException;
import com.personal.razorpay.payment.dto.request.CreateOrderRequest;
import com.personal.razorpay.payment.dto.response.OrderResponse;
import com.personal.razorpay.payment.entity.OrderRecord;
import com.personal.razorpay.payment.repository.OrderRepository;
import com.personal.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:20}")
    private int defaultExpiryMinutes;
    @Override
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

        return new OrderResponse(
                order.getId(),
                order.getMerchantId(),
                order.getReceipt(),
                order.getAmount(),
                order.getOrderStatus(),
                order.getAttempts(),
                order.getNotes(),
                order.getExpiresAt(),
                null // TODO: Add the createdAt after adding auditing classes
        );

    }
}
