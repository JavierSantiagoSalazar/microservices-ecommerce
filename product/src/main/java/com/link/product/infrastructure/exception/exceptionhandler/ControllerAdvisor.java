package com.link.product.infrastructure.exception.exceptionhandler;

import com.link.product.domain.exceptions.NoContentProductException;
import com.link.product.domain.exceptions.ProductAlreadyExistsException;
import com.link.product.domain.exceptions.ProductNotFoundException;
import com.link.product.domain.utils.Constants;
import com.link.product.infrastructure.exception.exceptionhandler.dto.JsonApiError;
import com.link.product.infrastructure.exception.exceptionhandler.dto.JsonApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<JsonApiErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.CONFLICT.value()),
                "Product Already Exists",
                Constants.PRODUCT_ALREADY_EXISTS_EXCEPTION_MESSAGE,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<JsonApiErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Product Not Found",
                ex.getMessage() + ex.getNotFoundId(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(NoContentProductException.class)
    public ResponseEntity<JsonApiErrorResponse> handleNoContentProductException(
            NoContentProductException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.NO_CONTENT.value()),
                "No Products Found",
                Constants.PRODUCT_NO_CONTENT_MESSAGE,
                null
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(error));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<JsonApiErrorResponse> handleConnectException(
            ConnectException ex
    ) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                "Service Unavailable",
                ex.getMessage(),
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
                "Bad Request",
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
                    "Validation Error",
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
                    "Constraint Violation",
                    violation.getMessage(),
                    violation.getPropertyPath().toString()
            );
            errors.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(new JsonApiErrorResponse(errors));
    }
}
