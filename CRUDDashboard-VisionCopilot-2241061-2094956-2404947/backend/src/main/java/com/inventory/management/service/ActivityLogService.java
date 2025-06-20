package com.inventory.management.service;

import com.inventory.management.dto.ActivityLogDTO;
import com.inventory.management.model.ActivityLog;
import com.inventory.management.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    
    private final ActivityLogRepository activityLogRepository;
    
    // For demo purposes, we'll use a default manager
    private static final Long DEFAULT_MANAGER_ID = 1L;
    
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> getRecentActivities() {
        List<ActivityLog> logs = activityLogRepository.findTop10ByManagerIdOrderByTimestampDesc(DEFAULT_MANAGER_ID);
        return logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private ActivityLogDTO convertToDTO(ActivityLog log) {
        ActivityLogDTO dto = new ActivityLogDTO();
        dto.setId(log.getId());
        dto.setAction(log.getAction());
        dto.setItemName(log.getItemName());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
