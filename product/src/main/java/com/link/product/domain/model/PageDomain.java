package com.link.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private Long id;
    private String productName;
    private String description;
    private Double price;
    private String category;
    private String brand;
    private String imageUrl;

}
