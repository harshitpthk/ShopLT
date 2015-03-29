/**
 * 
 */
package com.homelybuysapp.models;



/**
 * @author I300291
 *
 */
public class PreviousOrder {
	
	int orderId;
	String orderDate;
	double orderAmount;
	int orderTotalItems;
	int orderState;


	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int productId) {
		this.orderId = productId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public int getOrderTotalItems() {
		return orderTotalItems;
	}
	public void setOrderTotalItems(int orderTotalItems) {
		this.orderTotalItems = orderTotalItems;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
}
