package com.inventory.management.repository;

import com.inventory.management.model.ActivityLog;
import com.inventory.management.model.StoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ActivityLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    private StoreManager testManager;
    private StoreManager otherManager;

    @BeforeEach
    void setUp() {
        // Create test managers
        testManager = new StoreManager();
        testManager.setName("Test Manager");
        testManager.setEmail("test@example.com");
        testManager.setPasswordHash("password123");
        entityManager.persist(testManager);

        otherManager = new StoreManager();
        otherManager.setName("Other Manager");
        otherManager.setEmail("other@example.com");
        otherManager.setPasswordHash("password456");
        entityManager.persist(otherManager);

        // Create activity logs with timestamps in descending order for sorting test
        createActivityLog("Created Item: Laptop", "Laptop", 1L, LocalDateTime.now().minusHours(1), testManager);
        createActivityLog("Updated Item: Laptop", "Laptop", 1L, LocalDateTime.now().minusHours(2), testManager);
        createActivityLog("Created Item: Mouse", "Mouse", 2L, LocalDateTime.now().minusHours(3), testManager);
        createActivityLog("Created Item: Keyboard", "Keyboard", 3L, LocalDateTime.now().minusHours(4), testManager);
        createActivityLog("Updated Item: Keyboard", "Keyboard", 3L, LocalDateTime.now().minusHours(5), testManager);
        createActivityLog("Created Item: Monitor", "Monitor", 4L, LocalDateTime.now().minusHours(6), testManager);
        createActivityLog("Deleted Item: Monitor", "Monitor", 4L, LocalDateTime.now().minusHours(7), testManager);
        createActivityLog("Created Item: Desk", "Desk", 5L, LocalDateTime.now().minusHours(8), testManager);
        createActivityLog("Updated Item: Desk", "Desk", 5L, LocalDateTime.now().minusHours(9), testManager);
        createActivityLog("Inventory Count: Desk", "Desk", 5L, LocalDateTime.now().minusHours(10), testManager);
        createActivityLog("Created Item: Chair", "Chair", 6L, LocalDateTime.now().minusHours(11), testManager);
        createActivityLog("Updated Item: Chair", "Chair", 6L, LocalDateTime.now().minusHours(12), testManager);

        // Create activity logs for the other manager
        createActivityLog("Created Item: Tablet", "Tablet", 7L, LocalDateTime.now().minusHours(1), otherManager);
        createActivityLog("Updated Item: Tablet", "Tablet", 7L, LocalDateTime.now().minusHours(2), otherManager);
        createActivityLog("Deleted Item: Tablet", "Tablet", 7L, LocalDateTime.now().minusHours(3), otherManager);

        entityManager.flush();
    }

    private void createActivityLog(String action, String itemName, Long itemId, LocalDateTime timestamp, StoreManager manager) {
        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setItemName(itemName);
        log.setItemId(itemId);
        log.setTimestamp(timestamp);
        log.setManager(manager);
        entityManager.persist(log);
    }

    @Test
    void testFindTop10ByManagerIdOrderByTimestampDesc() {
        // Act
        List<ActivityLog> recentLogs = activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(testManager.getId());

        // Assert
        assertEquals(10, recentLogs.size(), "Should return exactly 10 logs");
        
        // Verify the logs are sorted by timestamp descending (newest first)
        for (int i = 0; i < recentLogs.size() - 1; i++) {
            assertTrue(recentLogs.get(i).getTimestamp().isAfter(recentLogs.get(i + 1).getTimestamp()) ||
                       recentLogs.get(i).getTimestamp().equals(recentLogs.get(i + 1).getTimestamp()),
                      "Logs should be sorted by timestamp in descending order");
        }
        
        // Verify all returned logs belong to the test manager
        assertTrue(recentLogs.stream().allMatch(log -> log.getManager().equals(testManager)),
                "All logs should belong to the test manager");
        
        // Verify the most recent log is first
        assertEquals("Created Item: Laptop", recentLogs.get(0).getAction(), 
                "The most recent log should be first");
    }

    @Test
    void testManagerIsolation() {
        // Act
        List<ActivityLog> testManagerLogs = activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(testManager.getId());
        List<ActivityLog> otherManagerLogs = activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(otherManager.getId());

        // Assert
        assertEquals(10, testManagerLogs.size(), "Should return 10 logs for test manager");
        assertEquals(3, otherManagerLogs.size(), "Should return 3 logs for other manager");
        
        // Verify no logs from other manager in test manager's results
        assertTrue(testManagerLogs.stream().noneMatch(log -> log.getManager().equals(otherManager)),
                "Test manager logs should not contain logs from other manager");
        
        // Verify no logs from test manager in other manager's results
        assertTrue(otherManagerLogs.stream().noneMatch(log -> log.getManager().equals(testManager)),
                "Other manager logs should not contain logs from test manager");
        
        // Verify the most recent log for other manager is as expected
        assertEquals("Created Item: Tablet", otherManagerLogs.get(0).getAction(),
                "The most recent log for other manager should be as expected");
    }
}
