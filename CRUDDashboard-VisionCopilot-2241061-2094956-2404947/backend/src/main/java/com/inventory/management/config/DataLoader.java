package com.inventory.management.config;

import com.inventory.management.model.InventoryItem;
import com.inventory.management.model.StoreManager;
import com.inventory.management.repository.InventoryItemRepository;
import com.inventory.management.repository.StoreManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final StoreManagerRepository storeManagerRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public void run(String... args) {
        // Create default manager if not exists
        if (storeManagerRepository.count() == 0) {
            StoreManager manager = new StoreManager();
            manager.setName("Default Manager");
            manager.setEmail("admin@inventoryhub.com");
            manager.setPasswordHash("$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS"); // "password"
            storeManagerRepository.save(manager);
            
            // Add some sample inventory items
            createSampleItems(manager);
        }
    }

    private void createSampleItems(StoreManager manager) {
        // Electronics category
        createItem(manager, "iPhone 15 Pro", "APPL-IP15-PRO", "Electronics", 999.99, 20);
        createItem(manager, "Samsung Galaxy S24", "SAMS-GS24-BLK", "Electronics", 899.99, 15);
        createItem(manager, "Sony PlayStation 5", "SONY-PS5-1TB", "Electronics", 499.99, 5);
        createItem(manager, "Nintendo Switch", "NINT-SWITCH-V2", "Electronics", 299.99, 0);
        
        // Clothing category
        createItem(manager, "Men's T-Shirt", "CLOTH-MTEE-BLK-L", "Clothing", 19.99, 50);
        createItem(manager, "Women's Jeans", "CLOTH-WJEAN-BLU-M", "Clothing", 59.99, 30);
        createItem(manager, "Running Shoes", "SHOE-RUN-10.5", "Clothing", 89.99, 8);
        createItem(manager, "Winter Coat", "CLOTH-WCOAT-BLK-XL", "Clothing", 129.99, 12);
        
        // Home & Kitchen
        createItem(manager, "Coffee Maker", "HOME-COFFEE-STL", "Home & Kitchen", 79.99, 10);
        createItem(manager, "Knife Set", "HOME-KNIFE-SET", "Home & Kitchen", 49.99, 0);
        createItem(manager, "Toaster", "HOME-TOASTER-2SL", "Home & Kitchen", 29.99, 7);
        createItem(manager, "Blender", "HOME-BLEND-750W", "Home & Kitchen", 69.99, 9);
    }
    
    private void createItem(StoreManager manager, String name, String sku, String category, double price, int quantity) {
        InventoryItem item = new InventoryItem();
        item.setManager(manager);
        item.setName(name);
        item.setSku(sku);
        item.setCategory(category);
        item.setPrice(price);
        item.setQuantity(quantity);
        inventoryItemRepository.save(item);
    }
}
