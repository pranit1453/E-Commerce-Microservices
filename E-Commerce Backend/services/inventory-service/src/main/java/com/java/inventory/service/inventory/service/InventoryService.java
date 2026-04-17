package com.java.inventory.service.inventory.service;

import com.java.inventory.service.inventory.dto.CreateInventoryRequest;
import com.java.inventory.service.inventory.dto.CreateInventoryResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface InventoryService {
    CreateInventoryResponse createNewInventory(@Valid CreateInventoryRequest request);

    Boolean isStockAvailable(@NotNull UUID id, @Min(1) Integer quantity);

    void reserveStock(@NotNull UUID id, @Min(1) Integer quantity);

    void releaseStock(@NotNull UUID id, @Min(1) Integer quantity);
}
