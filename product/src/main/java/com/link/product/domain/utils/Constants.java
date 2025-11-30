package com.link.product.domain.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String UTILITY_CLASS = "Utility class";
    public static final Integer ZERO = 0;

    // JSON:API Constants
    public static final String JSON_API_MEDIA_TYPE = "application/vnd.api+json";

    // Pagination Constants
    public static final int FIRST_PAGE = 0;
    public static final int PAGE_INCREMENT = 1;

    // JSON:API Link Keys
    public static final String LINK_SELF = "self";
    public static final String LINK_FIRST = "first";
    public static final String LINK_LAST = "last";
    public static final String LINK_NEXT = "next";
    public static final String LINK_PREV = "prev";

    // JSON:API Meta Keys
    public static final String META_TOTAL_PAGES = "totalPages";
    public static final String META_TOTAL_ELEMENTS = "totalElements";
    public static final String META_CURRENT_PAGE = "currentPage";
    public static final String META_PAGE_SIZE = "pageSize";

    // URL Template
    public static final String PAGE_URL_TEMPLATE = "%s?page[number]=%d&page[size]=%d&sort=%s&direction=%s";

    /* --- OPENAPI CONSTANTS --- */
    public static final String CREATE_PRODUCT_SUMMARY = "Add new product";
    public static final String GET_PRODUCT_BY_ID_SUMMARY = "Get product by ID";
    public static final String UPDATE_PRODUCT_SUMMARY = "Update product by ID";
    public static final String GET_ALL_PRODUCTS_SUMMARY = "Get all products with pagination";

    public static final String CREATED_PRODUCT = "Product created";
    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists";
    public static final String FOUND_PRODUCT = "Product found";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String INVALID_PRODUCT_ID = "The product ID must be a positive number";
    public static final String UPDATED_PRODUCT = "Product updated successfully";
    public static final String DELETE_PRODUCT_SUMMARY = "Delete a product by ID";
    public static final String DELETED_PRODUCT = "Product successfully deleted";
    public static final String PRODUCTS_OBTAINED = "Products successfully retrieved";
    public static final String NO_PRODUCTS_FOUND = "No products found";
    public static final String INVALID_PAGE_PARAMETERS = "Invalid pagination parameters";

    /* --- PAGINATION DEFAULT VALUES ---*/

    public static final String PAGE_DEFAULT_VALUE = "0";
    public static final String SIZE_DEFAULT_VALUE = "10";
    public static final String SORT_BY_DEFAULT = "id";
    public static final String SORT_DIRECTION_ASC = "ASC";
    public static final String SORT_DIRECTION_DESC = "DESC";

    /* --- VALIDATION CONSTANTS --- */
    public static final String FIELD_MUST_NOT_BE_BLANK = "The field must not be blank";
    public static final String FIELD_MUST_NOT_BE_NULL = "The field must not be null";
    public static final String FIELD_MUST_NOT_EXCEED_LENGTH = "The field must not exceed maximum length";
    public static final String FIELD_MUST_BE_POSITIVE = "The field must be a positive number";
    public static final String FIELD_MAX_PRICE_EXCEEDED = "The price exceeds the maximum allowed";

    /* --- PRODUCT FIELD LENGTHS --- */
    public static final int PRODUCT_NAME_MAX_LENGTH = 120;
    public static final int PRODUCT_DESCRIPTION_MAX_LENGTH = 500;
    public static final int PRODUCT_CATEGORY_MAX_LENGTH = 50;
    public static final int PRODUCT_BRAND_MAX_LENGTH = 50;

    /* --- NUMERIC LIMITS --- */
    public static final long PRODUCT_MAX_PRICE = 100_000_000L;

    /* --- EXCEPTIONS CONSTANTS --- */
    public static final String PRODUCT_ALREADY_EXISTS_EXCEPTION_MESSAGE = "The product already exists";
    public static final String PRODUCT_DOES_NOT_EXIST_MESSAGE = "The product was not found for the provided ID: ";
    public static final String PRODUCT_NO_CONTENT_MESSAGE = "There are currently no products available";

    // Security - Headers
    public static final String HEADER_X_API_KEY = "X-API-Key";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String API_KEY_PREFIX = "ApiKey ";

    // Security - Error Messages
    public static final String UNAUTHORIZED_TITLE = "Unauthorized";
    public static final String INVALID_API_KEY_DETAIL = "Invalid or missing API key";
    public static final String INVALID_API_KEY_LOG = "Invalid API key from IP: {}";

    // HTTP Status
    public static final String STATUS_401 = "401";

}
