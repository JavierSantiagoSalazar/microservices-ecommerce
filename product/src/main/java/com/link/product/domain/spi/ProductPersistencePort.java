package com.link.product.domain.spi;


import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;

import java.util.Optional;

public interface ProductPersistencePort {

    Product saveProduct(Product product);

    Boolean checkIfProductExists(String productName);

    Optional<Product> getProductById(Long id);

    Optional<Product> updateProductById(Long id, Product product);

    void deleteProductById(Long id);

    PageDomain<Product> getAllProducts(int page, int size, String sortBy, String sortDirection);

}
