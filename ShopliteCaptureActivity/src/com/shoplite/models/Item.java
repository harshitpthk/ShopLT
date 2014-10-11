package com.shoplite.models;


import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import android.widget.Toast;

import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnection;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.ItemInterface;

public class Item implements ConnectionInterface {

	private static ItemInterface calling_class_object;
	private int id;
	private String name;
	private int itemCategory;
	private double price;
	private int quantity;
	
	
	private Input ItemInput = new Input();
	private Input brandInput = new Input();
	
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
	public int getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int barcode) {
		this.quantity = barcode;
	}
	
	public Item(int id, String name, double price, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public  void getItem( ItemInterface calling_class_object,int itemID) {
		// TODO Auto-generated method stub
		Item.calling_class_object = calling_class_object;
		Globals.get_item_bool = true;
		ItemInput.id = 10000;//itemID;
		ItemInput.type = "ItemCategoryId";
		String shopURL = null;
		if(Globals.connected_shop_url != null)
			shopURL  = "https://" + Globals.connected_shop_url;
		else
			shopURL  = "https://" + "planetp1940097444trial.hanatrial.ondemand.com/shop-sys/";
		ServerConnection.sendRequest(this, shopURL);
		
	}
	public  void getItems( ItemInterface calling_class_object,int brandId) {
		// TODO Auto-generated method stub
		Item.calling_class_object = calling_class_object;
		Globals.get_items_from_brand_bool = true;
		brandInput.id = brandId;//itemID;
		brandInput.type = "brand";
		String shopURL = null;
		if(Globals.connected_shop_url != null)
			shopURL  = "https://" + Globals.connected_shop_url;
		else
			shopURL  = "https://" + "planetp1940097444trial.hanatrial.ondemand.com/shop-sys/";
		ServerConnection.sendRequest(this, shopURL);
		
	}
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		if(Globals.get_items_from_brand_bool = true && !Globals.get_item_bool){
			serviceProvider.getItems( brandInput,new Callback<ArrayList<ItemCategory>>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					//Log.e("Retrofit error", response.getUrl());
					//Log.e("Retrofit error", response.getMessage());
					//Controls.dismiss_progress_dialog();
					ServerConnection.recieveResponse(null);
					
				}

				@Override
				public void success(ArrayList<ItemCategory> itemFamily, Response response) {
					// TODO Auto-generated method stub
					ServerConnection.recieveResponse(response);
					Log.e("Retrofit Success", response.toString());
					Log.e("Retrofit Success", itemFamily.toString());
					//Toast.makeText(Globals.ApplicationContext, itemFamily.toString(), Toast.LENGTH_LONG).show();
					Globals.get_items_from_brand_bool = false;
					Globals.simmilar_item_list = itemFamily;
					Item.calling_class_object.ItemListGetSuccess();
				}
				
			});
		}
		else if(Globals.get_item_bool = true){
			serviceProvider.getItem( ItemInput,new Callback<ItemCategory>(){

				@Override
				public void failure(RetrofitError response) {
					if (response.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", response.getUrl());
					Log.e("Retrofit error", response.getMessage());
					//Controls.dismiss_progress_dialog();
					ServerConnection.recieveResponse(null);
					
				}

				@Override
				public void success(ItemCategory item, Response response) {
					// TODO Auto-generated method stub
					ServerConnection.recieveResponse(response);
					Log.e("Retrofit Success", response.toString());
					Log.e("Retrofit Success", item.toString());
					Toast.makeText(Globals.ApplicationContext, item.toString(), Toast.LENGTH_LONG).show();
					Globals.get_item_bool = false;
					Globals.fetched_item_category = item;
					getItems(Item.calling_class_object,item.getBrandId());
					Item.calling_class_object.ItemGetSuccess();
					
				}
				
			});
		}
	
	}

}

