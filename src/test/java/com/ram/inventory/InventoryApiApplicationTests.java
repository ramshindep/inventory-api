package com.ram.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.ram.inventory.dto.request.ProductAddRequest;
import com.ram.inventory.dto.response.InventoryData;
import com.ram.inventory.dto.response.ProductLowStockResponse;
import com.ram.inventory.dto.response.ProductResponse;
import com.ram.inventory.entity.Inventory;
import com.ram.inventory.repository.InventoryRepository;
import com.ram.inventory.service.InventoryServiceImpl;

@SpringBootTest
class InventoryApiApplicationTests {

	 @Mock
	    private InventoryRepository inventoryRepo;

	    @InjectMocks
	    private InventoryServiceImpl productService; 

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAddProduct_ProductAlreadyExists() {
	 
	        ProductAddRequest request = new ProductAddRequest();
	        request.setProductName("ExistingProduct");

	        Inventory existingInventory = new Inventory();
	        existingInventory.setProductName("ExistingProduct");

	        when(inventoryRepo.findByProductName("ExistingProduct")).thenReturn(Optional.of(existingInventory));

	  
	        ResponseEntity<ProductResponse> response = productService.addProduct(request);

	   
	        assertEquals(CONFLICT.value(), response.getBody().getResponseCode());
	        assertEquals("Product with the same name already exists", response.getBody().getResponseMessage());
	        verify(inventoryRepo, times(1)).findByProductName("ExistingProduct");
	        verify(inventoryRepo, never()).saveAndFlush(any());
	    }

	    @Test
	    void testAddProduct_NewProductAddedSuccessfully() {
	
	        ProductAddRequest request = new ProductAddRequest();
	        request.setProductName("Laptop");
	        request.setDescription("Hp");
	        request.setStockQuantity(10);

	        when(inventoryRepo.findByProductName("Laptop")).thenReturn(Optional.empty());

	        Inventory savedInventory = new Inventory();
	        savedInventory.setProductId(1L);
	        savedInventory.setProductName("Laptop");
	        savedInventory.setDescription("Hp");
	        savedInventory.setStockQuantity(10);

	        when(inventoryRepo.saveAndFlush(any(Inventory.class))).thenReturn(savedInventory);

	  
	        ResponseEntity<ProductResponse> response = productService.addProduct(request);

	  
	        assertEquals(CREATED.value(), response.getBody().getResponseCode());
	        assertEquals("Product Added Successfully", response.getBody().getResponseMessage());

	        InventoryData data = response.getBody().getData();
	        assertNotNull(data);
	        assertEquals(1L, data.getProductId());
	        assertEquals("Laptop", data.getProductName());
	        assertEquals("Hp", data.getDescription());
	        assertEquals(10, data.getStockQuantity());

	        verify(inventoryRepo, times(1)).findByProductName("Laptop");
	        verify(inventoryRepo, times(1)).saveAndFlush(any(Inventory.class));
	    }
	    
	    @Test
	    void testGetProductById_ProductFound() {
	        Long productId = 1L;
	        Inventory inventory = new Inventory();
	        inventory.setProductId(productId);
	        inventory.setProductName("Laptop");
	        inventory.setDescription("Hp");
	        inventory.setStockQuantity(10);

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(inventory));

	        ResponseEntity<ProductResponse> response = productService.getProductById(productId);

