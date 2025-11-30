package com.link.product.infrastructure.out.jpa.adapter;

import com.link.product.domain.model.PageDomain;
import com.link.product.domain.model.Product;
import com.link.product.domain.spi.ProductPersistencePort;
import com.link.product.domain.utils.Constants;
import com.link.product.infrastructure.out.jpa.entity.ProductEntity;
import com.link.product.infrastructure.out.jpa.mapper.ProductEntityMapper;
import com.link.product.infrastructure.out.jpa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductPersistencePort {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Product saveProduct(Product product) {
        ProductEntity productEntity = productEntityMapper.toEntity(product);
        ProductEntity savedEntity = productRepository.save(productEntity);
        return productEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean checkIfProductExists(String productName) {
        return productRepository.findByProductName(productName).isPresent();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return  productRepository.findById(id).map(productEntityMapper::toDomain);
    }

    @Override
    public Optional<Product> updateProductById(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingEntity -> {

                    existingEntity.setProductName(product.getProductName());
                    existingEntity.setDescription(product.getDescription());
                    existingEntity.setPrice(product.getPrice());
                    existingEntity.setCategory(product.getCategory());
                    existingEntity.setBrand(product.getBrand());
                    existingEntity.setImageUrl(product.getImageUrl());

                    ProductEntity savedEntity = productRepository.save(existingEntity);

                    return productEntityMapper.toDomain(savedEntity);
                });
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public PageDomain<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase(Constants.SORT_DIRECTION_DESC)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductEntity> pageResult = productRepository.findAll(pageable);

        List<Product> products = pageResult.getContent().stream()
                .map(productEntityMapper::toDomain)
                .toList();

        return new PageDomain<>(
                products,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

    }

}
