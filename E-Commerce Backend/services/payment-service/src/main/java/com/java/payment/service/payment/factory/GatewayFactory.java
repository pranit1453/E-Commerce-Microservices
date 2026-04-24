package com.java.payment.service.payment.factory;

import com.java.payment.service.payment.service.PaymentMethodService;

public interface GatewayFactory {
    String getGatewayName();

    PaymentMethodService getPaymentService(String gateway);
}
