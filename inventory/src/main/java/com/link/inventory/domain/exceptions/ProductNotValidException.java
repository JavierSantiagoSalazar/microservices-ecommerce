package com.link.inventory.domain.exceptions;

import com.link.inventory.domain.utils.Constants;
import lombok.Getter;

@Getter
public class ProductNotValidException extends RuntimeException {

    private final Long productId;

    public ProductNotValidException(Long productId) {
        super(Constants.PRODUCT_NOT_VALID_MESSAGE + productId);
        this.productId = productId;
    }

}
