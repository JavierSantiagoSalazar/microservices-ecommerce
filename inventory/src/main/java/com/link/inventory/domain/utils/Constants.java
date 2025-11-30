package com.link.inventory.domain.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    // ========================================================================
    // GENERAL UTILITY
    // ========================================================================
    public static final String UTILITY_CLASS = "Utility class";
    public static final String BACKSLASH = "/";
    public static final Integer ZERO = 0;

    // ========================================================================
    // JSON:API SPECIFICATION
    // ========================================================================
    public static final String JSON_API_MEDIA_TYPE = "application/vnd.api+json";
    public static final String LINK_SELF = "self";

    // ========================================================================
    // HTTP & WEBCLIENT
    // ========================================================================
    public static final String URI_PRODUCT_ID = "/product/{id}";

    // TIMEOUTS
    public static final int WEBCLIENT_RESPONSE_TIMEOUT_SECONDS = 3;
    public static final int WEBCLIENT_BLOCK_TIMEOUT_SECONDS = 5;

    // ========================================================================
    // RESILIENCE4J (Circuit Breaker, Retry, TimeLimiter)
    // ========================================================================
    public static final String CIRCUIT_BREAKER_NAME = "productService";
    public static final String RETRY_NAME = "productCall";
    public static final String FALLBACK_METHOD_NAME = "getProductByIdFallback";

    // LOG MESSAGES
    public static final String LOG_CALLING_PRODUCT_SERVICE = "🔄 Calling product-service for productId: {}";
    public static final String LOG_CIRCUIT_BREAKER_OPEN = "🔴 CIRCUIT BREAKER OPEN - Product service unavailable for: {}";

    // ========================================================================
    // SECURITY
    // ========================================================================
    public static final String HEADER_X_API_KEY = "X-API-Key";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String API_KEY_PREFIX = "ApiKey ";

    public static final String UNAUTHORIZED_TITLE = "Unauthorized";
    public static final String INVALID_API_KEY_DETAIL = "Invalid or missing API key";
    public static final String INVALID_API_KEY_LOG = "Invalid API key from IP: {}";

    public static final String STATUS_401 = "401";

    // ========================================================================
    // OPENAPI DOCUMENTATION
    // ========================================================================
    public static final String GET_INVENTORY_BY_PRODUCT_SUMMARY = "Get inventory by product ID";
    public static final String CREATE_INVENTORY_SUMMARY = "Create new inventory record";

    // Responses
    public static final String INVENTORY_FOUND = "Inventory found successfully";
    public static final String INVENTORY_ALREADY_EXISTS_DESCRIPTION = "Inventory already exists";
    public static final String INVENTORY_CREATED_DESCRIPTION = "Inventory created successfully";
    public static final String INVENTORY_UPDATED_DESCRIPTION = "Inventory quantity updated successfully";

    public static final String INVENTORY_NOT_FOUND = "Inventory not found for this product";
    public static final String INVENTORY_NOT_FOUND_DESCRIPTION = "Inventory not found for the given product ID";

    public static final String PRODUCT_NOT_FOUND_DESCRIPTION = "Product does not exist in product service";
    public static final String BAD_REQUEST_DESCRIPTION = "Invalid request parameters";

    // ========================================================================
    // VALIDATION MESSAGES
    // ========================================================================
    public static final String INVALID_PRODUCT_ID = "The product ID must be a positive number";
    public static final String INVENTORY_PRODUCT_ID_REQUIRED = "Product ID is required";
    public static final String INVENTORY_PRODUCT_ID_MIN = "Product ID must be greater than 0";

    public static final String INVENTORY_QUANTITY_REQUIRED = "Quantity is required";
    public static final String INVENTORY_QUANTITY_MIN = "Quantity cannot be negative";

    public static final String INVENTORY_LOCATION_REQUIRED = "Location is required";
    public static final int INVENTORY_LOCATION_MAX_LENGTH = 80;
    public static final String INVENTORY_LOCATION_MAX_EXCEEDED = "Location cannot exceed maximum length";

    // ========================================================================
    // BUSINESS OPERATIONS
    // ========================================================================
    public static final String INVENTORY_OPERATION_ADDED = "ADDED";
    public static final String INVENTORY_OPERATION_REMOVED = "REMOVED";
    public static final String NOT_SPECIFIED_REASON = "NOT_SPECIFIED";

    public static final String INVENTORY_CHANGED_LOG_MESSAGE =
            "INVENTORY CHANGED - InventoryId: {}, ProductId: {}, Operation: {}, QuantityChange: {}, OldQuantity: {}, NewQuantity: {}, Reason: {}";

    // ========================================================================
    // EXCEPTION TITLES
    // ========================================================================
    public static final String INVENTORY_ALREADY_EXISTS_TITLE = "Inventory Already Exists";
    public static final String INVENTORY_NOT_FOUND_TITLE = "Inventory Not Found";
    public static final String PRODUCT_NOT_FOUND_TITLE = "Product Not Found";
    public static final String INSUFFICIENT_STOCK_TITLE = "Insufficient Stock";

    public static final String PRODUCT_SERVICE_ERROR_TITLE = "Product Service Error";
    public static final String SERVICE_UNAVAILABLE_TITLE = "Service Unavailable";

    public static final String CIRCUIT_BREAKER_OPEN_TITLE = "Circuit Breaker Open";
    public static final String TIMEOUT_TITLE = "Request Timeout";

    public static final String BAD_REQUEST_TITLE = "Bad Request";
    public static final String VALIDATION_ERROR_TITLE = "Validation Error";
    public static final String CONSTRAINT_VIOLATION_TITLE = "Constraint Violation";

    // ========================================================================
    // EXCEPTION MESSAGES
    // ========================================================================
    public static final String INVENTORY_NOT_FOUND_MESSAGE = "Inventory not found for product ID: ";
    public static final String PRODUCT_NOT_VALID_MESSAGE = "Product with ID does not exist: ";

    public static final String PRODUCT_SERVICE_ERROR_MESSAGE = "Error communicating with product service";
    public static final String PRODUCT_SERVICE_UNAVAILABLE_MESSAGE = "Cannot connect to product service";
    public static final String PRODUCT_SERVICE_DOWN_MESSAGE = "Product service is not available";

    public static final String CIRCUIT_BREAKER_OPEN_MESSAGE = "Product service temporarily unavailable";
    public static final String TIMEOUT_MESSAGE = "Service response timeout exceeded";

    public static final String QUANTITY_IS_REQUIRED = "Quantity change is required";
    public static final String INSUFFICIENT_STOCK_DESCRIPTION = "Insufficient stock for the requested operation";
}
