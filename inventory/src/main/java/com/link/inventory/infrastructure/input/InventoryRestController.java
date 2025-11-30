package com.link.inventory.infrastructure.input;

import com.link.inventory.application.dto.InventoryRequest;
import com.link.inventory.application.dto.InventoryResponse;
import com.link.inventory.application.dto.JsonApiResponse;
import com.link.inventory.application.dto.UpdateQuantityRequest;
import com.link.inventory.application.handler.InventoryHandler;
import com.link.inventory.domain.utils.Constants;
import com.link.inventory.domain.utils.HttpStatusCodes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryRestController {

    private final InventoryHandler inventoryHandler;

    @Operation(summary = Constants.GET_INVENTORY_BY_PRODUCT_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.OK,
                    description = Constants.INVENTORY_FOUND,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.INVENTORY_NOT_FOUND,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.INVALID_PRODUCT_ID,
                    content = @Content
            )
    })
    @GetMapping(value = "/product/{productId}", produces = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<InventoryResponse>> getInventoryByProductId(
            @Valid @PathVariable @Min(1) Long productId,
            HttpServletRequest request
    ) {

        InventoryResponse inventoryResponse = inventoryHandler.getInventoryByProductId(productId);

        JsonApiResponse<InventoryResponse> response = new JsonApiResponse<>(inventoryResponse);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, request.getRequestURL().toString());
        response.setLinks(links);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);

    }


    @Operation(summary = Constants.CREATE_INVENTORY_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.CREATED,
                    description = Constants.INVENTORY_CREATED_DESCRIPTION,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.BAD_REQUEST_DESCRIPTION
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.PRODUCT_NOT_FOUND_DESCRIPTION
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.CONFLICT,
                    description = Constants.INVENTORY_ALREADY_EXISTS_DESCRIPTION
            )
    })
    @PostMapping(consumes = Constants.JSON_API_MEDIA_TYPE, produces = Constants.JSON_API_MEDIA_TYPE)
    public ResponseEntity<JsonApiResponse<InventoryResponse>> createInventory(
            @Valid @RequestBody InventoryRequest inventoryRequest,
            HttpServletRequest request
    ) {

        InventoryResponse created = inventoryHandler.createInventory(inventoryRequest);

        JsonApiResponse<InventoryResponse> response = new JsonApiResponse<>(created);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, request.getRequestURL().toString() + Constants.BACKSLASH + created.getId());
        response.setLinks(links);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);

    }

    @Operation(summary = "Update inventory quantity after purchase or restock")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCodes.OK,
                    description = Constants.INVENTORY_UPDATED_DESCRIPTION,
                    content = @Content(
                            mediaType = Constants.JSON_API_MEDIA_TYPE,
                            schema = @Schema(implementation = JsonApiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.BAD_REQUEST,
                    description = Constants.BAD_REQUEST_DESCRIPTION
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.NOT_FOUND,
                    description = Constants.INVENTORY_NOT_FOUND_DESCRIPTION
            ),
            @ApiResponse(
                    responseCode = HttpStatusCodes.CONFLICT,
                    description = Constants.INSUFFICIENT_STOCK_DESCRIPTION
            )
    })
    @PutMapping(
            value = "/{id}/quantity",
            consumes = Constants.JSON_API_MEDIA_TYPE,
            produces = Constants.JSON_API_MEDIA_TYPE
    )
    public ResponseEntity<JsonApiResponse<InventoryResponse>> updateQuantity(
            @Valid @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdateQuantityRequest request,
            HttpServletRequest httpRequest
    ) {

        InventoryResponse updated = inventoryHandler.updateQuantity(id, request);

        JsonApiResponse<InventoryResponse> response = new JsonApiResponse<>(updated);

        Map<String, String> links = new HashMap<>();
        links.put(Constants.LINK_SELF, httpRequest.getRequestURL().toString());
        response.setLinks(links);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Constants.JSON_API_MEDIA_TYPE))
                .body(response);

    }

}
