package com.link.product.infrastructure.out.jpa.mapper;

import com.link.product.domain.model.Product;
import com.link.product.infrastructure.out.jpa.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductEntityMapper {

    ProductEntity toEntity(Product domain);

    Product toDomain(ProductEntity entity);

}
