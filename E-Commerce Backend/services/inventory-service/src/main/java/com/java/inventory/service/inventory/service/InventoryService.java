package com.java.inventory.service.inventory.service;

import com.java.inventory.service.inventory.dto.*;
import com.java.inventory.service.inventory.enums.InventorySortField;
import com.java.inventory.service.wrapper.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface InventoryService {
    CreateInventoryResponse createNewInventory(@Valid CreateInventoryRequest request);

    Boolean isStockAvailable(@NotNull UUID id, @Min(1) Integer quantity);

    void reserveStock(@NotNull UUID id, @Min(1) Integer quantity);

    void releaseStock(@NotNull UUID id, @Min(1) Integer quantity);

    UpdateInventoryResponse updateInventory(@NotNull UUID id, @Valid UpdateInventoryRequest request);

    UpdateInventoryResponse patchInventory(@NotNull UUID id, @Valid UpdateInventoryRequest request);

    void deleteInventory(@NotNull UUID id);

    InventoryResponse fetchInventoryById(@NotNull UUID id);

    PageResponse<InventoryResponse> fetchAllInventoryInPage(int page, int size, Integer qty, InventorySortField sortBy, String sortDirection);
}
