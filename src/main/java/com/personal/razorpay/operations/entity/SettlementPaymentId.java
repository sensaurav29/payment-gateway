package com.personal.razorpay.operations.entity;

import com.personal.razorpay.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId extends BaseEntity {

    private UUID settlementId;
    private UUID paymentId;
}
