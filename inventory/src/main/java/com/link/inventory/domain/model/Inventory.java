package com.link.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Inventory {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String location;
    private Instant lastUpdated;

}
