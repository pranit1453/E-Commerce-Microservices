package com.java.inventory.service.inventory.repository;

import com.java.inventory.service.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    boolean existsByProductId(UUID uuid);

    Optional<Inventory> findByProductId(UUID productId);
}
