package com.link.product.domain.api;

import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;

public interface ProductServicePort {

    Product saveProduct(Product product);

    Product getProductById(Long id);

    Product updateProductById(Long id, Product product);

    void deleteProductById(Long id);

    PageDomain<Product> getAllProducts(int page, int size, String sortBy, String sortDirection);

}
