package com.java.payment.service.payment.dto;

import com.java.payment.service.payment.details.PaymentDetails;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PaymentGatewayData(
        UUID paymentId,
        UUID orderId,
        BigDecimal amount,
        String service,
        PaymentDetails details
) {
}
