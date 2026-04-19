package com.java.inventory.service.transaction;

import com.java.inventory.service.inventory.entity.Inventory;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "inventory_transactions", schema = "inventory_schema",
        indexes = {
                @Index(name = "idx_tx_inventory", columnList = "inventory_id"),
                @Index(name = "idx_tx_order", columnList = "orderId"),
                @Index(name = "idx_tx_type", columnList = "type"),
                @Index(name = "idx_tx_created_at", columnList = "createdAt"),
                @Index(name = "idx_tx_inventory_created", columnList = "inventory_id, createdAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID inventoryTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private UUID orderId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}