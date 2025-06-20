package com.inventory.management.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryItemTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidInventoryItem() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("Test Manager");
        manager.setEmail("test@example.com");
        manager.setPasswordHash("password");

        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setName("Test Item");
        item.setSku("TST-001");
        item.setCategory("Electronics");
        item.setPrice(99.99);
        item.setQuantity(10);
        item.setManager(manager);

        // Act
        Set<ConstraintViolation<InventoryItem>> violations = validator.validate(item);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidInventoryItem_EmptyName() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("Test Manager");
        manager.setEmail("test@example.com");
        manager.setPasswordHash("password");

        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setName(""); // Empty name
        item.setSku("TST-001");
        item.setCategory("Electronics");
        item.setPrice(99.99);
        item.setQuantity(10);
        item.setManager(manager);

        // Act
        Set<ConstraintViolation<InventoryItem>> violations = validator.validate(item);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidInventoryItem_NullCategory() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("Test Manager");
        manager.setEmail("test@example.com");
        manager.setPasswordHash("password");

        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setName("Test Item");
        item.setSku("TST-001");
        item.setCategory(null); // Null category
        item.setPrice(99.99);
        item.setQuantity(10);
        item.setManager(manager);

        // Act
        Set<ConstraintViolation<InventoryItem>> violations = validator.validate(item);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidInventoryItem_NegativePrice() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("Test Manager");
        manager.setEmail("test@example.com");
        manager.setPasswordHash("password");

        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setName("Test Item");
        item.setSku("TST-001");
        item.setCategory("Electronics");
        item.setPrice(-10.0); // Negative price
        item.setQuantity(10);
        item.setManager(manager);

        // Act
        Set<ConstraintViolation<InventoryItem>> violations = validator.validate(item);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 0", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidInventoryItem_NegativeQuantity() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("Test Manager");
        manager.setEmail("test@example.com");
        manager.setPasswordHash("password");

        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setName("Test Item");
        item.setSku("TST-001");
        item.setCategory("Electronics");
        item.setPrice(99.99);
        item.setQuantity(-5); // Negative quantity
        item.setManager(manager);

        // Act
        Set<ConstraintViolation<InventoryItem>> violations = validator.validate(item);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 0", violations.iterator().next().getMessage());
    }

    @Test
    void testOnUpdate() {
        // Arrange
        InventoryItem item = new InventoryItem();
        item.setName("Test Item");
        item.setSku("TST-001");
        item.setCategory("Electronics");
        item.setPrice(99.99);
        item.setQuantity(10);

        LocalDateTime initialTime = item.getUpdatedAt();
        
        // Wait a short time to ensure the timestamp would be different
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Act
        item.onUpdate();

        // Assert
        assertNotEquals(initialTime, item.getUpdatedAt(), 
                "UpdatedAt timestamp should be updated after onUpdate is called");
    }
}
