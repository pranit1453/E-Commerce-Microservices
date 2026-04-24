package com.java.payment.service.gateway.service;

import com.java.payment.service.gateway.dto.GatewayResponse;

public interface GatewayService {
    GatewayResponse completePayment(String gatewayOrderId);
}
