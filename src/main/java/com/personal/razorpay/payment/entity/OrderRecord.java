package com.personal.razorpay.payment.entity;

import com.personal.razorpay.common.entity.BaseEntity;
import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.OrderStatus;
import com.personal.razorpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_record", indexes = {
        @Index(name = "idx_order_id_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_order_merchant_id", columnList = "merchant_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //no foreign key relationship to avoid cascade delete issues when merchant is deleted
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Column(length = 100)
    private String receipt; // Given by merchant if they created an order on their side, can be null(merchant expects us to create an order and provide them the orderId)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


}
