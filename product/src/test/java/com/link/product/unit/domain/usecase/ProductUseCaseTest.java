package com.link.product.unit.domain.usecase;


import com.link.product.domain.exceptions.ProductAlreadyExistsException;
import com.link.product.domain.exceptions.ProductNotFoundException;
import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;
import com.link.product.domain.spi.ProductPersistencePort;
import com.link.product.domain.usecase.ProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductPersistencePort productPersistencePort;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(
                1L,
                "iPhone 15 Pro",
                "Último modelo de Apple con chip A17 Pro",
                4500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );
    }

    // ========== TESTS OF SAVE PRODUCT ==========

    @Test
    void shouldSaveProductSuccessfully() {
        // Given
        when(productPersistencePort.checkIfProductExists(product.getProductName())).thenReturn(false);
        when(productPersistencePort.saveProduct(any(Product.class))).thenReturn(product);

        // When
        Product savedProduct = productUseCase.saveProduct(product);

        // Then
        assertNotNull(savedProduct);
        assertEquals("iPhone 15 Pro", savedProduct.getProductName());
        verify(productPersistencePort, times(1)).checkIfProductExists(product.getProductName());
        verify(productPersistencePort, times(1)).saveProduct(product);
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExists() {
        // Given
        when(productPersistencePort.checkIfProductExists(product.getProductName())).thenReturn(true);

        // When & Then
        assertThrows(ProductAlreadyExistsException.class, () -> {
            productUseCase.saveProduct(product);
        });

        verify(productPersistencePort, times(1)).checkIfProductExists(product.getProductName());
        verify(productPersistencePort, never()).saveProduct(any(Product.class));
    }

    // ========== TESTS OF GET PRODUCT BY ID ==========

    @Test
    void shouldGetProductByIdSuccessfully() {
        // Given
        Long productId = 1L;
        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.of(product));

        // When
        Product foundProduct = productUseCase.getProductById(productId);

        // Then
        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getId());
        assertEquals("iPhone 15 Pro", foundProduct.getProductName());
        verify(productPersistencePort, times(1)).getProductById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        Long productId = 999L;
        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productUseCase.getProductById(productId);
        });

        assertEquals(productId, exception.getNotFoundId());
        verify(productPersistencePort, times(1)).getProductById(productId);
    }

    // ========== TESTS OF UPDATE PRODUCT ==========

    @Test
    void shouldUpdateProductSuccessfully() {
        // Given
        Long productId = 1L;
        Product updatedProduct = new Product(
                1L,
                "iPhone 15 Pro Max",
                "Actualizado - Modelo más grande",
                5500000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15promax.jpg"
        );

        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.of(product));
        when(productPersistencePort.checkIfProductExists(updatedProduct.getProductName())).thenReturn(false);
        when(productPersistencePort.updateProductById(eq(productId), any(Product.class)))
                .thenReturn(Optional.of(updatedProduct));

        // When
        Product result = productUseCase.updateProductById(productId, updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals("iPhone 15 Pro Max", result.getProductName());
        assertEquals(5500000.0, result.getPrice());
        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, times(1))
                .checkIfProductExists(updatedProduct.getProductName());
        verify(productPersistencePort, times(1)).updateProductById(productId, updatedProduct);
    }

    @Test
    void shouldUpdateProductWithSameNameSuccessfully() {
        // Given - Update with no name change
        Long productId = 1L;
        Product updatedProduct = new Product(
                1L,
                "iPhone 15 Pro", // Same name
                "Nueva descripción",
                4800000.0,
                "Electronics",
                "Apple",
                "https://example.com/iphone15pro.jpg"
        );

        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.of(product));
        when(productPersistencePort.updateProductById(eq(productId), any(Product.class)))
                .thenReturn(Optional.of(updatedProduct));

        // When
        Product result = productUseCase.updateProductById(productId, updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals("iPhone 15 Pro", result.getProductName());
        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, never()).checkIfProductExists(anyString()); // Must not validate if name change
        verify(productPersistencePort, times(1))
                .updateProductById(productId, updatedProduct);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToExistingProductName() {
        // Given
        Long productId = 1L;
        Product updatedProduct = new Product(
                1L,
                "Samsung Galaxy S24", // Name already exists in other product
                "Actualizado",
                4500000.0,
                "Electronics",
                "Samsung",
                "https://example.com/galaxys24.jpg"
        );

        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.of(product));
        when(productPersistencePort.checkIfProductExists(updatedProduct.getProductName())).thenReturn(true);

        // When & Then
        assertThrows(ProductAlreadyExistsException.class, () -> {
            productUseCase.updateProductById(productId, updatedProduct);
        });

        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, times(1))
                .checkIfProductExists(updatedProduct.getProductName());
        verify(productPersistencePort, never()).updateProductById(anyLong(), any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        // Given
        Long productId = 999L;
        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productUseCase.updateProductById(productId, product);
        });

        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, never()).checkIfProductExists(anyString());
        verify(productPersistencePort, never()).updateProductById(anyLong(), any(Product.class));
    }

    // ========== TESTS OF DELETE PRODUCT ==========

    @Test
    void shouldDeleteProductSuccessfully() {
        // Given
        Long productId = 1L;
        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productPersistencePort).deleteProductById(productId);

        // When
        productUseCase.deleteProductById(productId);

        // Then
        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, times(1)).deleteProductById(productId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // Given
        Long productId = 999L;
        when(productPersistencePort.getProductById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productUseCase.deleteProductById(productId);
        });

        verify(productPersistencePort, times(1)).getProductById(productId);
        verify(productPersistencePort, never()).deleteProductById(anyLong());
    }

    // ========== TESTS OF GET ALL PRODUCTS ==========

    @Test
    void shouldGetAllProductsSuccessfully() {
        // Given
        Product product2 = new Product(
                2L,
                "Samsung Galaxy S24",
                "Smartphone flagship con cámara de 200MP",
                3800000.0,
                "Electronics",
                "Samsung",
                "https://example.com/galaxys24.jpg"
        );

        List<Product> products = List.of(product, product2);
        PageDomain<Product> pageDomain = new PageDomain<>(products,
                0,
                10,
                2,
                1,
                true);

        when(productPersistencePort.getAllProducts(0, 10, "id", "ASC"))
                .thenReturn(pageDomain);

        // When
        PageDomain<Product> result = productUseCase.getAllProducts(0, 10, "id", "ASC");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
        verify(productPersistencePort, times(1))
                .getAllProducts(0, 10, "id", "ASC");
    }

    @Test
    void shouldGetEmptyPageWhenNoProducts() {
        // Given
        PageDomain<Product> emptyPage = new PageDomain<>(List.of(),
                0,
                10,
                0,
                0,
                true
        );
        when(productPersistencePort.getAllProducts(0, 10, "id", "ASC"))
                .thenReturn(emptyPage);

        // When
        PageDomain<Product> result = productUseCase.getAllProducts(0, 10, "id", "ASC");

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(productPersistencePort, times(1))
                .getAllProducts(0, 10, "id", "ASC");
    }
}