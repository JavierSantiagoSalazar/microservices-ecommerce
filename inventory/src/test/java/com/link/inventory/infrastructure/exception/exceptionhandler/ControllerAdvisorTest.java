package com.link.inventory.infrastructure.exception.exceptionhandler;

import com.link.inventory.domain.exceptions.InsufficientStockException;
import com.link.inventory.domain.exceptions.InventoryAlreadyExistsException;
import com.link.inventory.domain.exceptions.InventoryNotFoundException;
import com.link.inventory.domain.exceptions.ProductNotValidException;
import com.link.inventory.domain.utils.Constants;
import com.link.inventory.infrastructure.exception.exceptionhandler.dto.JsonApiError;
import com.link.inventory.infrastructure.exception.exceptionhandler.dto.JsonApiErrorResponse;
import io.katharsis.errorhandling.exception.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.ConnectException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerAdvisorTest {

    @InjectMocks
    private ControllerAdvisor controllerAdvisor;

    // ========== TESTS OF INVENTORY EXCEPTIONS ==========

    @Test
    void shouldHandleInventoryAlreadyExistsException() {
        // Given
        InventoryAlreadyExistsException exception = new InventoryAlreadyExistsException(5L);

        // When
        ResponseEntity<JsonApiErrorResponse> response
                = controllerAdvisor.handleInventoryAlreadyExistsException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                Constants.JSON_API_MEDIA_TYPE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("409", error.getStatus());
        assertEquals(Constants.INVENTORY_ALREADY_EXISTS_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("5"));
    }

    @Test
    void shouldHandleInventoryNotFoundException() {
        // Given
        InventoryNotFoundException exception = new InventoryNotFoundException(999L);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleInventoryNotFoundException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("404", error.getStatus());
        assertEquals(Constants.INVENTORY_NOT_FOUND_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("999"));
    }

    @Test
    void shouldHandleProductNotValidException() {
        // Given
        ProductNotValidException exception = new ProductNotValidException(999L);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleProductNotValidException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("404", error.getStatus());
        assertEquals(Constants.PRODUCT_NOT_FOUND_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("999"));
    }

    @Test
    void shouldHandleWebClientResponseException() {
        // Given
        WebClientResponseException exception = new WebClientResponseException(401, "Unauthorized", null, null, null);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleWebClientResponseException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("401", error.getStatus());
        assertEquals(Constants.PRODUCT_SERVICE_ERROR_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("401 Unauthorized"));
    }

    @Test
    void shouldHandleConnectException() {
        // Given
        ConnectException exception = new ConnectException("Connection refused");

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleConnectException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("503", error.getStatus());
        assertEquals(Constants.SERVICE_UNAVAILABLE_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("Connection refused"));
    }

    @Test
    void shouldHandleInsufficientStockException() {
        // Given
        InsufficientStockException exception = new InsufficientStockException(1L, 5, 10);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleInsufficientStockException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("409", error.getStatus());
        assertEquals(Constants.INSUFFICIENT_STOCK_TITLE, error.getTitle());
        assertTrue(error.getDetail().contains("5") && error.getDetail().contains("10"));
    }

    @Test
    void shouldHandleBadRequestException() {
        // Given
        BadRequestException exception = new BadRequestException("Invalid request format");

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleBadRequestException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        JsonApiError error = errorResponse.getErrors().get(0);
        assertEquals("400", error.getStatus());
        assertEquals(Constants.BAD_REQUEST_TITLE, error.getTitle());
        assertEquals("Invalid request format", error.getDetail());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError1 = new FieldError("inventoryRequest",
                "productId",
                "Product ID is required");
        FieldError fieldError2 = new FieldError("inventoryRequest",
                "quantity",
                "Quantity must be >= 0");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(2, errorResponse.getErrors().size());

        // First error
        JsonApiError error1 = errorResponse.getErrors().get(0);
        assertEquals("400", error1.getStatus());
        assertEquals(Constants.VALIDATION_ERROR_TITLE, error1.getTitle());
        assertEquals("Product ID is required", error1.getDetail());
        assertEquals("productId", error1.getSource());

        // Second error
        JsonApiError error2 = errorResponse.getErrors().get(1);
        assertEquals("400", error2.getStatus());
        assertEquals(Constants.VALIDATION_ERROR_TITLE, error2.getTitle());
        assertEquals("Quantity must be >= 0", error2.getDetail());
        assertEquals("quantity", error2.getSource());
    }

    @Test
    void shouldHandleConstraintViolationException() {
        // Given
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);

        Path path1 = mock(Path.class);
        Path path2 = mock(Path.class);

        when(path1.toString()).thenReturn("inventoryRequest.productId");
        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("must be greater than or equal to 1");

        when(path2.toString()).thenReturn("inventoryRequest.quantity");
        when(violation2.getPropertyPath()).thenReturn(path2);
        when(violation2.getMessage()).thenReturn("must not be null");

        ConstraintViolationException exception = new ConstraintViolationException(
                Set.of(violation1, violation2)
        );

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleConstraintViolationException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertEquals(2, errorResponse.getErrors().size());

        errorResponse.getErrors().forEach(error -> {
            assertEquals("400", error.getStatus());
            assertEquals(Constants.CONSTRAINT_VIOLATION_TITLE, error.getTitle());
            assertNotNull(error.getDetail());
            assertNotNull(error.getSource());
        });
    }

    @Test
    void shouldAlwaysReturnJsonApiMediaType() {
        // Test multiple handlers return JSON:API
        InventoryNotFoundException notFoundEx = new InventoryNotFoundException(1L);
        InsufficientStockException stockEx = new InsufficientStockException(1L, 5, 10);

        ResponseEntity<JsonApiErrorResponse> response1 = controllerAdvisor.handleInventoryNotFoundException(notFoundEx);
        ResponseEntity<JsonApiErrorResponse> response2 = controllerAdvisor.handleInsufficientStockException(stockEx);

        assertEquals(
                Constants.JSON_API_MEDIA_TYPE,
                Objects.requireNonNull(response1.getHeaders().getContentType()).toString()
        );
        assertEquals(
                Constants.JSON_API_MEDIA_TYPE,
                Objects.requireNonNull(response2.getHeaders().getContentType()).toString()
        );
    }
}
