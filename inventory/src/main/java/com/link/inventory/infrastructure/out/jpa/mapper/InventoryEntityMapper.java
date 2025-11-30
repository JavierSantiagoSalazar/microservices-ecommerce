package com.link.inventory.infrastructure.out.jpa.mapper;

import com.link.inventory.domain.model.Inventory;
import com.link.inventory.infrastructure.out.jpa.entity.InventoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryEntityMapper {

    @Mapping(target = "productName", ignore = true)
    Inventory toDomain(InventoryEntity entity);

    InventoryEntity toEntity(Inventory inventory);

}
