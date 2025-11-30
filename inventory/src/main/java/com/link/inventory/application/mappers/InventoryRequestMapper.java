package com.link.inventory.application.mappers;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.domain.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InventoryRequestMapper {

    Inventory toDomain(InventoryRequest request);

}
