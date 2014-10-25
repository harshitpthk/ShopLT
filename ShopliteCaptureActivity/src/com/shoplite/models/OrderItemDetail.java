package com.shoplite.models;


public class OrderItemDetail {
	
	int orderId;
	int itemId;
	int quantity;
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
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
		this.itemId = itemId;
		this.quantity = quantity;
		this.price = price;
	}
	
	public OrderItemDetail( int itemId, int quantity) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		
	}
	
	
}
