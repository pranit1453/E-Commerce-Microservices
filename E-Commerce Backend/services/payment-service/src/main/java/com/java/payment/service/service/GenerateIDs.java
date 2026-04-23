package com.java.payment.service.service;

import java.util.UUID;

public class GenerateIDs {

    public static String generateGatewayOrderId(UUID orderId) {
        return "order_" + System.currentTimeMillis() + "_" + orderId.toString().replace("-", "");
    }

    public static String generateGatewayPaymentId(UUID paymentId) {
        return "payment_" + System.currentTimeMillis() + "_" + paymentId.toString().replace("-", "");
    }
}
