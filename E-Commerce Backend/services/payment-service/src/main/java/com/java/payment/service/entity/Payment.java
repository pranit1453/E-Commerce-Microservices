package com.java.payment.service.entity;

import com.java.payment.service.enums.PaymentStatus;
import com.java.payment.service.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    private UUID orderId;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private String currency;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;

    private Instant createdAt;
    private Instant updatedAt;

}
