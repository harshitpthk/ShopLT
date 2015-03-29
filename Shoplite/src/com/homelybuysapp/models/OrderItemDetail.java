package com.homelybuysapp.models;


public class OrderItemDetail {
	
	int orderId;
	int varianceId;
	int quantity;
	int productId;
	String varianceName;
	String productName;
	
	double price;
	public boolean isSent;
	
	public boolean isSent() {
		return isSent;
	}
	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getItemId() {
		return varianceId;
	}
	public void setItemId(int itemId) {
		this.varianceId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getVarianceId() {
		return varianceId;
	}
	public void setVarianceId(int varianceId) {
		this.varianceId = varianceId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getVarianceName() {
		return varianceName;
	}
	public void setVarianceName(String varianceName) {
		this.varianceName = varianceName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public OrderItemDetail(int orderId, int itemId, int quantity, int price) {
		super();
		this.orderId = orderId;
		this.varianceId = itemId;
		this.quantity = quantity;
		this.price = price;
	}
	
	public OrderItemDetail( int itemId, int quantity) {
		super();
		this.varianceId = itemId;
		this.quantity = quantity;
		
	}
	
	
}
