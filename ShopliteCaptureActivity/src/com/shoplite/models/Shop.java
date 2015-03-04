package com.shoplite.models;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.google.gson.JsonObject;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.ShopInterface;

public class Shop implements ConnectionInterface {
	private String name;
	private int id;
	private String url;
	private Location location;
	private ShopInterface calling_class_object;
	private boolean get_shop_list_bool;
	private boolean connect_to_shop_bool;
	private static Location areaLocation;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void get_shop_list(ShopInterface calling_class_object,Location areaLocation)
	{
		this.calling_class_object = calling_class_object;
		this.get_shop_list_bool = true;
		Shop.areaLocation = areaLocation;
		ServerConnectionMaker.sendRequest(this);
		
	}
	public void connect_to_shop(ShopInterface calling_class_object )
	{
		this.calling_class_object = calling_class_object;
		this.connect_to_shop_bool  = true;
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		final Shop thisShopObject = this;
		if(this.connect_to_shop_bool){
			
			serviceProvider.loginShop(thisShopObject.getId(), new Callback<JsonObject>(){

				@Override
				public void failure(RetrofitError arg0) {
					
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					ServerConnectionMaker.recieveResponse(null);
				}

				@Override
				public void success(JsonObject result, Response response) {
					
					Log.e("Retrofit Success", response.toString());
					Log.e("Retrofit Success", result.toString());
					ServerConnectionMaker.recieveResponse(response);
					Globals.connected_to_shop_success = true;
					Globals.connectedShop = thisShopObject;
					Globals.dbhelper.storeShopLocation(thisShopObject.getName(), thisShopObject.getUrl(), thisShopObject.getLocation().getLatitude(), thisShopObject.getLocation().getLongitude());
					thisShopObject.calling_class_object.shop_connected();
					
				}

				
			});
		}
		
		else if(this.get_shop_list_bool){
			
			serviceProvider.getshoplist(Shop.areaLocation, new Callback<ArrayList<Shop>>(){

				
				@Override
				public void failure(RetrofitError response) {
					
					if (response.isNetworkError()) {
						Log.e("Retrofit error", "503"); 
				    }
					else{
						Log.e("Get Shop List error", response.getMessage());
					}
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(ArrayList<Shop> shoplist, Response response) {
					Globals.shop_list.addAll(shoplist);
					Log.e("Retrofit Success", response.toString());
					ServerConnectionMaker.recieveResponse(response);
					thisShopObject.calling_class_object.shop_list_success(areaLocation,shoplist);
						
					
					
				}

				
			});
		}
		
		
		
		
	}
}
