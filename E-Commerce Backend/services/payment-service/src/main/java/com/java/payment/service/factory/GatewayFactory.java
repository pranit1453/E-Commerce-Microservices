package com.java.payment.service.factory;

import com.java.payment.service.service.PaymentMethodService;

public interface GatewayFactory {
    String getGatewayName();

    PaymentMethodService getPaymentService(String gateway);
}
