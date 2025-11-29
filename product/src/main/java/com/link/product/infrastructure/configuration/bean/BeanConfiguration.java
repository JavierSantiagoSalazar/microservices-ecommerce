package com.link.product.infrastructure.configuration.bean;

import com.link.product.domain.api.ProductServicePort;
import com.link.product.domain.spi.ProductPersistencePort;
import com.link.product.domain.usecase.ProductUseCase;
import com.link.product.infrastructure.out.jpa.adapter.ProductJpaAdapter;
import com.link.product.infrastructure.out.jpa.mapper.ProductEntityMapper;
import com.link.product.infrastructure.out.jpa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Bean
    public ProductPersistencePort productPersistencePort() {
        return new ProductJpaAdapter(productRepository, productEntityMapper);
    }

    @Bean
    public ProductServicePort productServicePort() {
        return new ProductUseCase(
                productPersistencePort()
        );
    }

}
