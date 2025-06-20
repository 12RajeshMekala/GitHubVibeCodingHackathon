package com.inventory.management.service;

import com.inventory.management.dto.InventoryItemDTO;
import com.inventory.management.model.ActivityLog;
import com.inventory.management.model.InventoryItem;
import com.inventory.management.model.StoreManager;
import com.inventory.management.repository.ActivityLogRepository;
import com.inventory.management.repository.InventoryItemRepository;
import com.inventory.management.repository.StoreManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @Mock
    private StoreManagerRepository storeManagerRepository;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private StoreManager testManager;
    private InventoryItem testItem;
    private InventoryItemDTO testItemDTO;

    @BeforeEach
    void setUp() {
        // Setup test manager
        testManager = new StoreManager();
        testManager.setId(1L);
        testManager.setName("Test Manager");
        testManager.setEmail("test@example.com");
        testManager.setPasswordHash("password123");

        // Setup test item
        testItem = new InventoryItem();
        testItem.setId(1L);
        testItem.setName("Test Item");
        testItem.setSku("TST-001");
        testItem.setCategory("Electronics");
        testItem.setPrice(99.99);
        testItem.setQuantity(10);
        testItem.setManager(testManager);

        // Setup test DTO
        testItemDTO = new InventoryItemDTO();
        testItemDTO.setId(1L);
        testItemDTO.setName("Test Item");
        testItemDTO.setSku("TST-001");
        testItemDTO.setCategory("Electronics");
        testItemDTO.setPrice(99.99);
        testItemDTO.setQuantity(10);
    }

    @Test
    void testGetAllItems() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findByManagerId(1L))
            .thenReturn(Arrays.asList(testItem));

        // Act
        List<InventoryItem> result = inventoryService.getAllItems();

        // Assert
        assertEquals(1, result.size(), "Should return 1 item");
        assertEquals(testItem, result.get(0), "Should return the test item");
        verify(inventoryItemRepository).findByManagerId(1L);
    }

    @Test
    void testGetItemById() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        // Act
        Optional<InventoryItem> result = inventoryService.getItemById(1L);

        // Assert
        assertTrue(result.isPresent(), "Should return an item");
        assertEquals(testItem, result.get(), "Should return the test item");
        verify(inventoryItemRepository).findById(1L);
    }

    @Test
    void testGetItemById_NotFound() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Optional<InventoryItem> result = inventoryService.getItemById(2L);

        // Assert
        assertFalse(result.isPresent(), "Should return empty Optional");
        verify(inventoryItemRepository).findById(2L);
    }

    @Test
    void testSearchItems() {
        // Arrange
        String query = "Test";
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.searchItems(query, 1L))
            .thenReturn(Arrays.asList(testItem));

        // Act
        List<InventoryItem> result = inventoryService.searchItems(query);

        // Assert
        assertEquals(1, result.size(), "Should return 1 item");
        assertEquals(testItem, result.get(0), "Should return the test item");
        verify(inventoryItemRepository).searchItems(query, 1L);
    }

    @Test
    void testGetLowStockItems() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findLowStockItems(1L))
            .thenReturn(Arrays.asList(testItem));

        // Act
        List<InventoryItem> result = inventoryService.getLowStockItems();

        // Assert
        assertEquals(1, result.size(), "Should return 1 item");
        assertEquals(testItem, result.get(0), "Should return the test item");
        verify(inventoryItemRepository).findLowStockItems(1L);
    }

    @Test
    void testGetOutOfStockItems() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findOutOfStockItems(1L))
            .thenReturn(Arrays.asList(testItem));

        // Act
        List<InventoryItem> result = inventoryService.getOutOfStockItems();

        // Assert
        assertEquals(1, result.size(), "Should return 1 item");
        assertEquals(testItem, result.get(0), "Should return the test item");
        verify(inventoryItemRepository).findOutOfStockItems(1L);
    }

    @Test
    void testCreateItem() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // Act
        InventoryItem result = inventoryService.createItem(testItemDTO);

        // Assert
        assertEquals(testItem, result, "Should return the created item");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
        
        // Verify activity log is created
        verify(activityLogRepository).save(any(ActivityLog.class));
    }

    @Test
    void testUpdateItem() {
        // Arrange
        InventoryItemDTO updateDTO = new InventoryItemDTO();
        updateDTO.setId(1L);
        updateDTO.setName("Updated Item");
        updateDTO.setSku("TST-001");
        updateDTO.setCategory("Electronics");
        updateDTO.setPrice(129.99);
        updateDTO.setQuantity(15);

        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<InventoryItem> result = inventoryService.updateItem(1L, updateDTO);

        // Assert
        assertTrue(result.isPresent(), "Should return an updated item");
        assertEquals("Updated Item", result.get().getName(), "Name should be updated");
        assertEquals(129.99, result.get().getPrice(), "Price should be updated");
        assertEquals(15, result.get().getQuantity(), "Quantity should be updated");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
        
        // Verify activity log is created
        verify(activityLogRepository).save(any(ActivityLog.class));
    }

    @Test
    void testUpdateItem_NotFound() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Optional<InventoryItem> result = inventoryService.updateItem(2L, testItemDTO);

        // Assert
        assertFalse(result.isPresent(), "Should return empty Optional");
        verify(inventoryItemRepository, never()).save(any(InventoryItem.class));
        verify(activityLogRepository, never()).save(any(ActivityLog.class));
    }

    @Test
    void testDeleteItem() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        // Act
        boolean result = inventoryService.deleteItem(1L);

        // Assert
        assertTrue(result, "Should return true for successful deletion");
        verify(inventoryItemRepository).delete(testItem);
        
        // Verify activity log is created
        verify(activityLogRepository).save(any(ActivityLog.class));
    }

    @Test
    void testDeleteItem_NotFound() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(inventoryItemRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        boolean result = inventoryService.deleteItem(2L);

        // Assert
        assertFalse(result, "Should return false for unsuccessful deletion");
        verify(inventoryItemRepository, never()).delete(any());
        verify(activityLogRepository, never()).save(any());
    }

    @Test
    void testLogActivity() {
        // Arrange
        String action = "Test Action";
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        ArgumentCaptor<ActivityLog> logCaptor = ArgumentCaptor.forClass(ActivityLog.class);
        
        // Act
        inventoryService.logActivity(action, testItem);
        
        // Assert
        verify(activityLogRepository).save(logCaptor.capture());
        ActivityLog capturedLog = logCaptor.getValue();
        
        assertEquals(action, capturedLog.getAction(), "Action should match");
        assertEquals(testItem.getId(), capturedLog.getItemId(), "Item ID should match");
        assertEquals(testItem.getName(), capturedLog.getItemName(), "Item name should match");
        assertEquals(testManager, capturedLog.getManager(), "Manager should match");
    }

    @Test
    void testConvertToDTO() {
        // Act
        InventoryItemDTO result = inventoryService.convertToDTO(testItem);
        
        // Assert
        assertEquals(testItem.getId(), result.getId(), "ID should match");
        assertEquals(testItem.getName(), result.getName(), "Name should match");
        assertEquals(testItem.getSku(), result.getSku(), "SKU should match");
        assertEquals(testItem.getCategory(), result.getCategory(), "Category should match");
        assertEquals(testItem.getPrice(), result.getPrice(), "Price should match");
        assertEquals(testItem.getQuantity(), result.getQuantity(), "Quantity should match");
    }

    @Test
    void testGetDefaultManager_Existing() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        
        // Act - getDefaultManager() is private, so we'll use a public method that calls it
        inventoryService.getAllItems();
        
        // Assert
        verify(storeManagerRepository).findById(1L);
        verify(storeManagerRepository, never()).save(any(StoreManager.class));
    }

    @Test
    void testGetDefaultManager_NotFound() {
        // Arrange
        when(storeManagerRepository.findById(1L)).thenReturn(Optional.empty());
        when(storeManagerRepository.save(any(StoreManager.class))).thenReturn(testManager);
        
        // Act - getDefaultManager() is private, so we'll use a public method that calls it
        inventoryService.getAllItems();
        
        // Assert
        verify(storeManagerRepository).findById(1L);
        verify(storeManagerRepository).save(any(StoreManager.class));
    }
}
