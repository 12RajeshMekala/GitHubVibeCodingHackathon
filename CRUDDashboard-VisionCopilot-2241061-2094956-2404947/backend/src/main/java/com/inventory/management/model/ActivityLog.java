package com.inventory.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private StoreManager manager;
    
    @Column(nullable = false)
    private String action;
    
    @Column(name = "item_id")
    private Long itemId;
    
    @Column(name = "item_name")
    private String itemName;
    
    private LocalDateTime timestamp = LocalDateTime.now();
}
