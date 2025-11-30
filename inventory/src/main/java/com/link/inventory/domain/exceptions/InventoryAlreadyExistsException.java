package com.link.inventory.domain.exceptions;

import com.link.inventory.domain.utils.Constants;
import lombok.Getter;

@Getter
public class InventoryAlreadyExistsException extends RuntimeException {

    private final Long productId;

    public InventoryAlreadyExistsException(Long productId) {
        super(Constants.INVENTORY_NOT_FOUND_MESSAGE + productId);
        this.productId = productId;
    }
}
