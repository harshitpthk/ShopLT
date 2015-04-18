package com.homelybuysapp.models;


import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import android.widget.Toast;

import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.ItemInterface;

public class ProductVariance implements ConnectionInterface {

	private  ItemInterface calling_class_object;
	private int id;
	private String name;
	private int itemCategory;
	private double price;
	private int quantity;
	private boolean get_item_bool;
	private boolean get_items_from_brand_bool;
	private Input ItemInput;
	private Input brandInput;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getItemCategory() {
		return this.itemCategory;
	}
	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return this.quantity;
	}
	public void setQuantity(int barcode) {
		this.quantity = barcode;
	}
	
	public ProductVariance(int id, String name, double price, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public  void getItem( ItemInterface calling_class_object) {
		
		this.calling_class_object = calling_class_object;
		this.get_item_bool = true;
		ItemInput = new Input(this.getId(),"productid");
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	public  void getItems( ItemInterface calling_class_object,int brandId) {
		
		this.calling_class_object = calling_class_object;
		this.get_items_from_brand_bool = true;
		brandInput = new Input(brandId,"brand");
		
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		final ProductVariance thisProductVObj = this;
		if(this.get_items_from_brand_bool == true ){
			serviceProvider.getItems( brandInput,new Callback<ArrayList<Product>>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.getKind().equals(RetrofitError.Kind.NETWORK)) {
						Toast.makeText(Globals.ApplicationContext, "Network Problem", Toast.LENGTH_SHORT).show();
				    }
					else{
					}
					
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(ArrayList<Product> itemFamily, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.simmilar_item_list = itemFamily;
					thisProductVObj.calling_class_object.ItemListGetSuccess(itemFamily);
				}
				
			});
		}
		else if(this.get_item_bool == true){
			serviceProvider.getItem( ItemInput,new Callback<Product>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.getKind().equals(RetrofitError.Kind.NETWORK)) {
						Toast.makeText(Globals.ApplicationContext, "Network Problem", Toast.LENGTH_SHORT).show();
				    }
					else{
					}
					
					ServerConnectionMaker.recieveResponse(null);
					thisProductVObj.calling_class_object.ItemGetFailure();
				}

				@Override
				public void success(Product item, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Globals.fetched_item_category = item;
								//Currently calling all the products at the same time of the item fetch, have to move it to demand based fetching
					thisProductVObj.calling_class_object.ItemGetSuccess(item);
					
				}
				
			});
		}
	
	}

}

