package com.link.product.domain.exceptions;

import com.link.product.domain.utils.Constants;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {

    private final Long notFoundId;

    public ProductNotFoundException(Long notFoundId) {
        super(Constants.PRODUCT_DOES_NOT_EXIST_MESSAGE);
        this.notFoundId = notFoundId;
    }

}
