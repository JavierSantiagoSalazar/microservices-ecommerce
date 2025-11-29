package com.link.product.infrastructure.input;

import com.link.product.application.dto.JsonApiResponse;
import com.link.product.application.dto.PageResponse;
import com.link.product.application.dto.ProductRequest;
import com.link.product.application.dto.ProductResponse;
import com.link.product.application.handler.ProductHandler;
import com.link.product.domain.utils.Constants;
import com.link.product.domain.utils.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Validated
public class ProductRestController {

    private final ProductHandler productHandler;

    @Operation(summary = Constants.CREATE_PRODUCT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.CREATED,
                    description = Constants.CREATED_PRODUCT,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.CONFLICT,
                    description = Constants.PRODUCT_ALREADY_EXISTS,
                    content = @Content
            )
    })
    @PostMapping(value = "/", produces = Constants.JSON_API_MEDIA_TYPE, consumes = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            HttpServletRequest request
    ) {
        ProductResponse created = productHandler.createProduct(productRequest);

        JsonApiResponse<ProductResponse> response = new JsonApiResponse<>(created);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, request.getRequestURL().toString() + created.getId());
        response.setLinks(links);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);
    }

    @Operation(summary = Constants.GET_PRODUCT_BY_ID_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.OK,
                    description = Constants.FOUND_PRODUCT,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.PRODUCT_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.INVALID_PRODUCT_ID,
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}", produces = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<ProductResponse>> getProductById(
            @Valid @PathVariable @Min(1) Long id,
            HttpServletRequest request
    ) {
        ProductResponse productResponse = productHandler.getProductById(id);

        JsonApiResponse<ProductResponse> response = new JsonApiResponse<>(productResponse);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, request.getRequestURL().toString());
        response.setLinks(links);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);
    }

    @Operation(summary = Constants.UPDATE_PRODUCT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.OK,
                    description = Constants.UPDATED_PRODUCT,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.PRODUCT_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.INVALID_PRODUCT_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.CONFLICT,
                    description = Constants.PRODUCT_ALREADY_EXISTS,
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}", produces = Constants.JSON_API_MEDIA_TYPE, consumes = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<ProductResponse>> updateProductById(
            @Valid @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductRequest productRequest,
            HttpServletRequest request
    ) {
        ProductResponse updatedProduct = productHandler.updateProductById(id, productRequest);

        JsonApiResponse<ProductResponse> response = new JsonApiResponse<>(updatedProduct);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, request.getRequestURL().toString());
        response.setLinks(links);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);
    }

    @Operation(summary = Constants.DELETE_PRODUCT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.NO_CONTENT,
                    description = Constants.DELETED_PRODUCT,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.PRODUCT_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.INVALID_PRODUCT_ID,
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@Valid @PathVariable @Min(1) Long id) {

        productHandler.deleteProductById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = Constants.GET_ALL_PRODUCTS_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.OK,
                    description = Constants.PRODUCTS_OBTAINED,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NO_CONTENT,
                    description = Constants.NO_PRODUCTS_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.INVALID_PAGE_PARAMETERS,
                    content = @Content
            )
    })
    @GetMapping(value = "/", produces = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_VALUE) int page,
            @RequestParam(defaultValue = Constants.SIZE_DEFAULT_VALUE) int size,
            @RequestParam(defaultValue = Constants.SORT_BY_DEFAULT) String sortBy,
            @RequestParam(defaultValue = Constants.SORT_DIRECTION_ASC) String sortDirection,
            HttpServletRequest request
    ) {
        PageResponse<ProductResponse> pageResult = productHandler.getAllProducts(page, size, sortBy, sortDirection);

        JsonApiResponse<List<ProductResponse>> response = new JsonApiResponse<>(pageResult.getContent());

        String baseUrl = request.getRequestURL().toString();

        addPaginationLinks(response, baseUrl, pageResult, page, size, sortBy, sortDirection);

        Map<String, Object> meta = new HashMap<>();
        meta.put(Constants.META_TOTAL_PAGES, pageResult.getTotalPages());
        meta.put(Constants.META_TOTAL_ELEMENTS, pageResult.getTotalElements());
        meta.put(Constants.META_CURRENT_PAGE, pageResult.getPageNumber());
        meta.put(Constants.META_PAGE_SIZE, pageResult.getPageSize());
        response.setMeta(meta);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);
    }

    private String buildPageUrl(String baseUrl, int page, int size, String sortBy, String sortDirection) {
        return String.format(Constants.PAGE_URL_TEMPLATE, baseUrl, page, size, sortBy, sortDirection);
    }

    private void addPaginationLinks(
            JsonApiResponse<?> response,
            String baseUrl,
            PageResponse<?> pageResult,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, buildPageUrl(baseUrl, page, size, sortBy, sortDirection));
        links.put(Constants.LINK_FIRST, buildPageUrl(baseUrl, Constants.FIRST_PAGE, size, sortBy, sortDirection));
        links.put(Constants.LINK_LAST, buildPageUrl(
                baseUrl,
                pageResult.getTotalPages() - Constants.PAGE_INCREMENT,
                size,
                sortBy,
                sortDirection
        ));

        if (!pageResult.isLast()) {
            links.put(Constants.LINK_NEXT, buildPageUrl(
                    baseUrl,
                    page + Constants.PAGE_INCREMENT,
                    size,
                    sortBy,
                    sortDirection
                    )
            );
        }
        if (page > Constants.FIRST_PAGE) {
            links.put(Constants.LINK_PREV, buildPageUrl(
                    baseUrl,
                    page - Constants.PAGE_INCREMENT,
                    size,
                    sortBy,
                    sortDirection
            ));
        }
        response.setLinks(links);
    }

}
