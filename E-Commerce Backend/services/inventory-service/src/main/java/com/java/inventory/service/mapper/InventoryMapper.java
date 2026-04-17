package com.java.inventory.service.mapper;

import com.java.inventory.service.inventory.dto.CreateInventoryRequest;
import com.java.inventory.service.inventory.dto.CreateInventoryResponse;
import com.java.inventory.service.inventory.entity.Inventory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "totalQuantity", source = "totalQuantity"),
            @Mapping(target = "reservedQuantity", source = "reservedQuantity")
    })
    Inventory mappedToInventory(final CreateInventoryRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "inventoryId", source = "inventoryId"),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "totalQuantity", source = "totalQuantity"),
            @Mapping(target = "reservedQuantity", source = "reservedQuantity")
    })
    CreateInventoryResponse mappedToCreateInventoryResponse(final Inventory inventory);
}
