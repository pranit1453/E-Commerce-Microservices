package com.java.payment.service.payment.dto;

import com.java.payment.service.payment.details.PaymentDetails;

import java.util.UUID;

public record PaymentInitiateRequest(
        UUID paymentId,
        String gateway,
        String service,
        PaymentDetails detail
) {
}
