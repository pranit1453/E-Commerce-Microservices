package com.java.inventory.service.inventory.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateInventoryResponse(
        UUID inventoryId,
        UUID productId,
        Integer totalQuantity,
        Integer reservedQuantity
) {
}
