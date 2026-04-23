package com.java.payment.service.service;

import com.java.payment.service.dto.PaymentCreateDetails;
import com.java.payment.service.entity.Payment;
import com.java.payment.service.enums.PaymentStatus;
import com.java.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;

    @Bean
    public Function<PaymentCreateDetails, UUID> createPaymentAsOrderCreated() {
        return detail -> {
            Payment payment = Payment.builder()
                    .orderId(detail.orderId())
                    .userId(detail.userId())
                    .amount(detail.amount())
                    .currency("INR")
                    .paymentStatus(PaymentStatus.CREATED)
                    .build();
            Payment saved = paymentRepository.save(payment);
            return saved.getPaymentId();
        };
    }
}
