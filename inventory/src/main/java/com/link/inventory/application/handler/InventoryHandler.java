package com.link.inventory.application.handler;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.application.dto.UpdateQuantityRequest;

public interface InventoryHandler {

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse updateQuantity(Long inventoryId, UpdateQuantityRequest request);

}
