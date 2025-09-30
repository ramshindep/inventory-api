package com.ram.inventory.dto.response;

import org.springframework.stereotype.Component;

@Component
public class ProductData {

	 private Long productId;
		private String productName;
		private String description;
		private Integer stockQuantity;
		 private int lowStock;
		 
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
		 
		 
}
