package com.link.product.application.dto;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import lombok.Getter;
import lombok.Setter;

@JsonApiResource(type = "products")
@Getter
@Setter
public class ProductResponse {

    @JsonApiId
    private String id;

    private String productName;
    private String description;
    private Double price;
    private String category;
    private String brand;
    private String imageUrl;

}
