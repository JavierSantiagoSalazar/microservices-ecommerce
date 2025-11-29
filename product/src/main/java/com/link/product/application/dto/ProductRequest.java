package com.link.product.application.dto;

import com.link.product.domain.utils.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = Constants.FIELD_MUST_NOT_BE_BLANK)
    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    @Size(max = Constants.PRODUCT_NAME_MAX_LENGTH, message = Constants.FIELD_MUST_NOT_EXCEED_LENGTH)
    private String productName;

    @NotBlank(message = Constants.FIELD_MUST_NOT_BE_BLANK)
    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    @Size(max = Constants.PRODUCT_DESCRIPTION_MAX_LENGTH, message = Constants.FIELD_MUST_NOT_EXCEED_LENGTH)
    private String description;

    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    @Positive(message = Constants.FIELD_MUST_BE_POSITIVE)
    @Max(value = Constants.PRODUCT_MAX_PRICE, message = Constants.FIELD_MAX_PRICE_EXCEEDED)
    private Long price;

    @NotBlank(message = Constants.FIELD_MUST_NOT_BE_BLANK)
    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    @Size(max = Constants.PRODUCT_CATEGORY_MAX_LENGTH, message = Constants.FIELD_MUST_NOT_EXCEED_LENGTH)
    private String category;

    @NotBlank(message = Constants.FIELD_MUST_NOT_BE_BLANK)
    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    @Size(max = Constants.PRODUCT_BRAND_MAX_LENGTH, message = Constants.FIELD_MUST_NOT_EXCEED_LENGTH)
    private String brand;

    @NotBlank(message = Constants.FIELD_MUST_NOT_BE_BLANK)
    @NotNull(message = Constants.FIELD_MUST_NOT_BE_NULL)
    private String imageUrl;

}
