package com.inventory.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalItems;
    private Long lowStockItems;
    private Long outOfStockItems;
    private List<InventoryItemDTO> items;
    private List<ActivityLogDTO> recentActivities;
}
