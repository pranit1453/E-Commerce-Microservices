package com.java.payment.service.service;

import com.java.payment.service.dto.PaymentGatewayData;
import com.java.payment.service.dto.PaymentGatewayResponse;

public interface PaymentMethodService {
    String getServiceType();
    
    PaymentGatewayResponse initiate(PaymentGatewayData payment);

}
