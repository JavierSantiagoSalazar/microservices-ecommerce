package com.link.inventory.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private String id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String location;
    private String lastUpdated;

}
