package com.link.inventory.infrastructure.exception.exceptionhandler;

import com.link.inventory.domain.exceptions.InsufficientStockException;
import com.link.inventory.domain.exceptions.InventoryAlreadyExistsException;
import com.link.inventory.domain.exceptions.InventoryNotFoundException;
import com.link.inventory.domain.exceptions.ProductNotValidException;
import com.link.inventory.domain.utils.Constants;
import com.link.inventory.infrastructure.exception.exceptionhandler.dto.JsonApiError;
import com.link.inventory.infrastructure.exception.exceptionhandler.dto.JsonApiErrorResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.katharsis.errorhandling.exception.BadRequestException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<JsonApiErrorResponse> handleInventoryAlreadyExistsException(
            InventoryAlreadyExistsException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.CONFLICT.value()),
                Constants.INVENTORY_ALREADY_EXISTS_TITLE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<JsonApiErrorResponse> handleInventoryNotFoundException(
            InventoryNotFoundException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                Constants.INVENTORY_NOT_FOUND_TITLE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(ProductNotValidException.class)
    public ResponseEntity<JsonApiErrorResponse> handleProductNotValidException(
            ProductNotValidException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                Constants.PRODUCT_NOT_FOUND_TITLE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<JsonApiErrorResponse> handleWebClientResponseException(
            WebClientResponseException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(ex.getStatusCode().value()),
                Constants.PRODUCT_SERVICE_ERROR_TITLE,
                Constants.PRODUCT_SERVICE_ERROR_MESSAGE + ": " + ex.getMessage(),
                null
        );

        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<JsonApiErrorResponse> handleWebClientRequestException(
            WebClientRequestException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                Constants.SERVICE_UNAVAILABLE_TITLE,
                Constants.PRODUCT_SERVICE_UNAVAILABLE_MESSAGE + ": " + ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<JsonApiErrorResponse> handleConnectException(
            ConnectException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                Constants.SERVICE_UNAVAILABLE_TITLE,
                Constants.PRODUCT_SERVICE_DOWN_MESSAGE + ": " + ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<JsonApiErrorResponse> handleBadRequestException(
            BadRequestException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                Constants.BAD_REQUEST_TITLE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        List<JsonApiError> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            JsonApiError error = new JsonApiError(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    Constants.VALIDATION_ERROR_TITLE,
                    fieldError.getDefaultMessage(),
                    fieldError.getField()
            );
            errors.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<JsonApiErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        List<JsonApiError> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(violation -> {
            JsonApiError error = new JsonApiError(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    Constants.CONSTRAINT_VIOLATION_TITLE,
                    violation.getMessage(),
                    violation.getPropertyPath().toString()
            );
            errors.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(errors));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<JsonApiErrorResponse> handleInsufficientStockException(
            InsufficientStockException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.CONFLICT.value()),
                Constants.INSUFFICIENT_STOCK_TITLE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(CallNotPermittedException .class)
    public ResponseEntity<JsonApiErrorResponse> handleCircuitBreakerOpen(CallNotPermittedException ex) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                Constants.CIRCUIT_BREAKER_OPEN_TITLE,
                Constants.CIRCUIT_BREAKER_OPEN_MESSAGE + ": " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<JsonApiErrorResponse> handleTimeLimiterTimeout(RequestNotPermitted ex) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()),
                Constants.TIMEOUT_TITLE,
                Constants.TIMEOUT_MESSAGE + ": Product service response timeout",
                null
        );
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

}
