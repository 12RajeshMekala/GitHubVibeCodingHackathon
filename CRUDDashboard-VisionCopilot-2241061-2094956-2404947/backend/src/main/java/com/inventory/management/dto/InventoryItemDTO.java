package com.inventory.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private Long id;
    private String name;
    private String sku;
    private String category;
    private Double price;
    private Integer quantity;
    private String imageUrl;
}
