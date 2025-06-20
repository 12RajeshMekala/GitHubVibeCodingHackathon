package com.inventory.management.service;

import com.inventory.management.dto.ActivityLogDTO;
import com.inventory.management.model.ActivityLog;
import com.inventory.management.model.StoreManager;
import com.inventory.management.repository.ActivityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository activityLogRepository;

    @InjectMocks
    private ActivityLogService activityLogService;

    private StoreManager testManager;
    private ActivityLog testLog1;
    private ActivityLog testLog2;

    @BeforeEach
    void setUp() {
        // Setup test manager
        testManager = new StoreManager();
        testManager.setId(1L);
        testManager.setName("Test Manager");
        testManager.setEmail("test@example.com");
        testManager.setPasswordHash("password123");

        // Setup test logs
        testLog1 = new ActivityLog();
        testLog1.setId(1L);
        testLog1.setAction("Created item: Test Item");
        testLog1.setItemId(1L);
        testLog1.setItemName("Test Item");
        testLog1.setTimestamp(LocalDateTime.now());
        testLog1.setManager(testManager);

        testLog2 = new ActivityLog();
        testLog2.setId(2L);
        testLog2.setAction("Updated item: Test Item");
        testLog2.setItemId(1L);
        testLog2.setItemName("Test Item");
        testLog2.setTimestamp(LocalDateTime.now().minusHours(1));
        testLog2.setManager(testManager);
    }

    @Test
    void testGetRecentActivities() {
        // Arrange
        when(activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(1L))
            .thenReturn(Arrays.asList(testLog1, testLog2));

        // Act
        List<ActivityLogDTO> result = activityLogService.getRecentActivities();

        // Assert
        assertEquals(2, result.size(), "Should return 2 log entries");
        
        // Verify first log
        assertEquals(testLog1.getId(), result.get(0).getId(), "ID should match");
        assertEquals(testLog1.getAction(), result.get(0).getAction(), "Action should match");
        assertEquals(testLog1.getItemName(), result.get(0).getItemName(), "Item name should match");
        assertEquals(testLog1.getTimestamp(), result.get(0).getTimestamp(), "Timestamp should match");
        
        // Verify second log
        assertEquals(testLog2.getId(), result.get(1).getId(), "ID should match");
        assertEquals(testLog2.getAction(), result.get(1).getAction(), "Action should match");
        assertEquals(testLog2.getItemName(), result.get(1).getItemName(), "Item name should match");
        assertEquals(testLog2.getTimestamp(), result.get(1).getTimestamp(), "Timestamp should match");
        
        verify(activityLogRepository).findTop10ByManagerIdOrderByTimestampDesc(1L);
    }

    @Test
    void testGetRecentActivities_EmptyList() {
        // Arrange
        when(activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(1L))
            .thenReturn(Arrays.asList());

        // Act
        List<ActivityLogDTO> result = activityLogService.getRecentActivities();

        // Assert
        assertEquals(0, result.size(), "Should return empty list");
        verify(activityLogRepository).findTop10ByManagerIdOrderByTimestampDesc(1L);
    }

    @Test
    void testConvertToDTO() {
        // This test uses reflection to test a private method
        // Since convertToDTO is a simple mapper function, we can test it through getRecentActivities
        
        // Arrange
        when(activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(1L))
            .thenReturn(Arrays.asList(testLog1));

        // Act
        List<ActivityLogDTO> result = activityLogService.getRecentActivities();
        ActivityLogDTO dto = result.get(0);

        // Assert
        assertEquals(testLog1.getId(), dto.getId(), "ID should match");
        assertEquals(testLog1.getAction(), dto.getAction(), "Action should match");
        assertEquals(testLog1.getItemName(), dto.getItemName(), "Item name should match");
        assertEquals(testLog1.getTimestamp(), dto.getTimestamp(), "Timestamp should match");
    }
}
