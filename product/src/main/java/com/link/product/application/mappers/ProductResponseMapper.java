package com.link.product.application.mappers;

import com.link.product.application.dto.ProductRequest;
import com.link.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductRequestMapper {

    @Mapping(target = "id", ignore = true)
    Product toDomain(ProductRequest request);
}

