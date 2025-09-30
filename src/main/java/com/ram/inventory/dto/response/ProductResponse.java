package com.ram.inventory.dto.response;

import org.springframework.stereotype.Component;

@Component
public class ProductResponse {
	private int ResponseCode;
	private String ResponseMessage;
	private InventoryData data;
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
	public InventoryData getData() {
		return data;
	}
	public void setData(InventoryData data) {
		this.data = data;
	}
	
	
	
} 
