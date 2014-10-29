package com.shoplite.models;

import java.util.ArrayList;

public class ItemCategory {

	private int categoryId;
	private int id;
	private String name;
	private int brandId;
	private ArrayList<Item> itemList;
	private int currentItemId;
	private int currentQty;
	private String currentMeasure;
	private Double currentMsrPrice;
	private Double totalPrice;
	private int quantity;
	private String imageUrl;
	private boolean isSent;
	
	public int getCurrentItemId() {
		return currentItemId;
	}
	public void setCurrentItemId(int currentItemId) {
		this.currentItemId = currentItemId;
	}
	public int getCurrentQty() {
		return currentQty;
	}
	public void setCurrentQty(int currentQty) {
		this.currentQty = currentQty;
	}
	public String getCurrentMeasure() {
		return currentMeasure;
	}
	public void setCurrentMeasure(String currentMeasure) {
		this.currentMeasure = currentMeasure;
	}
	public Double getCurrentMsrPrice() {
		return currentMsrPrice;
	}
	public void setCurrentMsrPrice(Double currentMsrPrice) {
		this.currentMsrPrice = currentMsrPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public boolean isSent() {
		return isSent;
	}
	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}
	
		
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public ItemCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public ArrayList<Item> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}
	
	
	
	
	public static int countNotSent(ArrayList<ItemCategory> item_order_list){
		int count = 0;
		for(int i = 0 ; i < item_order_list.size();i++){
			if(!item_order_list.get(i).isSent()){
				count++;
			}
		}
		return count;
	}
	
	public static void setSentList(ArrayList<ItemCategory> item_order_list) {
		int i;
		for(i = 0 ; i < item_order_list.size() ;i++){
			if(!item_order_list.get(i).isSent()){
				item_order_list.get(i).setSent(true);
			}
		}
	}
	
	public static ArrayList<OrderItemDetail> getToSendList(ArrayList<ItemCategory> item_order_list)
	{
		int i;
		ArrayList<OrderItemDetail> toSendList = new ArrayList<OrderItemDetail>();
		for(i = 0 ; i < item_order_list.size() ; i++){
			if(!item_order_list.get(i).isSent()){
				OrderItemDetail itemToOrder = new OrderItemDetail(item_order_list.get(i).getCurrentItemId(),item_order_list.get(i).getCurrentQty());
				toSendList.add(itemToOrder);
			}
		}
		return toSendList;
	}
	
	
}
