package com.link.product.unit.application.mappers;

import com.link.product.application.dto.ProductRequest;
import com.link.product.application.mappers.ProductRequestMapper;
import com.link.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestMapperTest {

    private ProductRequestMapper productRequestMapper;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequestMapper = Mappers.getMapper(ProductRequestMapper.class);

        productRequest = new ProductRequest(
                "iPhone 15 Pro",
                "Último modelo de Apple con chip A17 Pro",
                4500000L,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );
    }

    @Test
    void shouldMapProductRequestToDomainSuccessfully() {
        // When
        Product product = productRequestMapper.toDomain(productRequest);

        // Then
        assertNotNull(product);
        assertNull(product.getId());
        assertEquals(productRequest.getProductName(), product.getProductName());
        assertEquals(productRequest.getDescription(), product.getDescription());
        assertEquals(productRequest.getPrice().doubleValue(), product.getPrice());
        assertEquals(productRequest.getCategory(), product.getCategory());
        assertEquals(productRequest.getBrand(), product.getBrand());
        assertEquals(productRequest.getImageUrl(), product.getImageUrl());
    }

    @Test
    void shouldReturnNullWhenProductRequestIsNull() {
        // When
        Product product = productRequestMapper.toDomain(null);

        // Then
        assertNull(product);
    }

    @Test
    void shouldMapProductRequestWithNullFieldsToDomain() {
        // Given
        ProductRequest requestWithNulls = new ProductRequest(
                "Test Product",
                null,  // description null
                100000L,
                null,  // category null
                "TestBrand",
                null   // imageUrl null
        );

        // When
        Product product = productRequestMapper.toDomain(requestWithNulls);

        // Then
        assertNotNull(product);
        assertNull(product.getId());
        assertEquals("Test Product", product.getProductName());
        assertNull(product.getDescription());
        assertEquals(100000.0, product.getPrice());
        assertNull(product.getCategory());
        assertEquals("TestBrand", product.getBrand());
        assertNull(product.getImageUrl());
    }

    @Test
    void shouldIgnoreIdFieldWhenMapping() {
        // When
        Product product = productRequestMapper.toDomain(productRequest);

        // Then
        assertNull(product.getId(), "El ID debe ser null porque está configurado con @Mapping(target = \"id\", ignore = true)");
    }

    @Test
    void shouldMapProductRequestWithAllRequiredFields() {
        // Given
        ProductRequest completeRequest = new ProductRequest(
                "Complete Product",
                "Full description",
                999999L,
                "Test Category",
                "Test Brand",
                "https://example.com/complete.jpg"
        );

        // When
        Product product = productRequestMapper.toDomain(completeRequest);

        // Then
        assertNotNull(product);
        assertEquals("Complete Product", product.getProductName());
        assertEquals("Full description", product.getDescription());
        assertEquals(999999.0, product.getPrice());
        assertEquals("Test Category", product.getCategory());
        assertEquals("Test Brand", product.getBrand());
        assertEquals("https://example.com/complete.jpg", product.getImageUrl());
    }

    @Test
    void shouldConvertLongPriceToDoubleCorrectly() {
        // Given
        ProductRequest requestWithLongPrice = new ProductRequest(
                "Price Test Product",
                "Testing Long to Double conversion",
                5000000L,
                "Test",
                "TestBrand",
                "https://test.com/image.jpg"
        );

        // When
        Product product = productRequestMapper.toDomain(requestWithLongPrice);

        // Then
        assertNotNull(product);
        assertEquals(5000000.0, product.getPrice());
        assertInstanceOf(Double.class, product.getPrice(), "El precio debe ser de tipo Double en el dominio");
    }
}