package com.java.payment.service.service;

import com.java.payment.service.entity.Payment;
import com.java.payment.service.enums.PaymentStatus;
import com.java.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    // create payment
    public Payment createPayment(UUID orderId, BigDecimal amount) {
        Payment payment = Payment.builder()
                .orderId(orderId)
                .paymentStatus(PaymentStatus.CREATED)
                .amount(amount)
                .currency("INR")
                .build();
        return paymentRepository.save(payment);
    }

    // initiate payment
    public Payment initiatePayment(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found!"));

        payment.setGatewayOrderId("order_" + UUID.randomUUID());
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        return paymentRepository.save(payment);
    }

    // simulate payment
    public Payment simulatePayment(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found!"));

        payment.setGatewayPaymentId("pay_" + UUID.randomUUID());

        // signature
        String raw = payment.getOrderId() + "_" + payment.getPaymentId();
        payment.setGatewaySignature(raw);

        return paymentRepository.save(payment);
    }

    public Payment verifyPayment(UUID paymentId, String signature) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found!"));
        // decode signature
        //String expected =
        if (!payment.getGatewaySignature().equals(signature)) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        }
        return paymentRepository.save(payment);
    }
}
