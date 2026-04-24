package com.java.payment.service.payment.service;


import com.java.payment.service.payment.dto.*;
import com.java.payment.service.payment.entity.Payment;
import com.java.payment.service.payment.entity.PaymentGateway;
import com.java.payment.service.payment.enums.GatewayName;
import com.java.payment.service.payment.enums.GatewayStatus;
import com.java.payment.service.payment.enums.PaymentMethod;
import com.java.payment.service.payment.enums.PaymentStatus;
import com.java.payment.service.payment.factory.PaymentGatewayFactory;
import com.java.payment.service.payment.repository.PaymentRepository;
import com.java.payment.service.util.HmacUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayFactory paymentGatewayFactory;
    private final HmacUtil hmacUtil;
    private final StreamBridge streamBridge;

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

        PaymentGatewayResponse response =
                paymentGatewayFactory.getGatewayFactory(request.gateway())
                        .getPaymentService(request.service())
                        .initiate(paymentData);

        if (response == null || response.gatewayOrderId() == null) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            throw new RuntimeException("Payment initiation failed");
        }
        PaymentGateway gateway = PaymentGateway.builder()
                .gatewayOrderId(response.gatewayOrderId())
                .status(GatewayStatus.INITIATED)
                .gatewayName(GatewayName.valueOf(request.gateway().toUpperCase()))
                .paymentMethod(PaymentMethod.valueOf(request.service().toUpperCase()))
                .build();
        payment.attachGateway(gateway);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        return Map.of(
                "paymentId", payment.getPaymentId().toString(),
                "gatewayOrderId", response.gatewayOrderId()
        );
    }

    @Transactional
    public void verifyPayment(VerifyPaymentRequest request) {
        UUID paymentId = UUID.fromString(request.paymentId());
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Founds"));

        PaymentGateway gateway = payment.getPaymentGateway();

        if (gateway == null) {
            throw new RuntimeException("Payment gateway not initialized");
        }
        String payload = request.gatewayOrderId() + "|" + request.gatewayPaymentId();

        String generateSignature = hmacUtil.generateSignature(payload);

        if (!generateSignature.equals(request.gatewaySignature())) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.getPaymentGateway().setStatus(GatewayStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Invalid Signature");
        }
        gateway.setGatewayPaymentId(request.gatewayPaymentId());
        gateway.setGatewaySignature(request.gatewaySignature());
        gateway.setStatus(GatewayStatus.VERIFIED);

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentGateway(gateway);
        paymentRepository.save(payment);

        // Call order service to update status of order
        CreateOrderResponseToUpdateStatus orderDetail = CreateOrderResponseToUpdateStatus.builder()
                .userId(payment.getUserId())
                .orderId(payment.getOrderId())
                .paymentId(payment.getPaymentId())
                .grandTotal(payment.getAmount())
                .build();

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        callOrderServiceToUpdateStatus(orderDetail);
                    }
                });

        // call billing service to generate a bill
    }

    private void callOrderServiceToUpdateStatus(CreateOrderResponseToUpdateStatus orderDetail) {
        streamBridge.send("paymentSuccessEvent-out-0", orderDetail);
    }


}
