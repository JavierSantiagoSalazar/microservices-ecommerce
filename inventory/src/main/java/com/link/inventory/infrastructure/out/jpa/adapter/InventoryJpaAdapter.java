package com.link.inventory.infrastructure.out.jpa.adapter;

import com.link.inventory.domain.model.Inventory;
import com.link.inventory.domain.spi.InventoryPersistencePort;
import com.link.inventory.infrastructure.out.jpa.entity.InventoryEntity;
import com.link.inventory.infrastructure.out.jpa.mapper.InventoryEntityMapper;
import com.link.inventory.infrastructure.out.jpa.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class InventoryJpaAdapter implements InventoryPersistencePort {

    private final InventoryRepository inventoryRepository;
    private final InventoryEntityMapper inventoryEntityMapper;

    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(inventoryEntityMapper::toDomain);
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = inventoryEntityMapper.toEntity(inventory);
        InventoryEntity savedEntity = inventoryRepository.save(entity);
        return inventoryEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return inventoryRepository.existsByProductId(productId);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id)
                .map(inventoryEntityMapper::toDomain);
    }

}
