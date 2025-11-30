package com.link.inventory.domain.api;

import com.link.inventory.domain.model.Inventory;

public interface InventoryServicePort {

    Inventory getInventoryByProductId(Long productId);

    Inventory createInventory(Inventory inventory);

    Inventory updateQuantity(Long inventoryId, Integer quantityChange, String reason);

}
