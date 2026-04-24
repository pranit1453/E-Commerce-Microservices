package com.java.payment.service.gateway.model;

import com.java.payment.service.gateway.enums.GatewayOrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class GatewayOrder {
    private String gatewayOrderId;
    private BigDecimal amount;
    private GatewayOrderStatus status;
}
