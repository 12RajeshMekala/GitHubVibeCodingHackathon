package com.inventory.management.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StoreManagerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidStoreManager() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("John Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPasswordHash("hashedpassword123");
        manager.setCreatedAt(LocalDateTime.now());

        // Act
        Set<ConstraintViolation<StoreManager>> violations = validator.validate(manager);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidStoreManager_EmptyName() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName(""); // Empty name
        manager.setEmail("john.doe@example.com");
        manager.setPasswordHash("hashedpassword123");

        // Act
        Set<ConstraintViolation<StoreManager>> violations = validator.validate(manager);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidStoreManager_InvalidEmail() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("John Doe");
        manager.setEmail("invalid-email"); // Invalid email format
        manager.setPasswordHash("hashedpassword123");

        // Act
        Set<ConstraintViolation<StoreManager>> violations = validator.validate(manager);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must be a well-formed email address", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidStoreManager_EmptyPasswordHash() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("John Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPasswordHash(""); // Empty password hash

        // Act
        Set<ConstraintViolation<StoreManager>> violations = validator.validate(manager);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testEqualsAndHashcode() {
        // Arrange
        StoreManager manager1 = new StoreManager();
        manager1.setId(1L);
        manager1.setName("John Doe");
        manager1.setEmail("john.doe@example.com");
        manager1.setPasswordHash("hashedpassword123");

        StoreManager manager2 = new StoreManager();
        manager2.setId(1L);
        manager2.setName("John Doe");
        manager2.setEmail("john.doe@example.com");
        manager2.setPasswordHash("hashedpassword123");

        StoreManager manager3 = new StoreManager();
        manager3.setId(2L);
        manager3.setName("Jane Smith");
        manager3.setEmail("jane.smith@example.com");
        manager3.setPasswordHash("hashedpassword456");

        // Act & Assert
        assertEquals(manager1, manager2, "Managers with the same properties should be equal");
        assertNotEquals(manager1, manager3, "Managers with different properties should not be equal");
        assertEquals(manager1.hashCode(), manager2.hashCode(), "Equal managers should have the same hashcode");
    }

    @Test
    void testToString() {
        // Arrange
        StoreManager manager = new StoreManager();
        manager.setId(1L);
        manager.setName("John Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPasswordHash("hashedpassword123");

        // Act
        String toStringResult = manager.toString();

        // Assert
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("John Doe"));
        assertTrue(toStringResult.contains("john.doe@example.com"));
    }
}
