package com.link.product.infrastructure.out.jpa.mapper;

import com.link.product.domain.model.Product;
import com.link.product.infrastructure.out.jpa.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityMapperTest {

    private ProductEntityMapper productEntityMapper;
    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntityMapper = Mappers.getMapper(ProductEntityMapper.class);

        product = new Product(
                1L,
                "iPhone 15 Pro",
                "Último modelo de Apple con chip A17 Pro",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );

        productEntity = new ProductEntity(
                1L,
                "iPhone 15 Pro",
                "Último modelo de Apple con chip A17 Pro",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );
    }

    @Test
    void shouldMapProductDomainToEntitySuccessfully() {
        // When
        ProductEntity entity = productEntityMapper.toEntity(product);

        // Then
        assertNotNull(entity);
        assertEquals(product.getId(), entity.getId());
        assertEquals(product.getProductName(), entity.getProductName());
        assertEquals(product.getDescription(), entity.getDescription());
        assertEquals(product.getPrice(), entity.getPrice());
        assertEquals(product.getCategory(), entity.getCategory());
        assertEquals(product.getBrand(), entity.getBrand());
        assertEquals(product.getImageUrl(), entity.getImageUrl());
    }

    @Test
    void shouldReturnNullWhenProductDomainIsNull() {
        // When
        ProductEntity entity = productEntityMapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    void shouldMapProductDomainWithNullFieldsToEntity() {
        // Given
        Product productWithNulls = new Product(
                2L,
                "Test Product",
                null,
                100000.0,
                null,
                "TestBrand",
                null
        );

        // When
        ProductEntity entity = productEntityMapper.toEntity(productWithNulls);

        // Then
        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Test Product", entity.getProductName());
        assertNull(entity.getDescription());
        assertEquals(100000.0, entity.getPrice());
        assertNull(entity.getCategory());
        assertEquals("TestBrand", entity.getBrand());
        assertNull(entity.getImageUrl());
    }

    @Test
    void shouldMapProductEntityToDomainSuccessfully() {
        // When
        Product domain = productEntityMapper.toDomain(productEntity);

        // Then
        assertNotNull(domain);
        assertEquals(productEntity.getId(), domain.getId());
        assertEquals(productEntity.getProductName(), domain.getProductName());
        assertEquals(productEntity.getDescription(), domain.getDescription());
        assertEquals(productEntity.getPrice(), domain.getPrice());
        assertEquals(productEntity.getCategory(), domain.getCategory());
        assertEquals(productEntity.getBrand(), domain.getBrand());
        assertEquals(productEntity.getImageUrl(), domain.getImageUrl());
    }

    @Test
    void shouldReturnNullWhenProductEntityIsNull() {
        // When
        Product domain = productEntityMapper.toDomain(null);

        // Then
        assertNull(domain);
    }

    @Test
    void shouldMapProductEntityWithNullFieldsToDomain() {
        // Given
        ProductEntity entityWithNulls = new ProductEntity(
                3L,
                "Test Product",
                null,
                100000.0,
                null,
                "TestBrand",
                null
        );

        // When
        Product domain = productEntityMapper.toDomain(entityWithNulls);

        // Then
        assertNotNull(domain);
        assertEquals(3L, domain.getId());
        assertEquals("Test Product", domain.getProductName());
        assertNull(domain.getDescription());
        assertEquals(100000.0, domain.getPrice());
        assertNull(domain.getCategory());
        assertEquals("TestBrand", domain.getBrand());
        assertNull(domain.getImageUrl());
    }

    @Test
    void shouldMaintainDataIntegrityInBidirectionalMapping() {
        // Given
        Product originalProduct = new Product(
                10L,
                "Bidirectional Test",
                "Testing bidirectional mapping",
                250000.0,
                "Test",
                "TestBrand",
                "https://test.com/image.jpg"
        );

        // When - Domain -> Entity -> Domain
        ProductEntity entity = productEntityMapper.toEntity(originalProduct);
        Product resultProduct = productEntityMapper.toDomain(entity);

        // Then
        assertNotNull(resultProduct);
        assertEquals(originalProduct.getId(), resultProduct.getId());
        assertEquals(originalProduct.getProductName(), resultProduct.getProductName());
        assertEquals(originalProduct.getDescription(), resultProduct.getDescription());
        assertEquals(originalProduct.getPrice(), resultProduct.getPrice());
        assertEquals(originalProduct.getCategory(), resultProduct.getCategory());
        assertEquals(originalProduct.getBrand(), resultProduct.getBrand());
        assertEquals(originalProduct.getImageUrl(), resultProduct.getImageUrl());
    }
}