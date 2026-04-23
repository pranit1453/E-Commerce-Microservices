package com.java.payment.service.entity;

import com.java.payment.service.enums.GatewayName;
import com.java.payment.service.enums.GatewayStatus;
import com.java.payment.service.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_gateway")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGateway {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gatewayId;

    @Column(nullable = false)
    private GatewayName gatewayName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;

    private GatewayStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
