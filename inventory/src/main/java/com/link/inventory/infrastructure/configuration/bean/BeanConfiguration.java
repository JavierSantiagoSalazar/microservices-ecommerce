package com.link.inventory.infrastructure.configuration.bean;

import com.link.inventory.domain.api.InventoryServicePort;
import com.link.inventory.domain.spi.InventoryPersistencePort;
import com.link.inventory.domain.spi.ProductClientPort;
import com.link.inventory.domain.usecase.InventoryUseCase;
import com.link.inventory.infrastructure.out.jpa.adapter.InventoryJpaAdapter;
import com.link.inventory.infrastructure.out.jpa.mapper.InventoryEntityMapper;
import com.link.inventory.infrastructure.out.jpa.repository.InventoryRepository;
import com.link.inventory.infrastructure.out.webclient.ProductWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final InventoryRepository inventoryRepository;
    private final InventoryEntityMapper inventoryEntityMapper;
    private final @Qualifier("productWebClient") WebClient productWebClient;

    @Bean
    public InventoryPersistencePort inventoryPersistencePort() {
        return new InventoryJpaAdapter(inventoryRepository, inventoryEntityMapper);
    }

    @Bean
    public ProductClientPort productClientPort() {
        return new ProductWebClient(productWebClient);
    }

    @Bean
    public InventoryServicePort inventoryServicePort() {
        return new InventoryUseCase(
                inventoryPersistencePort(),
                productClientPort()
        );
    }

}
