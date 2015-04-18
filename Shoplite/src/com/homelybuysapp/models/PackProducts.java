package com.homelybuysapp.models;

import java.util.ArrayList;

import com.homelybuysapp.Utils.Constants.DBState;

public class PackProducts {
	private DBState state;
	private ArrayList<OrderItemDetail> products;
	
	public PackProducts(DBState state, ArrayList<OrderItemDetail> products) {
		super();
		this.state = state;
		this.products = products;
	}
	
	public DBState getState() {
		return state;
	}
	public void setState(DBState state) {
		this.state = state;
	}
	public ArrayList<OrderItemDetail> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<OrderItemDetail> products) {
		this.products = products;
	}
}
