package com.link.product.unit.application.handler;

import com.link.product.application.dto.PageResponse;
import com.link.product.application.dto.ProductRequest;
import com.link.product.application.dto.ProductResponse;
import com.link.product.application.handler.ProductHandlerImpl;
import com.link.product.application.mappers.ProductRequestMapper;
import com.link.product.application.mappers.ProductResponseMapper;
import com.link.product.domain.api.ProductServicePort;
import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductHandlerImplTest {

    @Mock
    private ProductServicePort productServicePort;

    @Mock
    private ProductRequestMapper productRequestMapper;

    @Mock
    private ProductResponseMapper productResponseMapper;

    @InjectMocks
    private ProductHandlerImpl productHandler;

    private ProductRequest productRequest;
    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest(
                "iPhone 15 Pro",
                "Último modelo de Apple",
                4500000L,
                "Electronics",
                "Apple",
                "https://example.com/iphone.jpg"
        );

        product = new Product(
                1L,
                "iPhone 15 Pro",
                "Último modelo de Apple",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone.jpg"
        );

        productResponse = new ProductResponse(
                "1",
                "iPhone 15 Pro",
                "Último modelo de Apple",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone.jpg"
        );
    }

    // ========== TESTS OF CREATE PRODUCT ==========

    @Test
    void shouldCreateProductSuccessfully() {
        // Given
        when(productRequestMapper.toDomain(productRequest)).thenReturn(product);
        when(productServicePort.saveProduct(product)).thenReturn(product);
        when(productResponseMapper.toResponse(product)).thenReturn(productResponse);

        // When
        ProductResponse result = productHandler.createProduct(productRequest);

        // Then
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRequestMapper, times(1)).toDomain(productRequest);
        verify(productServicePort, times(1)).saveProduct(product);
        verify(productResponseMapper, times(1)).toResponse(product);
    }

    // ========== TESTS OF GET PRODUCT BY ID ==========

    @Test
    void shouldGetProductByIdSuccessfully() {
        // Given
        Long id = 1L;
        when(productServicePort.getProductById(id)).thenReturn(product);
        when(productResponseMapper.toResponse(product)).thenReturn(productResponse);

        // When
        ProductResponse result = productHandler.getProductById(id);

        // Then
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productServicePort, times(1)).getProductById(id);
        verify(productResponseMapper, times(1)).toResponse(product);
    }

    // ========== TESTS OF UPDATE PRODUCT BY ID ==========

    @Test
    void shouldUpdateProductByIdSuccessfully() {
        // Given
        Long id = 1L;
        when(productRequestMapper.toDomain(productRequest)).thenReturn(product);
        when(productServicePort.updateProductById(id, product)).thenReturn(product);
        when(productResponseMapper.toResponse(product)).thenReturn(productResponse);

        // When
        ProductResponse result = productHandler.updateProductById(id, productRequest);

        // Then
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRequestMapper, times(1)).toDomain(productRequest);
        verify(productServicePort, times(1)).updateProductById(id, product);
        verify(productResponseMapper, times(1)).toResponse(product);
    }

    // ========== TESTS OF DELETE PRODUCT BY ID ==========

    @Test
    void shouldDeleteProductByIdSuccessfully() {
        // Given
        Long id = 1L;
        doNothing().when(productServicePort).deleteProductById(id);

        // When
        productHandler.deleteProductById(id);

        // Then
        verify(productServicePort, times(1)).deleteProductById(id);
    }

    // ========== TESTS OF GET ALL PRODUCTS ==========

    @Test
    void shouldGetAllProductsSuccessfully() {
        // Given
        Product product2 = new Product(2L,
                "Samsung Galaxy",
                "Android flagship",
                3800000.0,
                "Electronics",
                "Samsung",
                "https://samsung.com/galaxy.jpg");
        List<Product> products = List.of(product, product2);

        PageDomain<Product> pageDomain = new PageDomain<>(
                products, 0, 10, 2, 1, true
        );

        ProductResponse response2 = new ProductResponse("2",
                "Samsung Galaxy",
                "Android flagship",
                3800000.0,
                "Electronics",
                "Samsung",
                "https://samsung.com/galaxy.jpg");
        List<ProductResponse> expectedResponses = List.of(productResponse, response2);

        when(productServicePort.getAllProducts(0, 10, "id", "ASC"))
                .thenReturn(pageDomain);
        when(productResponseMapper.toResponse(product)).thenReturn(productResponse);
        when(productResponseMapper.toResponse(product2)).thenReturn(response2);

        // When
        PageResponse<ProductResponse> result = productHandler.getAllProducts(0,
                10,
                "id",
                "ASC");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(expectedResponses, result.getContent());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());

        verify(productServicePort, times(1))
                .getAllProducts(0, 10, "id", "ASC");
        verify(productResponseMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldGetEmptyPageWhenNoProducts() {
        // Given
        PageDomain<Product> emptyPage
                = new PageDomain<>(List.of(), 0, 10, 0, 0, true);
        when(productServicePort.getAllProducts(0, 10, "id", "ASC")).thenReturn(emptyPage);

        // When
        PageResponse<ProductResponse> result = productHandler.getAllProducts(
                0,
                10,
                "id",
                "ASC"
        );

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(productServicePort, times(1))
                .getAllProducts(0, 10, "id", "ASC");
        verifyNoInteractions(productResponseMapper);
    }

    @Test
    void shouldChainMappersAndServiceCorrectly() {
        // Given
        Long id = 1L;
        when(productRequestMapper.toDomain(productRequest)).thenReturn(product);
        when(productServicePort.updateProductById(id, product)).thenReturn(product);
        when(productResponseMapper.toResponse(product)).thenReturn(productResponse);

        // When
        productHandler.updateProductById(id, productRequest);

        // Then - Verificar cadena completa: RequestMapper → Service → ResponseMapper
        verify(productRequestMapper, times(1)).toDomain(productRequest);
        verify(productServicePort, times(1)).updateProductById(id, product);
        verify(productResponseMapper, times(1)).toResponse(product);
    }
}
