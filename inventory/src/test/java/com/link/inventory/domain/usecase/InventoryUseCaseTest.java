package com.link.inventory.domain.usecase;

import com.link.inventory.domain.exceptions.InsufficientStockException;
import com.link.inventory.domain.exceptions.InventoryAlreadyExistsException;
import com.link.inventory.domain.exceptions.InventoryNotFoundException;
import com.link.inventory.domain.model.Inventory;
import com.link.inventory.domain.model.Product;
import com.link.inventory.domain.spi.InventoryPersistencePort;
import com.link.inventory.domain.spi.ProductClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryUseCaseTest {

    @Mock
    private InventoryPersistencePort inventoryPersistencePort;

    @Mock
    private ProductClientPort productClientPort;

    @InjectMocks
    private InventoryUseCase inventoryUseCase;

    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setUp() {
        inventory = new Inventory(
                1L, 5L, "iPhone 15 Pro", 100, "Warehouse A", Instant.now()
        );
        product = new Product(5L, "iPhone 15 Pro");
    }

    // ========== TESTS OF GET INVENTORY BY PRODUCT ID ==========

    @Test
    void shouldGetInventoryByProductIdSuccessfully() {
        // Given
        Long productId = 5L;
        when(productClientPort.getProductById(productId)).thenReturn(product);
        when(inventoryPersistencePort.findByProductId(productId)).thenReturn(Optional.of(inventory));

        // When
        Inventory result = inventoryUseCase.getInventoryByProductId(productId);

        // Then
        assertNotNull(result);
        assertEquals("iPhone 15 Pro", result.getProductName());
        verify(productClientPort, times(1)).getProductById(productId);
        verify(inventoryPersistencePort, times(1)).findByProductId(productId);
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFoundByProductId() {
        // Given
        Long productId = 999L;
        when(productClientPort.getProductById(productId)).thenReturn(product);
        when(inventoryPersistencePort.findByProductId(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InventoryNotFoundException.class, () -> {
            inventoryUseCase.getInventoryByProductId(productId);
        });

        verify(productClientPort, times(1)).getProductById(productId);
        verify(inventoryPersistencePort, times(1)).findByProductId(productId);
    }


    // ========== TESTS OF CREATE INVENTORY ==========

    @Test
    void shouldCreateInventorySuccessfully() {
        // Given
        inventory.setId(null);
        when(productClientPort.getProductById(5L)).thenReturn(product);
        when(inventoryPersistencePort.existsByProductId(5L)).thenReturn(false);
        when(inventoryPersistencePort.save(inventory)).thenReturn(inventory);

        // When
        Inventory result = inventoryUseCase.createInventory(inventory);

        // Then
        assertNotNull(result.getLastUpdated());
        verify(productClientPort, times(1)).getProductById(5L);
        verify(inventoryPersistencePort, times(1)).existsByProductId(5L);
        verify(inventoryPersistencePort, times(1)).save(inventory);
        assertEquals("iPhone 15 Pro", result.getProductName());
    }


    @Test
    void shouldThrowExceptionWhenInventoryAlreadyExists() {
        // Given
        when(productClientPort.getProductById(5L)).thenReturn(product);
        when(inventoryPersistencePort.existsByProductId(5L)).thenReturn(true);

        // When & Then
        assertThrows(InventoryAlreadyExistsException.class, () -> {
            inventoryUseCase.createInventory(inventory);
        });

        verify(productClientPort, times(1)).getProductById(5L);
        verify(inventoryPersistencePort, times(1)).existsByProductId(5L);
        verify(inventoryPersistencePort, never()).save(any(Inventory.class));
    }

    // ========== TESTS OF UPDATE QUANTITY ==========

    @Test
    void shouldUpdateQuantitySuccessfullyRemove() {
        // Given
        Long inventoryId = 1L;
        when(inventoryPersistencePort.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryPersistencePort.save(inventory)).thenReturn(inventory);
        when(productClientPort.getProductById(5L)).thenReturn(product);

        // When
        Inventory result = inventoryUseCase.updateQuantity(inventoryId, -10, "PURCHASE");

        // Then
        assertEquals(90, result.getQuantity());
        assertNotNull(result.getLastUpdated());
        verify(inventoryPersistencePort, times(1)).findById(inventoryId);
        verify(inventoryPersistencePort, times(1)).save(inventory);
        verify(productClientPort, times(1)).getProductById(5L);
    }

    @Test
    void shouldUpdateQuantitySuccessfullyAdd() {
        // Given
        Long inventoryId = 1L;
        when(inventoryPersistencePort.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryPersistencePort.save(inventory)).thenReturn(inventory);
        when(productClientPort.getProductById(5L)).thenReturn(product);

        // When
        Inventory result = inventoryUseCase.updateQuantity(inventoryId, 20, "STOCK ADJUSTMENT");

        // Then
        assertEquals(120, result.getQuantity());
        verify(inventoryPersistencePort, times(1)).findById(inventoryId);
        verify(inventoryPersistencePort, times(1)).save(inventory);
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFoundUpdate() {
        // Given
        Long inventoryId = 999L;
        when(inventoryPersistencePort.findById(inventoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InventoryNotFoundException.class, () -> {
            inventoryUseCase.updateQuantity(inventoryId, -10, "PURCHASE");
        });

        verify(inventoryPersistencePort, times(1)).findById(inventoryId);
        verify(inventoryPersistencePort, never()).save(any());
        verify(productClientPort, never()).getProductById(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        // Given
        inventory.setQuantity(5); // Only 5 available
        Long inventoryId = 1L;
        when(inventoryPersistencePort.findById(inventoryId)).thenReturn(Optional.of(inventory));

        // When & Then
        assertThrows(InsufficientStockException.class, () -> {
            inventoryUseCase.updateQuantity(inventoryId, -10, "PURCHASE");
        });

        verify(inventoryPersistencePort, times(1)).findById(inventoryId);
        verify(inventoryPersistencePort, never()).save(any());
    }

    @Test
    void shouldHandleNullReasonInUpdate() {
        // Given
        Long inventoryId = 1L;
        when(inventoryPersistencePort.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryPersistencePort.save(inventory)).thenReturn(inventory);
        when(productClientPort.getProductById(5L)).thenReturn(product);

        // When
        inventoryUseCase.updateQuantity(inventoryId, -5, null);

        // Then - No exception, uses "NOT_SPECIFIED_REASON"
        verify(inventoryPersistencePort, times(1)).save(inventory);
    }
}