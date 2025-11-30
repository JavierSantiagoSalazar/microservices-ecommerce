package com.link.inventory.application.mappers;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.domain.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class InventoryRequestMapperTest {

    private InventoryRequestMapper inventoryRequestMapper;
    private InventoryRequest validRequest;

    @BeforeEach
    void setUp() {
        inventoryRequestMapper = Mappers.getMapper(InventoryRequestMapper.class);

        validRequest = new InventoryRequest(
                1L,      // productId
                100,     // quantity
                "Warehouse A"  // location
        );
    }

    @Test
    void shouldMapInventoryRequestToDomainSuccessfully() {
        // When
        Inventory inventory = inventoryRequestMapper.toDomain(validRequest);

        // Then
        assertNotNull(inventory);
        assertNull(inventory.getId());
        assertNull(inventory.getProductName());
        assertNull(inventory.getLastUpdated());
        assertEquals(1L, inventory.getProductId());
        assertEquals(100, inventory.getQuantity());
        assertEquals("Warehouse A", inventory.getLocation());
    }

    @Test
    void shouldReturnNullWhenInventoryRequestIsNull() {
        // When
        Inventory inventory = inventoryRequestMapper.toDomain(null);

        // Then
        assertNull(inventory);
    }

    @Test
    void shouldMapWithMinimumValidValues() {
        // Given
        InventoryRequest minRequest = new InventoryRequest(
                1L,
                0,
                "A"
        );

        // When
        Inventory inventory = inventoryRequestMapper.toDomain(minRequest);

        // Then
        assertEquals(1L, inventory.getProductId());
        assertEquals(0, inventory.getQuantity());
        assertEquals("A", inventory.getLocation());
    }

    @Test
    void shouldMapWithNullFieldsToDomain() {
        // Given
        InventoryRequest requestWithNulls = new InventoryRequest(
                null,
                null,
                null
        );

        // When
        Inventory inventory = inventoryRequestMapper.toDomain(requestWithNulls);

        // Then
        assertNull(inventory.getProductId());
        assertNull(inventory.getQuantity());
        assertNull(inventory.getLocation());
    }

    @Test
    void shouldIgnoreFieldsNotInRequest() {
        // When
        Inventory inventory = inventoryRequestMapper.toDomain(validRequest);

        // Then
        assertNull(inventory.getId(), "ID debe ser null (generado por DB)");
        assertNull(inventory.getProductName(), "ProductName viene de Product service");
        assertNull(inventory.getLastUpdated(), "LastUpdated se setea en UseCase");
    }

    @Test
    void shouldMapMaximumLocationLength() {
        // Given
        String maxLocation = "A".repeat(255);
        InventoryRequest maxRequest = new InventoryRequest(1L, 100, maxLocation);

        // When
        Inventory inventory = inventoryRequestMapper.toDomain(maxRequest);

        // Then
        assertEquals(maxLocation, inventory.getLocation());
        assertEquals(255, inventory.getLocation().length());
    }

}
