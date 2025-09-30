package com.ram.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
    
    @Column(nullable = false,unique=true)
    private String productName;
    
    private String description;
    
    @Min(0)
    private Integer stockQuantity;
    
    @Min(0)
    private int lowStock=5;


	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}




	public Integer getStockQuantity() {
		return stockQuantity;
	}


	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}


	public int getLowStock() {
		return lowStock;
	}


	public void setLowStock(int lowStock) {
		this.lowStock = lowStock;
	}


	@Override
	public String toString() {
		return "Inventory [productId=" + productId + ", productName=" + productName + ", description=" + description
				+ ", stockQuantity=" + stockQuantity + ", lowStock=" + lowStock + "]";
	}
	
    
	
}
