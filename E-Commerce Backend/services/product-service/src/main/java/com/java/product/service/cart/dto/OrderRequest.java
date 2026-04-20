package com.java.product.service.cart.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderRequest(
        UUID userId,
        List<OrderRequestItem> items
) {
}
