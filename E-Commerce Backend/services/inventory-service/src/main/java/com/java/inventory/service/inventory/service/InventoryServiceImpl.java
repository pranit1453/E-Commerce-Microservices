package com.java.inventory.service.inventory.service;

import com.java.inventory.service.client.ProductClient;
import com.java.inventory.service.exception.custom.ResourceNotFoundException;
import com.java.inventory.service.inventory.dto.CreateInventoryRequest;
import com.java.inventory.service.inventory.dto.CreateInventoryResponse;
import com.java.inventory.service.inventory.entity.Inventory;
import com.java.inventory.service.inventory.repository.InventoryRepository;
import com.java.inventory.service.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public CreateInventoryResponse createNewInventory(final CreateInventoryRequest request) {
        if (!Boolean.TRUE.equals(productClient.isProductExists(request.productId()))) {
            throw new ResourceNotFoundException(
                    "Product with id " + request.productId() + " not found"
            );
        }
        Inventory inventory = inventoryMapper.mappedToInventory(request);
        inventory.setTotalQuantity(
                Optional.ofNullable(inventory.getTotalQuantity()).orElse(0));
        inventory.setReservedQuantity(
                Optional.ofNullable(inventory.getReservedQuantity()).orElse(0));

        Inventory savedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.mappedToCreateInventoryResponse(savedInventory);
    }

    @Override
    @Transactional
    public Boolean isStockAvailable(final UUID id, final Integer quantity) {
        Inventory inventory = findByProductId(id);
        return inventory.getAvailableQuantity() >= quantity;
    }

    @Override
    @Transactional
    public void reserveStock(final UUID id, final Integer quantity) {
        Inventory inventory = findByProductId(id);
        inventory.reserve(quantity);
    }

    @Override
    @Transactional
    public void releaseStock(final UUID id, final Integer quantity) {
        Inventory inventory = findByProductId(id);
        inventory.release(quantity);
    }

    private Inventory findByProductId(final UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in Inventory"));
    }
}
