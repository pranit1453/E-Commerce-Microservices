package com.java.order.service.order.service;

import com.java.order.service.order.dto.CreateOrderResponse;
import com.java.order.service.order.dto.OrderRequest;
import com.java.order.service.order.dto.OrderResponse;
import com.java.order.service.order.dto.ProductDetails;
import jakarta.validation.Valid;

import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(@Valid OrderRequest request);

    CreateOrderResponse createOrderAndProceedToBuy(UUID userId, @Valid ProductDetails details);
}
