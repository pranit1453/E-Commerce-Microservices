package com.java.order.service.order.dto;

import com.java.order.service.order.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID orderId,
        UUID userId,
        List<OrderItemResponse> orderItemResponse,
        BigDecimal grandTotal,
        OrderStatus status,
        String message
) {
}
