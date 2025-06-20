package com.inventory.management.repository;

import com.inventory.management.model.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreManagerRepository extends JpaRepository<StoreManager, Long> {
    Optional<StoreManager> findByEmail(String email);
}
