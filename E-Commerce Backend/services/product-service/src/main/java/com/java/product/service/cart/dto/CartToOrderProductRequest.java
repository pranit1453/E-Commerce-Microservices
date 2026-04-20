package com.java.product.service.cart.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CartToOrderProductRequest(
        UUID userId,
        UUID cartId
) {
}
