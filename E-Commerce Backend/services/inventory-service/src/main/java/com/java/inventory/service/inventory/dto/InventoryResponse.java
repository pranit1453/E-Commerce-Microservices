package com.java.inventory.service.inventory.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryResponse(
        UUID inventoryId,
        UUID productId,
        Integer totalQuantity,
        Integer reservedQuantity,
        Integer availableQuantity
) {
}
