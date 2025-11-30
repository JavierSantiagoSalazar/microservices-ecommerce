package com.link.inventory.infrastructure.out.webclient;

import com.link.inventory.domain.exceptions.ProductNotValidException;
import com.link.inventory.domain.model.Product;
import com.link.inventory.domain.spi.ProductClientPort;
import com.link.inventory.domain.utils.Constants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class ProductWebClient implements ProductClientPort {

    private final WebClient productWebClient;

    @Override
    @CircuitBreaker(name = Constants.CIRCUIT_BREAKER_NAME, fallbackMethod = Constants.FALLBACK_METHOD_NAME)
    @Retry(name = Constants.RETRY_NAME)
    public Product getProductById(Long productId) {

        log.debug(Constants.LOG_CALLING_PRODUCT_SERVICE, productId);

        return productWebClient.get()
                .uri(Constants.URI_PRODUCT_ID, productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new ProductNotValidException(productId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new RuntimeException(Constants.PRODUCT_SERVICE_UNAVAILABLE_MESSAGE)
                        )
                )
                .bodyToMono(JsonApiProductResponse.class)
                .map(response -> toProduct(response.getData()))
                .timeout(Duration.ofSeconds(Constants.WEBCLIENT_RESPONSE_TIMEOUT_SECONDS))
                .block(Duration.ofSeconds(Constants.WEBCLIENT_BLOCK_TIMEOUT_SECONDS));

    }

    private Product getProductByIdFallback(Long productId, Throwable throwable) {
        log.error(Constants.LOG_CIRCUIT_BREAKER_OPEN, productId);
        throw new RuntimeException(Constants.PRODUCT_SERVICE_UNAVAILABLE_MESSAGE, throwable);
    }

    private Product toProduct(ProductData data) {

        if (data == null || data.getId() == null) {
            throw new ProductNotValidException(-1L);
        }

        return new Product(Long.parseLong(data.getId()), data.getProductName());

    }

    @lombok.Data
    private static class JsonApiProductResponse {

        private ProductData data;
        private Links links;

    }

    @lombok.Data
    private static class ProductData {

        private String id;
        private String productName;
        private String description;
        private Double price;
        private String category;
        private String brand;
        private String imageUrl;

    }

    @lombok.Data
    private static class Links {
        private String self;
    }

}
