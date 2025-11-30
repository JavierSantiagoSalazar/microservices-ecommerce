package com.link.product.integration.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.link.product.application.dto.ProductRequest;
import com.link.product.domain.utils.Constants;
import com.link.product.infrastructure.out.jpa.entity.ProductEntity;
import com.link.product.infrastructure.out.jpa.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Value("${api.key.header}")
    private String apiKeyHeader;

    @Value("${api.key.value}")
    private String apiKeyValue;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity testProduct;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").header(apiKeyHeader, apiKeyValue))
                .apply(springSecurity())
                .build();

        productRepository.deleteAll();

        testProduct = new ProductEntity(
                null,
                "iPhone 15 Pro",
                "Último modelo de Apple con chip A17 Pro",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );
        testProduct = productRepository.save(testProduct);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    // ========== TESTS OF CREATE ==========

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        // Given
        ProductRequest newProduct = new ProductRequest(
                "Samsung Galaxy S24",
                "Smartphone flagship con cámara de 200MP",
                3800000L,
                "Electronics",
                "Samsung",
                "https://example.com/galaxys24.jpg"
        );

        // When & Then
        mockMvc.perform(post("/product/")
                        .contentType(Constants.JSON_API_MEDIA_TYPE)
                        .accept(Constants.JSON_API_MEDIA_TYPE)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.productName").value("Samsung Galaxy S24"))
                .andExpect(jsonPath("$.data.price").value(3800000.0))
                .andExpect(jsonPath("$.links.self").exists());
    }

    @Test
    void shouldReturnConflictWhenCreatingDuplicateProduct() throws Exception {
        // Given - duplicated product
        ProductRequest duplicateProduct = new ProductRequest(
                "iPhone 15 Pro",  // Already exists
                "Test",
                1000000L,
                "Electronics",
                "Apple",
                "https://test.com"
        );

        // When & Then
        mockMvc.perform(post("/product/")
                        .contentType(Constants.JSON_API_MEDIA_TYPE)
                        .accept(Constants.JSON_API_MEDIA_TYPE)
                        .content(objectMapper.writeValueAsString(duplicateProduct)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.errors[0].status").value("409"))
                .andExpect(jsonPath("$.errors[0].title").value("Product Already Exists"));
    }

    // ========== TESTS OF GET BY ID ==========

    @Test
    void shouldGetProductByIdSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(get("/product/{id}", testProduct.getId())
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.data.id").value(String.valueOf(testProduct.getId())))
                .andExpect(jsonPath("$.data.productName").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.data.brand").value("Apple"))
                .andExpect(jsonPath("$.links.self").exists());
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // When & Then
        mockMvc.perform(get("/product/{id}", 999L)
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.errors[0].status").value("404"))
                .andExpect(jsonPath("$.errors[0].title").value("Product Not Found"));
    }

    @Test
    void shouldReturnBadRequestForInvalidId() throws Exception {
        // When & Then
        mockMvc.perform(get("/product/{id}", 0)
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    // ========== TESTS OF UPDATE ==========

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        // Given
        ProductRequest updateRequest = new ProductRequest(
                "iPhone 15 Pro Max",
                "Actualizado - Modelo más grande",
                5500000L,
                "Electronics",
                "Apple",
                "https://example.com/iphone15promax.jpg"
        );

        // When & Then
        mockMvc.perform(put("/product/{id}", testProduct.getId())
                        .contentType(Constants.JSON_API_MEDIA_TYPE)
                        .accept(Constants.JSON_API_MEDIA_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.data.id").value(String.valueOf(testProduct.getId())))
                .andExpect(jsonPath("$.data.productName").value("iPhone 15 Pro Max"))
                .andExpect(jsonPath("$.data.price").value(5500000.0))
                .andExpect(jsonPath("$.links.self").exists());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        // Given
        ProductRequest updateRequest = new ProductRequest(
                "Test Product",
                "Test",
                1000000L,
                "Test",
                "Test",
                "https://test.com"
        );

        // When & Then
        mockMvc.perform(put("/product/{id}", 999L)
                        .contentType(Constants.JSON_API_MEDIA_TYPE)
                        .accept(Constants.JSON_API_MEDIA_TYPE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    // ========== TESTS OF DELETE ==========

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/product/{id}", testProduct.getId())
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isNoContent());

        // Verify was deleted
        assert productRepository.findById(testProduct.getId()).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentProduct() throws Exception {
        // When & Then
        mockMvc.perform(delete("/product/{id}", 999L)
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isNotFound());
    }

    // ========== TESTS OF GET ALL ==========

    @Test
    void shouldGetAllProductsWithPagination() throws Exception {
        // Given - Add more products
        productRepository.save(new ProductEntity(null,
                "Samsung S24",
                "Test",
                3800000.0,
                "Electronics",
                "Samsung",
                "url"));
        productRepository.save(new ProductEntity(null,
                "MacBook Pro",
                "Test",
                7000000.0,
                "Computers",
                "Apple",
                "url"));

        // When & Then
        mockMvc.perform(get("/product/")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.links.self").exists())
                .andExpect(jsonPath("$.links.first").exists())
                .andExpect(jsonPath("$.meta.totalElements").value(3))
                .andExpect(jsonPath("$.meta.currentPage").value(0));
    }

    @Test
    void shouldReturnEmptyArrayWhenNoProducts() throws Exception {
        // Given
        productRepository.deleteAll();

        // When & Then
        mockMvc.perform(get("/product/")
                        .accept(Constants.JSON_API_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.meta.totalElements").value(0));
    }

    // ========== TESTS OF VALIDATION ==========

    @Test
    void shouldReturnBadRequestForInvalidProductRequest() throws Exception {
        // Given - Product without name
        ProductRequest invalidProduct = new ProductRequest(
                null,
                "Test",
                1000000L,
                "Test",
                "Test",
                "https://test.com"
        );

        // When & Then
        mockMvc.perform(post("/product/")
                        .contentType(Constants.JSON_API_MEDIA_TYPE)
                        .accept(Constants.JSON_API_MEDIA_TYPE)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].status").value("400"))
                .andExpect(jsonPath("$.errors[0].title").value("Validation Error"));
    }
}
