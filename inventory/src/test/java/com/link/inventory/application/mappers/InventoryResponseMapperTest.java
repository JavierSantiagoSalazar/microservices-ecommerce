package com.link.inventory.application.mappers;

import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.domain.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class InventoryResponseMapperTest {

    private InventoryResponseMapper inventoryResponseMapper;
    private Inventory completeInventory;

    @BeforeEach
    void setUp() {
        inventoryResponseMapper = Mappers.getMapper(InventoryResponseMapper.class);

        Instant now = Instant.now();
        completeInventory = new Inventory(
                1L,                    // id
                5L,                    // productId
                "iPhone 15 Pro",       // productName
                100,                   // quantity
                "Warehouse A",         // location
                now                    // lastUpdated
        );
    }

    @Test
    void shouldMapInventoryToResponseSuccessfully() {
        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(completeInventory);

        // Then
        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals(5L, response.getProductId());
        assertEquals("iPhone 15 Pro", response.getProductName());
        assertEquals(100, response.getQuantity());
        assertEquals("Warehouse A", response.getLocation());
        assertEquals(completeInventory.getLastUpdated().toString(), response.getLastUpdated());
    }


    @Test
    void shouldHandleNullLastUpdated() {
        // Given
        Inventory inventoryWithoutLastUpdated = new Inventory(
                1L, 5L, "iPhone 15 Pro", 100, "Warehouse A", null
        );

        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(inventoryWithoutLastUpdated);

        // Then
        assertNull(response.getLastUpdated());  // Expression devuelve null
    }

    @Test
    void shouldReturnNullWhenInventoryIsNull() {
        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    void shouldConvertIdToStringCorrectly() {
        // Given
        Inventory inventoryWithLargeId = new Inventory(
                123456789L,
                1L,
                "Test",
                50,
                "Test",
                Instant.now()
        );

        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(inventoryWithLargeId);

        // Then
        assertEquals("123456789", response.getId());  // Expression: String.valueOf(id)
    }

    @Test
    void shouldMapInventoryWithNullFields() {
        // Given
        Inventory inventoryWithNulls = new Inventory(
                1L,
                5L,
                null,
                100,
                null,
                null
        );

        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(inventoryWithNulls);

        // Then
        assertEquals("1", response.getId());
        assertEquals(5L, response.getProductId());
        assertNull(response.getProductName());
        assertEquals(100, response.getQuantity());
        assertNull(response.getLocation());
        assertNull(response.getLastUpdated());
    }

    @Test
    void shouldMapLastUpdatedISOFormatCorrectly() {
        // Given
        Instant specificTime = Instant.parse("2025-11-30T01:00:00Z");

        Inventory inventory = new Inventory(
                1L,
                1L,
                "Test",
                10,
                "Test",
                specificTime
        );

        // When
        InventoryResponse response = inventoryResponseMapper.toResponse(inventory);

        // Then
        String expectedISO = "2025-11-30T01:00:00Z";
        assertEquals(expectedISO, response.getLastUpdated());
    }
}