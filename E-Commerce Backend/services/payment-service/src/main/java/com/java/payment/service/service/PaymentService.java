package com.java.payment.service.service;


import com.java.payment.service.dto.PaymentGatewayData;
import com.java.payment.service.dto.PaymentGatewayResponse;
import com.java.payment.service.dto.PaymentInitiateRequest;
import com.java.payment.service.entity.Payment;
import com.java.payment.service.enums.GatewayStatus;
import com.java.payment.service.enums.PaymentStatus;
import com.java.payment.service.factory.PaymentGatewayFactory;
import com.java.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayFactory paymentGatewayFactory;

    public Map<String, String> initiatePaymentWithGateway(PaymentInitiateRequest request) {
        Payment payment = paymentRepository.findByPaymentId(request.paymentId())
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Founds"));

        PaymentGatewayData paymentData = PaymentGatewayData.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .service(request.service())
                .details(request.detail())
                .build();
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.getPaymentGateway().setStatus(GatewayStatus.INITIATED);
        PaymentGatewayResponse response =
                paymentGatewayFactory.getGatewayFactory(request.gateway())
                        .getPaymentService(request.service())
                        .initiate(paymentData);
        // check response null gateway status failed, payment status pending
        payment.getPaymentGateway().setGatewayOrderId(response.gatewayOrderId());
        payment.getPaymentGateway().setGatewayPaymentId(response.gatewayPaymentId());
        payment.getPaymentGateway().setGatewaySignature(response.gatewaySignature());
        payment.getPaymentGateway().setStatus(GatewayStatus.VERIFIED);

        paymentRepository.save(payment);

        return Map.of(
                "gatewayOrderId", response.gatewayOrderId(),
                "gatewayPaymentId", response.gatewayPaymentId(),
                "gatewaySignature", response.gatewaySignature()
        );
    }

    public String verifyPayment(UUID paymentId, String signature) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        boolean isValid = validateSignature(signature, payment.getPaymentGateway().getGatewaySignature());
        if (!isValid) {
            payment.getPaymentGateway().setStatus(GatewayStatus.FAILED);
            paymentRepository.save(payment);
            return "Payment failed";
        }

        payment.getPaymentGateway().setStatus(GatewayStatus.VERIFIED);
        if (payment.getPaymentGateway().getStatus() == GatewayStatus.VERIFIED) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        }
        return "Payment success";
    }

    private boolean validateSignature(String signature, String gatewaySignature) {

        return gatewaySignature.equals(signature);
    }

}
