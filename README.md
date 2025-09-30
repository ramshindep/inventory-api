# Inventory Management API

## 1. Project Description

The **Inventory Management API** is a Spring Boot-based RESTful service for managing products in an inventory. It provides functionality to:

- Add new products.
- Update product details including name, description, and stock quantity.
- Increase or decrease stock for existing products.
- Retrieve low-stock products for better inventory tracking.
- Prevent duplicate product entries by product name.

The API uses **Spring Boot**, **Spring Data JPA**, and **MySQL** to provide efficient product management operations with consistent response messages for success and error scenarios.


## 2. Setting Up and Running Locally

### Prerequisites
- Java 17 or higher
- Maven
- MySQL

### Steps

1. **Clone the repository:**
git clone <your-repo-url>
cd <your-project-folder>



## 2. Setting Up and Running Locally

### Configure the Database
1. Create a MySQL database, e.g. inventory_db.
2. Update application.properties with your database credentials

## Build and Run the Application

To build and run the application:

mvn clean install
mvn spring-boot:run

## Test the Api

POST /api/inventory/add  ->	Add a new product
POST  /api/inventory/update => update a existing product
GET  /api/inventory/get/{productId}=>get a product
DELETE /api/inventory/delete/{productId} => delete a product
POST/api/inventory/increaseStock?productId=1&amount=5	 =>Increase stock
POST/api/inventory/decreaseStock?productId=1&amount=2	=>Decrease stock
GET/api/inventory/getLowStockProducts  	=>Retrieve low-stock products

## Run Test cases

mvn Test

## Assumption and Design Choices

- Unique Products: Product names are unique, duplicates are not allowed.

- Stock Validation: Stock cannot be negative, decreasing stock below zero returns an error.

- Consistent Responses: Each API returns a response wrapper containing:

  data: Product information

  responseCode: HTTP status code

- responseMessage: Success/error message





