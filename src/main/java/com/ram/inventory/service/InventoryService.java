package com.ram.inventory.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ram.inventory.dto.request.ProductAddRequest;
import com.ram.inventory.dto.response.ProductLowStockResponse;
import com.ram.inventory.dto.response.ProductResponse;


public interface InventoryService {

	public ResponseEntity<ProductResponse>addProduct(ProductAddRequest addRequest);
	
	public ResponseEntity<ProductResponse>getProductById(Long productId);
	
	public ResponseEntity<ProductResponse>updateProduct(ProductAddRequest productUpdateRequest);
	
	public ResponseEntity<ProductResponse>removeProduct(Long productId);
	
	public ResponseEntity<ProductResponse> increaseStock(Long productId, int amount);
	
	public ResponseEntity<ProductResponse> decreaseStock(Long productId, int amount);
	
	public ResponseEntity<List<ProductLowStockResponse>> getLowStockProducts();
}
