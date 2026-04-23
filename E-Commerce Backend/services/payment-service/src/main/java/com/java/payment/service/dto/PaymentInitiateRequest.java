package com.java.payment.service.dto;

import com.java.payment.service.details.PaymentDetails;

import java.util.UUID;

public record PaymentInitiateRequest(
        UUID paymentId,
        String gateway,
        String service,
        PaymentDetails detail
) {
}
