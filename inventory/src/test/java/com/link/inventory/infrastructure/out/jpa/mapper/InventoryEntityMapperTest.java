package com.link.inventory.infrastructure.out.jpa.mapper;

import com.link.inventory.domain.model.Inventory;
import com.link.inventory.infrastructure.out.jpa.entity.InventoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityMapperTest {

    private InventoryEntityMapper inventoryEntityMapper;
    private Inventory inventory;
    private InventoryEntity inventoryEntity;

    @BeforeEach
    void setUp() {
        inventoryEntityMapper = Mappers.getMapper(InventoryEntityMapper.class);

        Instant now = Instant.now();
        inventory = new Inventory(
                1L,                    // id
                5L,                    // productId
                "iPhone 15 Pro",       // productName
                100,                   // quantity
                "Warehouse A",         // location
                now                    // lastUpdated
        );

        inventoryEntity = new InventoryEntity(
                1L,                    // id
                5L,                    // productId
                100,                   // quantity
                "Warehouse A",         // location
                now                    // lastUpdated
        );
    }

    @Test
    void shouldMapInventoryDomainToEntitySuccessfully() {
        // When
        InventoryEntity entity = inventoryEntityMapper.toEntity(inventory);

        // Then
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(5L, entity.getProductId());
        assertEquals(100, entity.getQuantity());
        assertEquals("Warehouse A", entity.getLocation());
        assertEquals(inventory.getLastUpdated(), entity.getLastUpdated());

    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        // When
        InventoryEntity entity = inventoryEntityMapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    void shouldMapDomainWithNullFieldsToEntity() {
        // Given
        Inventory inventoryWithNulls = new Inventory(
                2L,
                10L,
                "Product Name",
                null,
                null,
                null
        );

        // When
        InventoryEntity entity = inventoryEntityMapper.toEntity(inventoryWithNulls);

        // Then
        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals(10L, entity.getProductId());
        assertNull(entity.getQuantity());
        assertNull(entity.getLocation());
        assertNull(entity.getLastUpdated());
    }

    @Test
    void shouldMapInventoryEntityToDomainSuccessfully() {
        // When
        Inventory domain = inventoryEntityMapper.toDomain(inventoryEntity);

        // Then
        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals(5L, domain.getProductId());
        assertEquals(100, domain.getQuantity());
        assertEquals("Warehouse A", domain.getLocation());
        assertEquals(inventoryEntity.getLastUpdated(), domain.getLastUpdated());
        assertNull(domain.getProductName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        // When
        Inventory domain = inventoryEntityMapper.toDomain(null);

        // Then
        assertNull(domain);
    }

    @Test
    void shouldMapEntityWithNullFieldsToDomain() {
        // Given
        InventoryEntity entityWithNulls = new InventoryEntity(
                3L,
                15L,
                null,      // quantity null
                null,      // location null
                null       // lastUpdated null
        );

        // When
        Inventory domain = inventoryEntityMapper.toDomain(entityWithNulls);

        // Then
        assertNotNull(domain);
        assertEquals(3L, domain.getId());
        assertEquals(15L, domain.getProductId());
        assertNull(domain.getQuantity());
        assertNull(domain.getLocation());
        assertNull(domain.getLastUpdated());
        assertNull(domain.getProductName());  // Ignorado siempre
    }

    @Test
    void shouldMaintainDataIntegrityInBidirectionalMapping() {
        // Given
        Instant now = Instant.now();
        Inventory originalInventory = new Inventory(
                10L,
                20L,
                "Test Product",
                50,
                "Test Warehouse",
                now
        );

        // When - Domain -> Entity -> Domain
        InventoryEntity entity = inventoryEntityMapper.toEntity(originalInventory);
        Inventory resultInventory = inventoryEntityMapper.toDomain(entity);

        // Then
        assertNotNull(resultInventory);
        assertEquals(originalInventory.getId(), resultInventory.getId());
        assertEquals(originalInventory.getProductId(), resultInventory.getProductId());
        assertEquals(originalInventory.getQuantity(), resultInventory.getQuantity());
        assertEquals(originalInventory.getLocation(), resultInventory.getLocation());
        assertEquals(originalInventory.getLastUpdated(), resultInventory.getLastUpdated());
        assertNull(resultInventory.getProductName());  // Ignorado correctamente
    }

    @Test
    void shouldHandleInstantConversionBidirectionally() {
        // Given
        Instant specificTime = Instant.parse("2025-11-30T01:00:00Z");
        inventory.setLastUpdated(specificTime);
        inventoryEntity.setLastUpdated(specificTime);

        // When
        InventoryEntity entity = inventoryEntityMapper.toEntity(inventory);
        Inventory domain = inventoryEntityMapper.toDomain(inventoryEntity);

        // Then
        assertEquals(specificTime, entity.getLastUpdated());
        assertEquals(specificTime, domain.getLastUpdated());
    }
}
