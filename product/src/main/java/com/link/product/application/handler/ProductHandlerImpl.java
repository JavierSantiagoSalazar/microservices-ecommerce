package com.link.product.application.handler;

import com.link.product.application.dto.PageResponse;
import com.link.product.application.dto.ProductRequest;
import com.link.product.application.dto.ProductResponse;
import com.link.product.application.mappers.ProductRequestMapper;
import com.link.product.application.mappers.ProductResponseMapper;
import com.link.product.domain.api.ProductServicePort;
import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductHandlerImpl implements ProductHandler {

    private final ProductServicePort productServicePort;
    private final ProductRequestMapper  productRequestMapper;
    private final ProductResponseMapper productResponseMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = productRequestMapper.toDomain(productRequest);
        Product savedProduct = productServicePort.saveProduct(product);
        return productResponseMapper.toResponse(savedProduct);

    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productServicePort.getProductById(id);
        return productResponseMapper.toResponse(product);

    }

    @Override
    public ProductResponse updateProductById(Long id, ProductRequest productRequest) {

        Product product = productRequestMapper.toDomain(productRequest);

        return productResponseMapper.toResponse(productServicePort.updateProductById(id, product));
    }

    @Override
    public void deleteProductById(Long id) {
        productServicePort.deleteProductById(id);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDirection) {

        PageDomain<Product> productPage = productServicePort.getAllProducts(page, size, sortBy, sortDirection);

        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(productResponseMapper::toResponse)
                .toList();

        return new PageResponse<>(
                productResponses,
                productPage.getPageNumber(),
                productPage.getPageSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

}
