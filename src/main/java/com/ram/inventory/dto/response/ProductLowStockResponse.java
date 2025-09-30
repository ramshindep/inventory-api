package com.ram.inventory.dto.response;

import org.springframework.stereotype.Component;

@Component
public class ProductLowStockResponse {
	private int ResponseCode;
	private String ResponseMessage;
	private ProductData data;
	public int getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(int responseCode) {
		ResponseCode = responseCode;
	}
	public String getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}
	public ProductData getData() {
		return data;
	}
	public void setData(ProductData data) {
		this.data = data;
	}
	
	
}
