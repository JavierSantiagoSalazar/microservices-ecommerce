package com.link.product.domain.usecase;

import com.link.product.domain.api.ProductServicePort;
import com.link.product.domain.exceptions.ProductAlreadyExistsException;
import com.link.product.domain.exceptions.ProductNotFoundException;
import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;
import com.link.product.domain.spi.ProductPersistencePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductUseCase implements ProductServicePort {

    private final ProductPersistencePort productPersistencePort;

    @Override
    public Product saveProduct(Product product) {

        if (checkIfArticleExists(product.getProductName())) {
            throw new ProductAlreadyExistsException();
        }

        return productPersistencePort.saveProduct(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productPersistencePort.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateProductById(Long id, Product product) {

        Product existingProduct = productPersistencePort.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!existingProduct.getProductName().equals(product.getProductName())
                && checkIfArticleExists(product.getProductName())) {
            throw new ProductAlreadyExistsException();
        }

        return productPersistencePort.updateProductById(id, product)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void deleteProductById(Long id) {
        getProductById(id);
        productPersistencePort.deleteProductById(id);
    }

    @Override
    public PageDomain<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        return productPersistencePort.getAllProducts(page, size, sortBy, sortDirection);
    }


    private boolean checkIfArticleExists(String productName) {
        return productPersistencePort.checkIfProductExists(productName);
    }

}
