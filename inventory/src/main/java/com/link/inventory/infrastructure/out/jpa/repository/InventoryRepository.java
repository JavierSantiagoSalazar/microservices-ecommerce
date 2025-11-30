package com.link.inventory.infrastructure.out.jpa.repository;

import com.link.inventory.infrastructure.out.jpa.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

}
