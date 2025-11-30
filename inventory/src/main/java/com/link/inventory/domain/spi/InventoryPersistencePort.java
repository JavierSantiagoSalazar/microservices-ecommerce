package com.link.inventory.domain.spi;


import com.link.inventory.domain.model.Inventory;

import java.util.Optional;

public interface InventoryPersistencePort {

    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findById(Long id);

    Inventory save(Inventory inventory);

    boolean existsByProductId(Long productId);

}
