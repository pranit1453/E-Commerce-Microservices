package com.java.payment.service.gateway.store;

import com.java.payment.service.gateway.model.GatewayOrder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DummyGatewayStore {
    /*
        For racking orders inside gateway
     */
    public static final Map<String, GatewayOrder> ORDERS = new ConcurrentHashMap<>();
}
