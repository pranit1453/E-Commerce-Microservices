package com.java.inventory.service.inventory.controller;

import com.java.inventory.service.inventory.dto.*;
import com.java.inventory.service.inventory.enums.InventorySortField;
import com.java.inventory.service.inventory.service.InventoryService;
import com.java.inventory.service.wrapper.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/create")
    ResponseEntity<CreateInventoryResponse> createInventory(@RequestBody final @Valid CreateInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.createNewInventory(request));
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<Boolean> isStockAvailable(
            @PathVariable final @NotNull UUID id,
            @RequestParam @Min(1) Integer quantity
    ) {
        return ResponseEntity.ok(
                inventoryService.isStockAvailable(id, quantity)
        );
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<Void> reserveStock(
            @PathVariable final @NotNull UUID id,
            @RequestParam @Min(1) Integer quantity
    ) {
        inventoryService.reserveStock(id, quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseStock(
            @PathVariable final @NotNull UUID id,
            @RequestParam @Min(1) Integer quantity
    ) {
        inventoryService.releaseStock(id, quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateInventoryResponse> updateInventory(
            @PathVariable final @NotNull UUID id,
            @RequestBody final @Valid UpdateInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.updateInventory(id, request));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<UpdateInventoryResponse> patchInventory(
            @PathVariable final @NotNull UUID id,
            @RequestBody final @Valid UpdateInventoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.patchInventory(id, request));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable final @NotNull UUID id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Inventory with id: " + id + " has been deleted.");
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<InventoryResponse> fetchInventoryById(@PathVariable final @NotNull UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.fetchInventoryById(id));
    }

    public ResponseEntity<PageResponse<InventoryResponse>> fetchInventoryInPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) Integer qty,
            @RequestParam(required = false, defaultValue = "INVENTORY_ID") InventorySortField sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.fetchAllInventoryInPage(page, size, qty, sortBy, sortDirection));
    }

}
