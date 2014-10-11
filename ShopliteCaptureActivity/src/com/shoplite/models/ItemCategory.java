package com.shoplite.models;

import java.util.ArrayList;

public class ItemCategory {

	private int id;
	private String name;
	private int categoryId;
	private int brandId;
	private boolean isSent;
	
	public boolean isSent() {
		return isSent;
	}
	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}
	public static int countNotSent(ArrayList<ItemCategory> ItemList){
		int count = 0;
		for(int i = 0 ; i < ItemList.size();i++){
			if(!ItemList.get(i).isSent){
				count++;
			}
		}
		return count;
	}
	
		
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	private ArrayList<Item> itemList;
	
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
	public static void setSentList(ArrayList<ItemCategory> item_order_list) {
		// TODO Auto-generated method stub
		int i;
		for(i = 0 ; i < item_order_list.size() ;i++){
			if(!item_order_list.get(i).isSent){
				item_order_list.get(i).setSent(true);
			}
		}
	}
	
	public static ArrayList<OrderItemDetail> getToSendList(ArrayList<ItemCategory> item_order_list)
	{
		int i;
		ArrayList<OrderItemDetail> toSendList = new ArrayList<OrderItemDetail>();
		for(i = 0 ; i < item_order_list.size() ; i++){
			if(!item_order_list.get(i).isSent){
				OrderItemDetail itemToOrder = new OrderItemDetail(item_order_list.get(i).getItemList().get(0).getId(),item_order_list.get(i).getItemList().get(0).getQuantity());
				toSendList.add(itemToOrder);
			}
		}
		return toSendList;
	}
	
	
}
