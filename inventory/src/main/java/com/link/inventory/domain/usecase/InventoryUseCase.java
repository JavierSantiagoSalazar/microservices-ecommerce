package com.link.inventory.domain.usecase;

import com.link.inventory.domain.exceptions.InsufficientStockException;
import com.link.inventory.domain.exceptions.InventoryAlreadyExistsException;
import com.link.inventory.domain.exceptions.InventoryNotFoundException;
import com.link.inventory.domain.model.Inventory;
import com.link.inventory.domain.model.Product;
import com.link.inventory.domain.api.InventoryServicePort;
import com.link.inventory.domain.spi.InventoryPersistencePort;
import com.link.inventory.domain.spi.ProductClientPort;
import com.link.inventory.domain.utils.Constants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@AllArgsConstructor
public class InventoryUseCase implements InventoryServicePort {

    private final InventoryPersistencePort inventoryPersistencePort;
    private final ProductClientPort productClientPort;

    private static final Logger log = LoggerFactory.getLogger(InventoryUseCase.class);

    @Override
    public Inventory getInventoryByProductId(Long productId) {

        Product product = productClientPort.getProductById(productId);

        Inventory inventory = inventoryPersistencePort.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));

        inventory.setProductName(product.getProductName());

        return inventory;

    }

    @Override
    public Inventory createInventory(Inventory inventory) {

        Product product = productClientPort.getProductById(inventory.getProductId());

        if (inventoryPersistencePort.existsByProductId(inventory.getProductId())) {
            throw new InventoryAlreadyExistsException(inventory.getProductId());
        }

        inventory.setLastUpdated(Instant.now());

        Inventory savedInventory = inventoryPersistencePort.save(inventory);

        savedInventory.setProductName(product.getProductName());

        return savedInventory;

    }


    @Override
    public Inventory updateQuantity(Long inventoryId, Integer quantityChange, String reason) {

        Inventory inventory = inventoryPersistencePort.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        int oldQuantity = inventory.getQuantity();
        int newQuantity = oldQuantity + quantityChange;

        if (newQuantity < Constants.ZERO) {
            throw new InsufficientStockException(inventoryId, oldQuantity, Math.abs(quantityChange));
        }

        inventory.setQuantity(newQuantity);
        inventory.setLastUpdated(Instant.now());

        Inventory updatedInventory = inventoryPersistencePort.save(inventory);

        String operation = quantityChange > Constants.ZERO
                ? Constants.INVENTORY_OPERATION_ADDED
                : Constants.INVENTORY_OPERATION_REMOVED;

        log.info(
                Constants.INVENTORY_CHANGED_LOG_MESSAGE,
                updatedInventory.getId(),
                updatedInventory.getProductId(),
                operation,
                Math.abs(quantityChange),
                oldQuantity,
                newQuantity,
                reason != null ? reason : Constants.NOT_SPECIFIED_REASON
        );

        Product product = productClientPort.getProductById(updatedInventory.getProductId());
        updatedInventory.setProductName(product.getProductName());

        return updatedInventory;

    }
}
