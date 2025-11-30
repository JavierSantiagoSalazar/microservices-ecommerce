package com.link.inventory.application.dto;

import com.link.inventory.domain.utils.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {

    @NotNull(message = Constants.QUANTITY_IS_REQUIRED)
    private Integer quantityChange;

    private String reason;

}
