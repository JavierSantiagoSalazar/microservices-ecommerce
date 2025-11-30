package com.link.inventory.application.handler;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.application.dto.UpdateQuantityRequest;
import com.link.inventory.application.mappers.InventoryRequestMapper;
import com.link.inventory.application.mappers.InventoryResponseMapper;
import com.link.inventory.domain.api.InventoryServicePort;
import com.link.inventory.domain.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryHandlerImpl implements InventoryHandler {

    private final InventoryServicePort inventoryServicePort;
    private final InventoryResponseMapper inventoryResponseMapper;
    private final InventoryRequestMapper inventoryRequestMapper;

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory = inventoryServicePort.getInventoryByProductId(productId);
        return inventoryResponseMapper.toResponse(inventory);

    }

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

        Inventory inventory = inventoryRequestMapper.toDomain(request);
        Inventory savedInventory = inventoryServicePort.createInventory(inventory);
        return inventoryResponseMapper.toResponse(savedInventory);

    }

    @Override
    public InventoryResponse updateQuantity(Long inventoryId, UpdateQuantityRequest request) {

        Inventory updatedInventory = inventoryServicePort.updateQuantity(
                inventoryId,
                request.getQuantityChange(),
                request.getReason()
        );
        return inventoryResponseMapper.toResponse(updatedInventory);

    }

}
