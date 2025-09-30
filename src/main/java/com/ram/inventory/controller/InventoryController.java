package com.ram.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ram.inventory.dto.request.ProductAddRequest;
import com.ram.inventory.dto.response.ProductLowStockResponse;
import com.ram.inventory.dto.response.ProductResponse;
import com.ram.inventory.service.InventoryServiceImpl;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {
	
	@Autowired
	InventoryServiceImpl invetoryServiceImpl;

	@PostMapping("/add")
	public ResponseEntity<ProductResponse>addProduct(@RequestBody ProductAddRequest productAddRequest){
		return invetoryServiceImpl.addProduct(productAddRequest);
	}
	
	@PostMapping("/update")
	public ResponseEntity<ProductResponse>updateProduct(@RequestBody ProductAddRequest productUpdateRequest){
		return invetoryServiceImpl.updateProduct(productUpdateRequest);
	}
	
	@PostMapping("/get/{productId}")
	public ResponseEntity<ProductResponse>getProductById(@PathVariable Long productId){
		return invetoryServiceImpl.getProductById(productId);
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<ProductResponse>removeProduct(@PathVariable Long productId){
		return invetoryServiceImpl.removeProduct(productId);
	}
	
	@PostMapping("/increaseStock")
	public ResponseEntity<ProductResponse>increaseStock(@RequestParam Long productId,@RequestParam int amount){
		return invetoryServiceImpl.increaseStock(productId, amount);
	}
	
	@PostMapping("/decreaseStock")
	public ResponseEntity<ProductResponse>decreaseStock(@RequestParam Long productId, @RequestParam int amount){
		return invetoryServiceImpl.decreaseStock(productId, amount);
	}
	
	
	@GetMapping("/getlowQuanity")
	public ResponseEntity<List<ProductLowStockResponse>> getLowStockProducts(){
		return invetoryServiceImpl.getLowStockProducts();
	}
	
}
