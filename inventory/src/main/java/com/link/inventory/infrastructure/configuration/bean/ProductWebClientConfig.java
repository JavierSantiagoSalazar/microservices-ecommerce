package com.link.inventory.infrastructure.configuration.bean;

import com.link.inventory.domain.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ProductWebClientConfig {

    @Bean
    public WebClient productWebClient(
            @Value("${product.service.url}") String baseUrl,
            @Value("${app.api.key}") String apiKey
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-API-Key", apiKey)
                .defaultHeader("Content-Type", Constants.JSON_API_MEDIA_TYPE)
                .build();
    }
}
