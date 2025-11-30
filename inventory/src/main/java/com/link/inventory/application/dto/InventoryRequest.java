package com.link.inventory.application.dto;

import com.link.inventory.domain.utils.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotNull(message = Constants.INVENTORY_PRODUCT_ID_REQUIRED)
    @Min(value = 1, message = Constants.INVENTORY_PRODUCT_ID_MIN)
    private Long productId;

    @NotNull(message = Constants.INVENTORY_QUANTITY_REQUIRED)
    @Min(value = 0, message = Constants.INVENTORY_QUANTITY_MIN)
    private Integer quantity;

    @NotBlank(message = Constants.INVENTORY_LOCATION_REQUIRED)
    @Size(max = Constants.INVENTORY_LOCATION_MAX_LENGTH, message = Constants.INVENTORY_LOCATION_MAX_EXCEEDED)
    private String location;

}
