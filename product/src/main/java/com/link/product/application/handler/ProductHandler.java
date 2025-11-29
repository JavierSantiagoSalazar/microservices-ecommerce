package com.link.product.application.handler;

import com.link.product.application.dto.PageResponse;
import com.link.product.application.dto.ProductRequest;
import com.link.product.application.dto.ProductResponse;

public interface ProductHandler {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    ProductResponse  updateProductById(Long id, ProductRequest productRequest);

    void deleteProductById(Long id);

    PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDirection);

}
