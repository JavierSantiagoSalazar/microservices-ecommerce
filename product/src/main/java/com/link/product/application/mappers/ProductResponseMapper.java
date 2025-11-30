package com.link.product.application.mappers;

import com.link.product.application.dto.ProductResponse;
import com.link.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductResponseMapper {

    ProductResponse toResponse(Product product);

}
