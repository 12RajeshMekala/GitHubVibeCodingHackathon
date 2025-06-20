package com.inventory.management.controller;

import com.inventory.management.dto.DashboardStatsDTO;
import com.inventory.management.dto.InventoryItemDTO;
import com.inventory.management.model.InventoryItem;
import com.inventory.management.service.ActivityLogService;
import com.inventory.management.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    private final ActivityLogService activityLogService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        List<InventoryItem> allItems = inventoryService.getAllItems();
        List<InventoryItem> lowStockItems = inventoryService.getLowStockItems();
        List<InventoryItem> outOfStockItems = inventoryService.getOutOfStockItems();
        
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalItems((long) allItems.size());
        stats.setLowStockItems((long) lowStockItems.size());
        stats.setOutOfStockItems((long) outOfStockItems.size());
        stats.setItems(allItems.stream().map(inventoryService::convertToDTO).collect(Collectors.toList()));
        stats.setRecentActivities(activityLogService.getRecentActivities());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAllItems() {
        List<InventoryItem> items = inventoryService.getAllItems();
        List<InventoryItemDTO> itemDTOs = items.stream()
                                                .map(inventoryService::convertToDTO)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<InventoryItemDTO>> searchItems(@RequestParam String query) {
        List<InventoryItem> items = inventoryService.searchItems(query);
        List<InventoryItemDTO> itemDTOs = items.stream()
                                                .map(inventoryService::convertToDTO)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getItemById(@PathVariable Long id) {
        return inventoryService.getItemById(id)
                .map(inventoryService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<InventoryItemDTO> createItem(@RequestBody InventoryItemDTO itemDTO) {
        InventoryItem createdItem = inventoryService.createItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(inventoryService.convertToDTO(createdItem));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateItem(@PathVariable Long id, @RequestBody InventoryItemDTO itemDTO) {
        return inventoryService.updateItem(id, itemDTO)
                .map(inventoryService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (inventoryService.deleteItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
