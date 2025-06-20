package com.inventory.management.repository;

import com.inventory.management.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByManagerId(Long managerId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity < 10 AND i.quantity > 0 AND i.manager.id = :managerId")
    List<InventoryItem> findLowStockItems(@Param("managerId") Long managerId);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0 AND i.manager.id = :managerId")
    List<InventoryItem> findOutOfStockItems(@Param("managerId") Long managerId);
    
    @Query("SELECT i FROM InventoryItem i WHERE " +
           "(LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR "+
           "LOWER(i.sku) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND i.manager.id = :managerId")
    List<InventoryItem> searchItems(String query, Long managerId);
}
