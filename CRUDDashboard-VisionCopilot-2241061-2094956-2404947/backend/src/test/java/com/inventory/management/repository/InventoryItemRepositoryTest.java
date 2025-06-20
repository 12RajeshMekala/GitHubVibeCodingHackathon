package com.inventory.management.repository;

import com.inventory.management.model.InventoryItem;
import com.inventory.management.model.StoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    private StoreManager testManager;
    private StoreManager otherManager;

    @BeforeEach
    void setUp() {
        // Create a test manager
        testManager = new StoreManager();
        testManager.setName("Test Manager");
        testManager.setEmail("test@example.com");
        testManager.setPasswordHash("password123");
        entityManager.persist(testManager);

        // Create another manager for testing isolation
        otherManager = new StoreManager();
        otherManager.setName("Other Manager");
        otherManager.setEmail("other@example.com");
        otherManager.setPasswordHash("password456");
        entityManager.persist(otherManager);

        // Create sample inventory items for the test manager
        createInventoryItem("Laptop", "TECH-001", "Electronics", 999.99, 15);
        createInventoryItem("Mouse", "TECH-002", "Electronics", 29.99, 8);
        createInventoryItem("Keyboard", "TECH-003", "Electronics", 59.99, 0);
        createInventoryItem("T-Shirt", "CLOTH-001", "Clothing", 19.99, 25);
        
        // Create an item for the other manager to test isolation
        createInventoryItemForManager("Tablet", "TECH-004", "Electronics", 499.99, 5, otherManager);

        entityManager.flush();
    }

    private void createInventoryItem(String name, String sku, String category, Double price, Integer quantity) {
        createInventoryItemForManager(name, sku, category, price, quantity, testManager);
    }

    private void createInventoryItemForManager(String name, String sku, String category, Double price, Integer quantity, StoreManager manager) {
        InventoryItem item = new InventoryItem();
        item.setName(name);
        item.setSku(sku);
        item.setCategory(category);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setManager(manager);
        entityManager.persist(item);
    }

    @Test
    void testFindByManagerId() {
        // Act
        List<InventoryItem> items = inventoryItemRepository.findByManagerId(testManager.getId());
        
        // Assert
        assertEquals(4, items.size(), "Should find 4 items for test manager");
        assertTrue(items.stream().allMatch(item -> item.getManager().equals(testManager)), 
                "All returned items should belong to the test manager");
    }

    @Test
    void testFindLowStockItems() {
        // Act
        List<InventoryItem> lowStockItems = inventoryItemRepository.findLowStockItems(testManager.getId());
        
        // Assert
        assertEquals(1, lowStockItems.size(), "Should find 1 low stock item (quantity > 0 and < 10)");
        assertEquals("Mouse", lowStockItems.get(0).getName(), "The low stock item should be 'Mouse'");
        assertEquals(8, lowStockItems.get(0).getQuantity(), "The quantity should be 8");
    }

    @Test
    void testFindOutOfStockItems() {
        // Act
        List<InventoryItem> outOfStockItems = inventoryItemRepository.findOutOfStockItems(testManager.getId());
        
        // Assert
        assertEquals(1, outOfStockItems.size(), "Should find 1 out of stock item (quantity = 0)");
        assertEquals("Keyboard", outOfStockItems.get(0).getName(), "The out of stock item should be 'Keyboard'");
        assertEquals(0, outOfStockItems.get(0).getQuantity(), "The quantity should be 0");
    }

    @Test
    void testSearchItems() {
        // Test search by name
        List<InventoryItem> laptopSearch = inventoryItemRepository.searchItems("Laptop", testManager.getId());
        assertEquals(1, laptopSearch.size(), "Should find 1 item when searching for 'Laptop'");
        assertEquals("Laptop", laptopSearch.get(0).getName());
        
        // Test search by category
        List<InventoryItem> electronicsSearch = inventoryItemRepository.searchItems("Electronics", testManager.getId());
        assertEquals(3, electronicsSearch.size(), "Should find 3 items in 'Electronics' category");
        
        // Test search by SKU
        List<InventoryItem> skuSearch = inventoryItemRepository.searchItems("CLOTH", testManager.getId());
        assertEquals(1, skuSearch.size(), "Should find 1 item with SKU containing 'CLOTH'");
        assertEquals("T-Shirt", skuSearch.get(0).getName());
        
        // Test search with no results
        List<InventoryItem> noResults = inventoryItemRepository.searchItems("nonexistent", testManager.getId());
        assertEquals(0, noResults.size(), "Should find 0 items for a nonexistent term");
        
        // Test case-insensitivity
        List<InventoryItem> caseInsensitive = inventoryItemRepository.searchItems("laptop", testManager.getId());
        assertEquals(1, caseInsensitive.size(), "Search should be case-insensitive");
    }
    
    @Test
    void testManagerIsolation() {
        // Act & Assert
        // Verify that queries for one manager don't return items from another manager
        List<InventoryItem> testManagerItems = inventoryItemRepository.findByManagerId(testManager.getId());
        assertEquals(4, testManagerItems.size());
        
        List<InventoryItem> otherManagerItems = inventoryItemRepository.findByManagerId(otherManager.getId());
        assertEquals(1, otherManagerItems.size());
        
        // Verify the low stock query respects manager isolation
        List<InventoryItem> otherManagerLowStock = inventoryItemRepository.findLowStockItems(otherManager.getId());
        assertEquals(1, otherManagerLowStock.size());
        assertEquals("Tablet", otherManagerLowStock.get(0).getName());
        
        // Verify the search query respects manager isolation
        List<InventoryItem> searchResults = inventoryItemRepository.searchItems("Tablet", testManager.getId());
        assertEquals(0, searchResults.size(), "Should not find other manager's items when searching");
    }
}
