package com.link.inventory.application.mappers;

import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.domain.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryResponseMapper {

    @Mapping(target = "id", expression = "java(String.valueOf(inventory.getId()))")
    @Mapping(
            target = "lastUpdated",
            expression = "java(inventory.getLastUpdated() != null ? inventory.getLastUpdated().toString() : null)"
    )
    InventoryResponse toResponse(Inventory inventory);

}

