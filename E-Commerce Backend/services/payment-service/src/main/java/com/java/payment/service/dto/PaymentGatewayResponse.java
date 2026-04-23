package com.java.payment.service.dto;

import lombok.Builder;

@Builder
public record PaymentGatewayResponse(
        String gatewayOrderId,
        String gatewayPaymentId,
        String gatewaySignature
) {
}
