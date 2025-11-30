package com.link.inventory.domain.spi;

import com.link.inventory.domain.model.Product;

public interface ProductClientPort {

    Product getProductById(Long productId);

}
