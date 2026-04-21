package com.java.product.service.cart.dto;

import lombok.Builder;

@Builder
public record OrderCreationResponse(
        String message
) {
}
