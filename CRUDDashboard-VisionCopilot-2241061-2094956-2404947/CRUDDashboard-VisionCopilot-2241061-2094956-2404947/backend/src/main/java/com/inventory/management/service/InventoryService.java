package com.inventory.management.service;

import com.inventory.management.dto.InventoryItemDTO;
import com.inventory.management.model.ActivityLog;
import com.inventory.management.model.InventoryItem;
import com.inventory.management.model.StoreManager;
import com.inventory.management.repository.ActivityLogRepository;
import com.inventory.management.repository.InventoryItemRepository;
import com.inventory.management.repository.StoreManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryItemRepository inventoryItemRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final ActivityLogRepository activityLogRepository;
    
    // For demo purposes, we'll use a default manager
    private static final Long DEFAULT_MANAGER_ID = 1L;
    
    @Transactional(readOnly = true)
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findByManagerId(getDefaultManager().getId());
    }
    
    @Transactional(readOnly = true)
    public Optional<InventoryItem> getItemById(Long id) {
        return inventoryItemRepository.findById(id)
                .filter(item -> item.getManager().getId().equals(getDefaultManager().getId()));
    }
    
    @Transactional(readOnly = true)
    public List<InventoryItem> searchItems(String query) {
        return inventoryItemRepository.searchItems(query, getDefaultManager().getId());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryItem> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems(getDefaultManager().getId());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryItem> getOutOfStockItems() {
        return inventoryItemRepository.findOutOfStockItems(getDefaultManager().getId());
    }
    
    @Transactional
    public InventoryItem createItem(InventoryItemDTO itemDTO) {
        StoreManager manager = getDefaultManager();
        
        InventoryItem item = new InventoryItem();
        item.setName(itemDTO.getName());
        item.setSku(itemDTO.getSku());
        item.setCategory(itemDTO.getCategory());
        item.setPrice(itemDTO.getPrice());
        item.setQuantity(itemDTO.getQuantity());
        item.setImageUrl(itemDTO.getImageUrl());
        item.setManager(manager);
        
        InventoryItem savedItem = inventoryItemRepository.save(item);
        
        // Log the activity
        logActivity("Created new item: " + savedItem.getName(), savedItem);
        
        return savedItem;
    }
    
    @Transactional
    public Optional<InventoryItem> updateItem(Long id, InventoryItemDTO itemDTO) {
        return inventoryItemRepository.findById(id)
                .filter(item -> item.getManager().getId().equals(getDefaultManager().getId()))
                .map(existingItem -> {
                    existingItem.setName(itemDTO.getName());
                    existingItem.setSku(itemDTO.getSku());
                    existingItem.setCategory(itemDTO.getCategory());
                    existingItem.setPrice(itemDTO.getPrice());
                    existingItem.setQuantity(itemDTO.getQuantity());
                    existingItem.setImageUrl(itemDTO.getImageUrl());
                    
                    // Log the activity
                    logActivity("Updated item: " + existingItem.getName(), existingItem);
                    
                    return inventoryItemRepository.save(existingItem);
                });
    }
    
    @Transactional
    public boolean deleteItem(Long id) {
        return inventoryItemRepository.findById(id)
                .filter(item -> item.getManager().getId().equals(getDefaultManager().getId()))
                .map(item -> {
                    // Log the activity before deletion
                    logActivity("Deleted item: " + item.getName(), item);
                    
                    inventoryItemRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional
    public void logActivity(String action, InventoryItem item) {
        ActivityLog log = new ActivityLog();
        log.setManager(getDefaultManager());
        log.setAction(action);
        log.setItemId(item.getId());
        log.setItemName(item.getName());
        activityLogRepository.save(log);
    }
    
    private StoreManager getDefaultManager() {
        return storeManagerRepository.findById(DEFAULT_MANAGER_ID)
                .orElseGet(() -> {
                    StoreManager manager = new StoreManager();
                    manager.setName("Default Manager");
                    manager.setEmail("admin@inventoryhub.com");
                    manager.setPasswordHash("$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS"); // default
                    return storeManagerRepository.save(manager);
                });
    }
    
    public InventoryItemDTO convertToDTO(InventoryItem item) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSku(item.getSku());
        dto.setCategory(item.getCategory());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }
}
