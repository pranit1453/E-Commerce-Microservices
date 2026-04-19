package com.java.inventory.service.inventory.mapper;

import com.java.inventory.service.inventory.dto.CreateInventoryRequest;
import com.java.inventory.service.inventory.dto.CreateInventoryResponse;
import com.java.inventory.service.inventory.dto.UpdateInventoryRequest;
import com.java.inventory.service.inventory.dto.UpdateInventoryResponse;
import com.java.inventory.service.inventory.entity.Inventory;
import org.mapstruct.*;

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

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "totalQuantity", source = "totalQuantity"),
            @Mapping(target = "reservedQuantity", source = "reservedQuantity")
    })
    Inventory mappedToInventory(final UpdateInventoryRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "inventoryId", source = "inventoryId"),
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "totalQuantity", source = "totalQuantity"),
            @Mapping(target = "reservedQuantity", source = "reservedQuantity")
    })
    UpdateInventoryResponse mappedToUpdateInventoryResponse(final Inventory inventory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchInventory(final UpdateInventoryRequest request, @MappingTarget Inventory inventory);


}
