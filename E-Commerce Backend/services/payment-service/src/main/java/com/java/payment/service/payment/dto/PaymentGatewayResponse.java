package com.java.payment.service.payment.dto;

import lombok.Builder;

@Builder
public record PaymentGatewayResponse(
        String gatewayOrderId
) {
}
