package com.link.product.application.mappers;

import com.link.product.application.dto.ProductResponse;
import com.link.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class ProductResponseMapperTest {

    private ProductResponseMapper productResponseMapper;
    private Product product;

    @BeforeEach
    void setUp() {
        productResponseMapper = Mappers.getMapper(ProductResponseMapper.class);

        product = new Product(
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
    void shouldMapProductToResponseSuccessfully() {
        // When
        ProductResponse response = productResponseMapper.toResponse(product);

        // Then
        assertNotNull(response);
        assertEquals(String.valueOf(product.getId()), response.getId()); // Convertir Long a String
        assertEquals(product.getProductName(), response.getProductName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getCategory(), response.getCategory());
        assertEquals(product.getBrand(), response.getBrand());
        assertEquals(product.getImageUrl(), response.getImageUrl());
    }

    @Test
    void shouldReturnNullWhenProductIsNull() {
        // When
        ProductResponse response = productResponseMapper.toResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    void shouldMapProductWithNullFieldsToResponse() {
        // Given
        Product productWithNulls = new Product(
                2L,
                "Test Product",
                null,  // description null
                100000.0,
                null,  // category null
                "TestBrand",
                null   // imageUrl null
        );

        // When
        ProductResponse response = productResponseMapper.toResponse(productWithNulls);

        // Then
        assertNotNull(response);
        assertEquals("2", response.getId()); // Comparar como String
        assertEquals("Test Product", response.getProductName());
        assertNull(response.getDescription());
        assertEquals(100000.0, response.getPrice());
        assertNull(response.getCategory());
        assertEquals("TestBrand", response.getBrand());
        assertNull(response.getImageUrl());
    }

    @Test
    void shouldMapProductWithZeroPriceToResponse() {
        // Given
        Product productWithZeroPrice = new Product(
                3L,
                "Free Product",
                "Free promotional item",
                0.0,
                "Promotion",
                "FreeBrand",
                "https://example.com/free.jpg"
        );

        // When
        ProductResponse response = productResponseMapper.toResponse(productWithZeroPrice);

        // Then
        assertNotNull(response);
        assertEquals("3", response.getId()); // Comparar como String
        assertEquals(0.0, response.getPrice());
    }

    @Test
    void shouldMapIdFromLongToString() {
        // Given
        Product productWithLongId = new Product(
                999L,
                "Test ID Conversion",
                "Testing Long to String conversion",
                100000.0,
                "Test",
                "TestBrand",
                "https://test.com/image.jpg"
        );

        // When
        ProductResponse response = productResponseMapper.toResponse(productWithLongId);

        // Then
        assertNotNull(response);
        assertEquals("999", response.getId());
        assertInstanceOf(String.class, response.getId(), "El ID debe ser de tipo String");
    }
}