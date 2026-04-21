package com.java.order.service.order.function.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderResponse(
        UUID orderId
) {
}
