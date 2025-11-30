package com.link.inventory.application.handler;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.application.dto.UpdateQuantityRequest;
import com.link.inventory.application.mappers.InventoryRequestMapper;
import com.link.inventory.application.mappers.InventoryResponseMapper;
import com.link.inventory.domain.api.InventoryServicePort;
import com.link.inventory.domain.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryHandlerImplTest {

    @Mock
    private InventoryServicePort inventoryServicePort;

    @Mock
    private InventoryResponseMapper inventoryResponseMapper;

    @Mock
    private InventoryRequestMapper inventoryRequestMapper;

    @InjectMocks
    private InventoryHandlerImpl inventoryHandler;

    private InventoryRequest inventoryRequest;
    private UpdateQuantityRequest updateQuantityRequest;
    private Inventory inventory;
    private InventoryResponse inventoryResponse;

    @BeforeEach
    void setUp() {
        inventoryRequest = new InventoryRequest(
                5L,    // productId
                100,   // quantity
                "Warehouse A"
        );

        updateQuantityRequest = new UpdateQuantityRequest(
                -10,   // quantityChange
                "PURCHASE"
        );

        inventory = new Inventory(
                1L,
                5L,
                "iPhone 15 Pro",
                100,
                "Warehouse A",
                Instant.now()
        );

        inventoryResponse = new InventoryResponse(
                "1",   // id
                5L,   // productId
                "iPhone 15 Pro",
                100,
                "Warehouse A",
                Instant.now().toString()
        );
    }

    // ========== TESTS OF GET INVENTORY BY PRODUCT ID ==========

    @Test
    void shouldGetInventoryByProductIdSuccessfully() {
        // Given
        Long productId = 5L;
        when(inventoryServicePort.getInventoryByProductId(productId)).thenReturn(inventory);
        when(inventoryResponseMapper.toResponse(inventory)).thenReturn(inventoryResponse);

        // When
        InventoryResponse result = inventoryHandler.getInventoryByProductId(productId);

        // Then
        assertNotNull(result);
        assertEquals(inventoryResponse, result);
        verify(inventoryServicePort, times(1)).getInventoryByProductId(productId);
        verify(inventoryResponseMapper, times(1)).toResponse(inventory);
    }

    // ========== TESTS OF CREATE INVENTORY ==========

    @Test
    void shouldCreateInventorySuccessfully() {
        // Given
        when(inventoryRequestMapper.toDomain(inventoryRequest)).thenReturn(inventory);
        when(inventoryServicePort.createInventory(inventory)).thenReturn(inventory);
        when(inventoryResponseMapper.toResponse(inventory)).thenReturn(inventoryResponse);

        // When
        InventoryResponse result = inventoryHandler.createInventory(inventoryRequest);

        // Then
        assertNotNull(result);
        assertEquals(inventoryResponse, result);
        verify(inventoryRequestMapper, times(1)).toDomain(inventoryRequest);
        verify(inventoryServicePort, times(1)).createInventory(inventory);
        verify(inventoryResponseMapper, times(1)).toResponse(inventory);
    }

    // ========== TESTS OF UPDATE QUANTITY ==========

    @Test
    void shouldUpdateQuantitySuccessfully() {
        // Given
        Long inventoryId = 1L;
        Inventory updatedInventory = new Inventory(
                1L, 5L, "iPhone 15 Pro", 90, "Warehouse A", Instant.now()
        );
        InventoryResponse updatedResponse = new InventoryResponse(
                "1", 5L, "iPhone 15 Pro", 90, "Warehouse A", Instant.now().toString()
        );

        when(inventoryServicePort.updateQuantity(inventoryId, -10, "PURCHASE")).thenReturn(updatedInventory);
        when(inventoryResponseMapper.toResponse(updatedInventory)).thenReturn(updatedResponse);

        // When
        InventoryResponse result = inventoryHandler.updateQuantity(inventoryId, updateQuantityRequest);

        // Then
        assertNotNull(result);
        assertEquals(90, result.getQuantity());
        verify(inventoryServicePort, times(1)).updateQuantity(inventoryId, -10, "PURCHASE");
        verify(inventoryResponseMapper, times(1)).toResponse(updatedInventory);
    }

    @Test
    void shouldUpdateQuantityWithPositiveChangeSuccessfully() {
        // Given
        Long inventoryId = 1L;
        UpdateQuantityRequest addRequest = new UpdateQuantityRequest(20, "STOCK ADJUSTMENT");
        Inventory updatedInventory = new Inventory(
                1L, 5L, "iPhone 15 Pro", 120, "Warehouse A", Instant.now()
        );
        InventoryResponse updatedResponse = new InventoryResponse("1",
                5L,
                "iPhone 15 Pro",
                120,
                "Warehouse A",
                Instant.now().toString());

        when(inventoryServicePort.updateQuantity(inventoryId, 20, "STOCK ADJUSTMENT"))
                .thenReturn(updatedInventory);
        when(inventoryResponseMapper.toResponse(updatedInventory)).thenReturn(updatedResponse);

        // When
        InventoryResponse result = inventoryHandler.updateQuantity(inventoryId, addRequest);

        // Then
        assertNotNull(result);
        assertEquals(120, result.getQuantity());
        verify(inventoryServicePort, times(1)).updateQuantity(inventoryId,
                20,
                "STOCK ADJUSTMENT");
    }

    @Test
    void shouldUpdateQuantityWithNullReason() {
        // Given
        Long inventoryId = 1L;
        UpdateQuantityRequest nullReasonRequest = new UpdateQuantityRequest(-5, null);
        when(inventoryServicePort.updateQuantity(inventoryId, -5, null)).thenReturn(inventory);

        // When
        inventoryHandler.updateQuantity(inventoryId, nullReasonRequest);

        // Then
        verify(inventoryServicePort, times(1))
                .updateQuantity(inventoryId, -5, null);
    }

    // ========== INTEGRATION TESTS ==========

    @Test
    void shouldChainMappersAndServiceCorrectlyInCreate() {
        // Given
        when(inventoryRequestMapper.toDomain(inventoryRequest)).thenReturn(inventory);
        when(inventoryServicePort.createInventory(inventory)).thenReturn(inventory);
        when(inventoryResponseMapper.toResponse(inventory)).thenReturn(inventoryResponse);

        // When
        inventoryHandler.createInventory(inventoryRequest);

        // Then - Verify whole chain: RequestMapper → Service → ResponseMapper
        verify(inventoryRequestMapper, times(1)).toDomain(inventoryRequest);
        verify(inventoryServicePort, times(1)).createInventory(inventory);
        verify(inventoryResponseMapper, times(1)).toResponse(inventory);
    }

    @Test
    void shouldChainServiceAndResponseMapperInGetByProductId() {
        // Given
        Long productId = 5L;
        when(inventoryServicePort.getInventoryByProductId(productId)).thenReturn(inventory);
        when(inventoryResponseMapper.toResponse(inventory)).thenReturn(inventoryResponse);

        // When
        inventoryHandler.getInventoryByProductId(productId);

        // Then
        verify(inventoryServicePort, times(1)).getInventoryByProductId(productId);
        verify(inventoryResponseMapper, times(1)).toResponse(inventory);
    }

    // ========== EDGE CASES ==========

    @Test
    void shouldHandleUpdateQuantityWithNullRequest() {
        // Given
        Long inventoryId = 1L;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            inventoryHandler.updateQuantity(inventoryId, null);
        });
    }
}
