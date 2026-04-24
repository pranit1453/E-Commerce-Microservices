package com.java.payment.service.payment.service;

import com.java.payment.service.payment.dto.PaymentGatewayData;
import com.java.payment.service.payment.dto.PaymentGatewayResponse;

public interface PaymentMethodService {
    String getServiceType();

    PaymentGatewayResponse initiate(PaymentGatewayData payment);

}
