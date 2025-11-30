package com.link.inventory.domain.exceptions;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final Long inventoryId;
    private final Integer availableStock;
    private final Integer requestedQuantity;

    public InsufficientStockException(Long inventoryId, Integer availableStock, Integer requestedQuantity) {
        super(String.format("Insufficient stock. Available: %d, Requested: %d", availableStock, requestedQuantity));
        this.inventoryId = inventoryId;
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }
}
