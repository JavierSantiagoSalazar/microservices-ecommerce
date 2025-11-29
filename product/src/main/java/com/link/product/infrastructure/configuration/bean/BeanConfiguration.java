package com.pragma.emazon_stock.infrastructure.configuration.bean;

import com.pragma.emazon_stock.domain.api.ArticleServicePort;
import com.pragma.emazon_stock.domain.api.BrandServicePort;
import com.pragma.emazon_stock.domain.api.CategoryServicePort;
import com.pragma.emazon_stock.domain.spi.ArticlePersistencePort;
import com.pragma.emazon_stock.domain.spi.BrandPersistencePort;
import com.pragma.emazon_stock.domain.spi.CategoryPersistencePort;
import com.pragma.emazon_stock.domain.spi.FeignClientPort;
import com.pragma.emazon_stock.domain.usecase.ArticleUseCase;
import com.pragma.emazon_stock.domain.usecase.BrandUseCase;
import com.pragma.emazon_stock.domain.usecase.CategoryUseCase;
import com.pragma.emazon_stock.infrastructure.configuration.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.pragma.emazon_stock.infrastructure.feing.TransactionFeignClient;
import com.pragma.emazon_stock.infrastructure.out.feing.adapter.FeignClientAdapter;
import com.pragma.emazon_stock.infrastructure.out.feing.mapper.SupplyTransactionRequestMapper;
import com.pragma.emazon_stock.infrastructure.out.jpa.adapter.ArticleJpaAdapter;
import com.pragma.emazon_stock.infrastructure.out.jpa.adapter.BrandJpaAdapter;
import com.pragma.emazon_stock.infrastructure.out.jpa.adapter.CategoryJpaAdapter;
import com.pragma.emazon_stock.infrastructure.out.jpa.mapper.ArticleEntityMapper;
import com.pragma.emazon_stock.infrastructure.out.jpa.mapper.BrandEntityMapper;
import com.pragma.emazon_stock.infrastructure.out.jpa.mapper.CategoryEntityMapper;
import com.pragma.emazon_stock.infrastructure.out.jpa.repository.ArticleRepository;
import com.pragma.emazon_stock.infrastructure.out.jpa.repository.BrandRepository;
import com.pragma.emazon_stock.infrastructure.out.jpa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    private final BrandRepository brandRepository;
    private final BrandEntityMapper brandEntityMapper;

    private final ArticleRepository articleRepository;
    private final ArticleEntityMapper articleEntityMapper;

    private final TransactionFeignClient transactionFeignClient;
    private final SupplyTransactionRequestMapper supplyTransactionRequestMapper;

    @Bean
    public CategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public CategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public BrandPersistencePort brandPersistencePort() {
        return new BrandJpaAdapter(brandRepository, brandEntityMapper);
    }

    @Bean
    public BrandServicePort brandServicePort() {
        return new BrandUseCase(brandPersistencePort());
    }

    @Bean
    public FeignClientPort feignClientPort() {
        return new FeignClientAdapter(transactionFeignClient, supplyTransactionRequestMapper);
    }

    @Bean
    public ArticlePersistencePort articlePersistencePort() {
        return new ArticleJpaAdapter(articleRepository, articleEntityMapper);
    }

    @Bean
    public ArticleServicePort articleServicePort() {
        return new ArticleUseCase(
                articlePersistencePort(),
                categoryPersistencePort(),
                brandPersistencePort(),
                feignClientPort()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

}
