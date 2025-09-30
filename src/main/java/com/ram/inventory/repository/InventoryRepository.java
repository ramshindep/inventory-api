package com.ram.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ram.inventory.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

	@Query("SELECT i FROM Inventory i WHERE i.stockQuantity < i.lowStock")
	List<Inventory> findLowStockProducts();


	Optional<Inventory> findByProductName(String productName);
	
}