	        assertEquals(OK.value(), response.getBody().getResponseCode());
	        assertEquals("product With provided id: " + productId + " Found", response.getBody().getResponseMessage());
	        assertNotNull(response.getBody().getData());
	        assertEquals(productId, response.getBody().getData().getProductId());
	        assertEquals("Laptop", response.getBody().getData().getProductName());
	    }

	    @Test
	    void testGetProductById_ProductNotFound() {
	        Long productId = 1L;
	        when(inventoryRepo.findById(productId)).thenReturn(Optional.empty());

	        ResponseEntity<ProductResponse> response = productService.getProductById(productId);

	        assertEquals(NOT_FOUND.value(), response.getBody().getResponseCode());
	        assertEquals("product with provided id is Not found", response.getBody().getResponseMessage());
	        assertNull(response.getBody().getData());
	    }

	
	    @Test
	    void testUpdateProduct_ProductFoundAndUpdated() {
	        Long productId = 1L;
	        ProductAddRequest updateRequest = new ProductAddRequest();
	        updateRequest.setProductId(productId);
	        updateRequest.setProductName("Laptop");
	        updateRequest.setDescription("Hp");
	        updateRequest.setStockQuantity(15);

	        Inventory existingInventory = new Inventory();
	        existingInventory.setProductId(productId);
	        existingInventory.setProductName("OldName");
	        existingInventory.setDescription("OldDesc");
	        existingInventory.setStockQuantity(10);

	        Inventory savedInventory = new Inventory();
	        savedInventory.setProductId(productId);
	        savedInventory.setProductName("Laptop"); // same as request
	        savedInventory.setDescription("Hp");     // same as request
	        savedInventory.setStockQuantity(15);     // same as request

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(existingInventory));
	        when(inventoryRepo.saveAndFlush(any(Inventory.class))).thenReturn(savedInventory);

	        ResponseEntity<ProductResponse> response = productService.updateProduct(updateRequest);

	        assertEquals(CREATED.value(), response.getBody().getResponseCode());
	        assertEquals("product record updated successfully", response.getBody().getResponseMessage());

	        assertNotNull(response.getBody().getData());
	        assertEquals(productId, response.getBody().getData().getProductId());
	        assertEquals("Laptop", response.getBody().getData().getProductName());
	        assertEquals("Hp", response.getBody().getData().getDescription());
	        assertEquals(15, response.getBody().getData().getStockQuantity());
	    }

	    @Test
	    void testUpdateProduct_ProductNotFound() {
	        ProductAddRequest updateRequest = new ProductAddRequest();
	        updateRequest.setProductId(1L);

	        when(inventoryRepo.findById(1L)).thenReturn(Optional.empty());

	        ResponseEntity<ProductResponse> response = productService.updateProduct(updateRequest);

	        assertEquals(NOT_FOUND.value(), response.getBody().getResponseCode());
	        assertEquals("Product with provided id is not found", response.getBody().getResponseMessage());
	    }

	   
	    @Test
	    void testRemoveProduct_ProductFound() {
	        Long productId = 1L;
	        Inventory inventory = new Inventory();

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(inventory));
	        doNothing().when(inventoryRepo).deleteById(productId);

	        ResponseEntity<ProductResponse> response = productService.removeProduct(productId);

	        assertEquals(OK.value(), response.getBody().getResponseCode());
	        assertEquals("Product with provided id is Removed SuccessFully", response.getBody().getResponseMessage());
	        verify(inventoryRepo, times(1)).deleteById(productId);
	    }

	    @Test
	    void testRemoveProduct_ProductNotFound() {
	        Long productId = 1L;

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.empty());

	        ResponseEntity<ProductResponse> response = productService.removeProduct(productId);

	        assertEquals(NOT_FOUND.value(), response.getBody().getResponseCode());
	        assertEquals("Product Not Found", response.getBody().getResponseMessage());
	        verify(inventoryRepo, never()).deleteById(anyLong());
	    }

	   
	    @Test
	    void testIncreaseStock_ProductFound() {
	        Long productId = 1L;
	        int amount = 5;

	        Inventory inventory = new Inventory();
	        inventory.setProductId(productId);
	        inventory.setStockQuantity(10);

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(inventory));
	        when(inventoryRepo.saveAndFlush(inventory)).thenReturn(inventory);

	        ResponseEntity<ProductResponse> response = productService.increaseStock(productId, amount);

	        assertEquals(OK.value(), response.getBody().getResponseCode());
	        assertTrue(response.getBody().getResponseMessage().contains("Stock increased successfully"));
	        assertTrue(response.getBody().getResponseMessage().contains("Total stock is 15"));
	        verify(inventoryRepo, times(1)).saveAndFlush(inventory);
	    }

	    @Test
	    void testIncreaseStock_ProductNotFound() {
	        Long productId = 1L;
	        int amount = 5;

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.empty());

	        ResponseEntity<ProductResponse> response = productService.increaseStock(productId, amount);

	        assertEquals(NOT_FOUND.value(), response.getBody().getResponseCode());
	        assertEquals("Product Not Found", response.getBody().getResponseMessage());
	    }

	  
	    @Test
	    void testDecreaseStock_ProductFound_SufficientStock() {
	        Long productId = 1L;
	        int amount = 5;

	        Inventory inventory = new Inventory();
	        inventory.setProductId(productId);
	        inventory.setStockQuantity(10);

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(inventory));
	        when(inventoryRepo.saveAndFlush(inventory)).thenReturn(inventory);

	        ResponseEntity<ProductResponse> response = productService.decreaseStock(productId, amount);

	        assertEquals(OK.value(), response.getBody().getResponseCode());
	        assertTrue(response.getBody().getResponseMessage().contains("Stock decreased successfully"));
	        assertTrue(response.getBody().getResponseMessage().contains("now Available stock is 5"));
	        verify(inventoryRepo, times(1)).saveAndFlush(inventory);
	    }

	    @Test
	    void testDecreaseStock_ProductFound_InsufficientStock() {
	        Long productId = 1L;
	        int amount = 15;

	        Inventory inventory = new Inventory();
	        inventory.setProductId(productId);
	        inventory.setStockQuantity(10);

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.of(inventory));

	        ResponseEntity<ProductResponse> response = productService.decreaseStock(productId, amount);

	        assertEquals(BAD_REQUEST.value(), response.getBody().getResponseCode());
	        assertTrue(response.getBody().getResponseMessage().contains("Insufficient stock"));
	        assertTrue(response.getBody().getResponseMessage().contains("quantity Left only: 10"));
	        verify(inventoryRepo, never()).saveAndFlush(any());
	    }

	    @Test
	    void testDecreaseStock_ProductNotFound() {
	        Long productId = 1L;
	        int amount = 5;

	        when(inventoryRepo.findById(productId)).thenReturn(Optional.empty());

	        ResponseEntity<ProductResponse> response = productService.decreaseStock(productId, amount);

	        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getResponseCode());
	        assertEquals("Product Not Found", response.getBody().getResponseMessage());
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }


	   
	    
	    @Test
	    void testGetLowStockProducts_Found() {
	        Inventory lowStock1 = new Inventory();
	        lowStock1.setProductId(1L);
	        lowStock1.setProductName("Laptop");
	        lowStock1.setDescription("HP");
	        lowStock1.setStockQuantity(2);

	        Inventory lowStock2 = new Inventory();
	        lowStock2.setProductId(2L);
	        lowStock2.setProductName("Buds");
	        lowStock2.setDescription("Boat");
	        lowStock2.setStockQuantity(3);

	        List<Inventory> lowStockList = Arrays.asList(lowStock1, lowStock2);

	        when(inventoryRepo.findLowStockProducts()).thenReturn(lowStockList);

	        ResponseEntity<List<ProductLowStockResponse>> response = productService.getLowStockProducts();

	        assertEquals(OK.value(), response.getStatusCodeValue());
	        assertFalse(response.getBody().isEmpty());
	        assertEquals(2, response.getBody().size());
	        assertEquals("Low stock product found", response.getBody().get(0).getResponseMessage());
	    }
	    
	    
	}



