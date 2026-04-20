package com.java.order.service.order.service;

import com.java.order.service.order.dto.OrderRequest;
import com.java.order.service.order.dto.OrderResponse;
import jakarta.validation.Valid;

public interface OrderService {
    OrderResponse createOrder(@Valid OrderRequest request);
}
