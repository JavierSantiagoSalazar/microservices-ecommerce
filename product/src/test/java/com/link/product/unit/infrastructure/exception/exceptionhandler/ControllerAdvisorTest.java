package com.link.product.unit.infrastructure.exception.exceptionhandler;

import com.link.product.domain.exceptions.NoContentProductException;
import com.link.product.domain.exceptions.ProductAlreadyExistsException;
import com.link.product.domain.exceptions.ProductNotFoundException;
import com.link.product.domain.utils.Constants;
import com.link.product.infrastructure.exception.exceptionhandler.ControllerAdvisor;
import com.link.product.infrastructure.exception.exceptionhandler.dto.JsonApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.ConnectException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerAdvisorTest {

    @InjectMocks
    private ControllerAdvisor controllerAdvisor;

    // ========== TESTS OF ProductAlreadyExistsException ==========

    @Test
    void shouldHandleProductAlreadyExistsException() {
        // Given
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException();

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleProductAlreadyExistsException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("409", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Product Already Exists", errorResponse.getErrors().get(0).getTitle());
        assertEquals(Constants.PRODUCT_ALREADY_EXISTS_EXCEPTION_MESSAGE, errorResponse.getErrors().get(0).getDetail());
    }

    // ========== TESTS OF ProductNotFoundException ==========

    @Test
    void shouldHandleProductNotFoundException() {
        // Given
        Long productId = 999L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleProductNotFoundException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("404", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Product Not Found", errorResponse.getErrors().get(0).getTitle());
        assertTrue(errorResponse.getErrors().get(0).getDetail().contains(String.valueOf(productId)));
    }

    // ========== TESTS OF NoContentProductException ==========

    @Test
    void shouldHandleNoContentProductException() {
        // Given
        NoContentProductException exception = new NoContentProductException();

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleNoContentProductException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("204", errorResponse.getErrors().get(0).getStatus());
        assertEquals("No Products Found", errorResponse.getErrors().get(0).getTitle());
        assertEquals(Constants.PRODUCT_NO_CONTENT_MESSAGE, errorResponse.getErrors().get(0).getDetail());
    }

    // ========== TESTS OF ConnectException ==========

    @Test
    void shouldHandleConnectException() {
        // Given
        String errorMessage = "Connection refused to service";
        ConnectException exception = new ConnectException(errorMessage);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleConnectException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("503", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Service Unavailable", errorResponse.getErrors().get(0).getTitle());
        assertEquals(errorMessage, errorResponse.getErrors().get(0).getDetail());
    }

    // ========== TESTS OF BadRequestException ==========

    @Test
    void shouldHandleBadRequestException() {
        // Given
        String errorMessage = "Invalid request format";
        BadRequestException exception = new BadRequestException(errorMessage);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleBadRequestException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("400", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Bad Request", errorResponse.getErrors().get(0).getTitle());
        assertEquals(errorMessage, errorResponse.getErrors().get(0).getDetail());
    }

    // ========== TESTS OF MethodArgumentNotValidException ==========

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError1 = new FieldError("productRequest",
                "productName",
                "Product name must not be empty"
        );
        FieldError fieldError2 = new FieldError("productRequest",
                "price",
                "Price must be greater than 0"
        );

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(2, errorResponse.getErrors().size());

        // Verify first error
        assertEquals("400", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Validation Error", errorResponse.getErrors().get(0).getTitle());
        assertEquals("Product name must not be empty", errorResponse.getErrors().get(0).getDetail());
        assertEquals("productName", errorResponse.getErrors().get(0).getSource());

        // Verify second error
        assertEquals("400", errorResponse.getErrors().get(1).getStatus());
        assertEquals("Validation Error", errorResponse.getErrors().get(1).getTitle());
        assertEquals("Price must be greater than 0", errorResponse.getErrors().get(1).getDetail());
        assertEquals("price", errorResponse.getErrors().get(1).getSource());
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionWithSingleError() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError = new FieldError("productRequest",
                "productName",
                "Product name must not be empty"
        );

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());
    }

    // ========== TESTS OF ConstraintViolationException ==========

    @Test
    void shouldHandleConstraintViolationException() {
        // Given
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);

        Path path1 = mock(Path.class);
        Path path2 = mock(Path.class);

        when(path1.toString()).thenReturn("getProductById.id");
        when(path2.toString()).thenReturn("updateProductById.id");

        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("must be greater than or equal to 1");

        when(violation2.getPropertyPath()).thenReturn(path2);
        when(violation2.getMessage()).thenReturn("must be greater than or equal to 1");

        ConstraintViolationException exception = new ConstraintViolationException(
                "Constraint violations",
                Set.of(violation1, violation2)
        );

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleConstraintViolationException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, response.getHeaders().getContentType().toString());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getErrors());
        assertEquals(2, errorResponse.getErrors().size());

        // Verify that all errors are 400
        errorResponse.getErrors().forEach(error -> {
            assertEquals("400", error.getStatus());
            assertEquals("Constraint Violation", error.getTitle());
            assertNotNull(error.getDetail());
            assertNotNull(error.getSource());
        });
    }

    @Test
    void shouldHandleConstraintViolationExceptionWithSingleViolation() {
        // Given
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("deleteProductById.id");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be greater than or equal to 1");

        ConstraintViolationException exception = new ConstraintViolationException(
                "Constraint violation",
                Set.of(violation)
        );

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleConstraintViolationException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonApiErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getErrors().size());

        assertEquals("400", errorResponse.getErrors().get(0).getStatus());
        assertEquals("Constraint Violation", errorResponse.getErrors().get(0).getTitle());
        assertEquals("must be greater than or equal to 1", errorResponse.getErrors().get(0).getDetail());
        assertEquals("deleteProductById.id", errorResponse.getErrors().get(0).getSource());
    }

    // ========== TESTS OF JSON:API ==========

    @Test
    void shouldReturnJsonApiMediaTypeInAllResponses() {
        // Given
        ProductNotFoundException notFoundException = new ProductNotFoundException(1L);
        ProductAlreadyExistsException conflictException = new ProductAlreadyExistsException();
        BadRequestException badRequestException = new BadRequestException("Bad request");

        // When
        ResponseEntity<JsonApiErrorResponse> response1 = controllerAdvisor
                .handleProductNotFoundException(notFoundException);
        ResponseEntity<JsonApiErrorResponse> response2 = controllerAdvisor
                .handleProductAlreadyExistsException(conflictException);
        ResponseEntity<JsonApiErrorResponse> response3 = controllerAdvisor
                .handleBadRequestException(badRequestException);

        // Then
        assertEquals(Constants.JSON_API_MEDIA_TYPE, Objects
                .requireNonNull(response1.getHeaders().getContentType()).toString());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, Objects
                .requireNonNull(response2.getHeaders().getContentType()).toString());
        assertEquals(Constants.JSON_API_MEDIA_TYPE, Objects
                .requireNonNull(response3.getHeaders().getContentType()).toString());
    }

    @Test
    void shouldAlwaysReturnErrorsArrayInJsonApiFormat() {
        // Given
        ProductNotFoundException exception = new ProductNotFoundException(1L);

        // When
        ResponseEntity<JsonApiErrorResponse> response = controllerAdvisor.handleProductNotFoundException(exception);

        // Then
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getErrors());
        assertFalse(response.getBody().getErrors().isEmpty());

        // JSON: API Structure verification
        response.getBody().getErrors().forEach(error -> {
            assertNotNull(error.getStatus(), "El campo 'status' no debe ser null");
            assertNotNull(error.getTitle(), "El campo 'title' no debe ser null");
            assertNotNull(error.getDetail(), "El campo 'detail' no debe ser null");
        });
    }
}
