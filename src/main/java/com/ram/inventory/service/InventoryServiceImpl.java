package com.ram.inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ram.inventory.dto.request.ProductAddRequest;
import com.ram.inventory.dto.response.InventoryData;
import com.ram.inventory.dto.response.ProductData;
import com.ram.inventory.dto.response.ProductLowStockResponse;
import com.ram.inventory.dto.response.ProductResponse;

import com.ram.inventory.entity.Inventory;
import com.ram.inventory.repository.InventoryRepository;
@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepo;

	 Inventory inventory;
    @Autowired
	private ProductResponse productResponse;
    
    @Autowired
    private ProductLowStockResponse productLowStockResponse;
     @Autowired
	InventoryData productData;
     
     @Autowired
     ProductData data;

	@Override
	public ResponseEntity<ProductResponse> addProduct(ProductAddRequest addRequest) {

		productResponse = new ProductResponse();
		productResponse.setData(new InventoryData() );

		Optional<Inventory> existingProduct = inventoryRepo.findByProductName(addRequest.getProductName());
		if (existingProduct.isPresent()) {
			productResponse.setResponseCode(HttpStatus.CONFLICT.value());
			productResponse.setResponseMessage("Product with the same name already exists");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(productResponse);
		}

		inventory = new Inventory();

		inventory.setProductName(addRequest.getProductName());
		inventory.setDescription(addRequest.getDescription());
		inventory.setStockQuantity(addRequest.getStockQuantity());

		inventory = inventoryRepo.saveAndFlush(inventory);

		productResponse.setResponseCode(HttpStatus.CREATED.value());
		productResponse.setResponseMessage("Product Added Successfully");
		productResponse.getData().setProductId(inventory.getProductId());
		productResponse.getData().setProductName(inventory.getProductName());
		productResponse.getData().setDescription(inventory.getDescription());
		productResponse.getData().setStockQuantity(inventory.getStockQuantity());

		return ResponseEntity.status(HttpStatus.OK).body(productResponse);
	}

	@Override
	public ResponseEntity<ProductResponse> getProductById(Long ProductId) {
		Optional<Inventory> product = inventoryRepo.findById(ProductId);
		if (product.isPresent()) {
			Inventory inventory = product.get();
			productData = new InventoryData();
			productData.setProductId(inventory.getProductId());
			productData.setProductName(inventory.getProductName());
			productData.setDescription(inventory.getDescription());
			productData.setStockQuantity(inventory.getStockQuantity());

			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.OK.value());
			productResponse.setResponseMessage("product With provided id: " + ProductId +" Found");
			productResponse.setData(productData);

			return ResponseEntity.status(HttpStatus.OK).body(productResponse);

		} else {
			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productResponse.setResponseMessage("product with provided id is Not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productResponse);
		}

	}

	@Override
	public ResponseEntity<ProductResponse> updateProduct(ProductAddRequest productUpdateRequest) {
		productResponse = new ProductResponse();
		Optional<Inventory> product = inventoryRepo.findById(productUpdateRequest.getProductId());
		if (product.isPresent()) {
			Inventory productToUpdate = product.get();
			if (productUpdateRequest.getProductName() != null) {
				productToUpdate.setProductName(productUpdateRequest.getProductName());
			}
			if (productUpdateRequest.getDescription() != null) {
				productToUpdate.setDescription(productUpdateRequest.getDescription());
			}
			
			if (productUpdateRequest.getStockQuantity() != null && productUpdateRequest.getStockQuantity() >= 0) {
			    productToUpdate.setStockQuantity(productUpdateRequest.getStockQuantity());
			}

			inventory = inventoryRepo.saveAndFlush(productToUpdate);
			productResponse.setData(new InventoryData()); 
			productResponse.setResponseCode(HttpStatus.CREATED.value());
			productResponse.setResponseMessage("product record updated successfully");
			productResponse.getData().setProductId(inventory.getProductId());
			productResponse.getData().setProductName(inventory.getProductName());
			productResponse.getData().setDescription(inventory.getDescription());
			productResponse.getData().setStockQuantity(inventory.getStockQuantity());

			return ResponseEntity.status(HttpStatus.OK).body(productResponse);

		} else {
			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productResponse.setResponseMessage("Product with provided id is not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productResponse);
		}

	}

	@Override
	public ResponseEntity<ProductResponse> removeProduct(Long productId) {
		productResponse = new ProductResponse();
		Optional<Inventory> existingProduct = inventoryRepo.findById(productId);
		if (existingProduct.isPresent()) {
			inventoryRepo.deleteById(productId);
			productResponse.setResponseCode(HttpStatus.OK.value());
			productResponse.setResponseMessage("Product with provided id is Removed SuccessFully");
			return ResponseEntity.status(HttpStatus.OK).body(productResponse);

		} else {
			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productResponse.setResponseMessage("Product Not Found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productResponse);
		}

	}

	@Override
	public ResponseEntity<ProductResponse> increaseStock(Long productId, int amount) {
		Optional<Inventory> product = inventoryRepo.findById(productId);
		if (product.isPresent()) {
			Inventory inv = product.get();
			inv.setStockQuantity(inv.getStockQuantity() + amount);
			inventoryRepo.saveAndFlush(inv);

			ProductResponse response = new ProductResponse();
			response.setResponseCode(HttpStatus.OK.value());
			response.setResponseMessage("Stock increased successfully"+"Total stock is " + inv.getStockQuantity());
			return ResponseEntity.ok(response);
		} else {
			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productResponse.setResponseMessage("Product Not Found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productResponse);
		}
	}

	@Override
	public ResponseEntity<ProductResponse> decreaseStock(Long productId, int amount) {
		Optional<Inventory> product = inventoryRepo.findById(productId);
		if (product.isPresent()) {
			Inventory inv = product.get();
			if (inv.getStockQuantity() < amount) {
				productResponse = new ProductResponse();
				productResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
				productResponse.setResponseMessage("Insufficient stock " +"quantity Left only: "+inv.getStockQuantity());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productResponse);
			}
			inv.setStockQuantity(inv.getStockQuantity() - amount);
			inventoryRepo.saveAndFlush(inv);

			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.OK.value());
	
			productResponse.setResponseMessage("Stock decreased successfully "+"now Available stock is " + inv.getStockQuantity());
			return ResponseEntity.ok(productResponse);
		} else {
			productResponse = new ProductResponse();
			productResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productResponse.setResponseMessage("Product Not Found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productResponse);
		}
	}

	@Override
	public ResponseEntity<List<ProductLowStockResponse>> getLowStockProducts() {
		List<Inventory> lowStockItems = inventoryRepo.findLowStockProducts();

		List<ProductLowStockResponse> responseList = new ArrayList<>();

		if (!lowStockItems.isEmpty()) {
			for (Inventory inv : lowStockItems) {
				productLowStockResponse = new ProductLowStockResponse();
				productLowStockResponse.setResponseCode(HttpStatus.OK.value());
				productLowStockResponse.setResponseMessage("Low stock product found");

				data = new ProductData();
				data.setProductId(inv.getProductId());
				data.setProductName(inv.getProductName());
				data.setDescription(inv.getDescription());
				data.setStockQuantity(inv.getStockQuantity());
				productLowStockResponse.setData(data);

				responseList.add(productLowStockResponse);
			}

			return ResponseEntity.status(HttpStatus.OK).body(responseList);
		} else {
			productLowStockResponse = new ProductLowStockResponse();
			productLowStockResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
			productLowStockResponse.setResponseMessage("Low Stock Quantity Not Found");
			responseList.add(productLowStockResponse);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseList);
		}
	}
	
	

}
