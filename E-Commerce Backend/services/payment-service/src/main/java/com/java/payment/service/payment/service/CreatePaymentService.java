package com.java.payment.service.payment.service;

import com.java.payment.service.payment.dto.PaymentCreateDetails;
import com.java.payment.service.payment.entity.Payment;
import com.java.payment.service.payment.enums.PaymentStatus;
import com.java.payment.service.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;

    @Bean
    public Function<PaymentCreateDetails, Map<String, UUID>> createPaymentAsOrderCreated() {
        return detail -> {

            if (detail.amount() == null ||
                    detail.orderId() == null ||
                    detail.userId() == null) {
                throw new IllegalArgumentException("Invalid payment details");
            }

            Payment payment = Payment.builder()
                    .orderId(detail.orderId())
                    .userId(detail.userId())
                    .amount(detail.amount())
                    .currency("INR")
                    .paymentStatus(PaymentStatus.CREATED)
                    .build();


            Payment saved = paymentRepository.save(payment);
            return Map.of(
                    "paymentId", saved.getPaymentId(),
                    "orderId", saved.getOrderId()
            );
        };
    }
}
