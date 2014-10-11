package com.shoplite.models;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import com.shoplite.Utils.Globals;
import com.shoplite.Utils.util;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnection;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.ShopInterface;

public class Shop implements ConnectionInterface {
	private String name;
	private String id;
	private String url;
	private Location location;
	private static ShopInterface calling_class_object;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	
	public void get_shop_list(ShopInterface calling_class_object)
	{
		Shop.calling_class_object = calling_class_object;
		Globals.get_shop_list_bool = true;
		ServerConnection.sendRequest(this,util.starURL);
		//Toast.makeText(Globals.ApplicationContext, "getting shop lists ", Toast.LENGTH_SHORT).show();
	}
	public void connect_to_shop(ShopInterface calling_class_object , String shopURL, String shopName, Location shopLoc)
	{
		Shop.calling_class_object = calling_class_object;
		Globals.connect_to_shop_bool  = true;
		Globals.connect_to_shop_name = shopName;
		Globals.connect_to_shop_url = shopURL;
		Globals.connect_to_shop_location = shopLoc;
		shopURL= "https://" + Globals.connect_to_shop_url;
		ServerConnection.sendRequest(this,shopURL);
		//Toast.makeText(Globals.ApplicationContext, "connecting to  shop ", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
			
		if(Globals.connect_to_shop_bool && !Globals.get_shop_list_bool){
			
			serviceProvider.loginShop(ServerConnection.star_sessionID, new Callback<JsonObject>(){

				@Override
				public void failure(RetrofitError arg0) {
					
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					//Controls.dismiss_progress_dialog();
					ServerConnection.recieveResponse(null);
				}

				@Override
				public void success(JsonObject result, Response response) {
					// TODO Auto-generated method stub
					Log.e("Retrofit Success", response.toString());
					Log.e("Retrofit Success", result.toString());
					ServerConnection.recieveResponse(response);
					
					Globals.connected_to_shop_success = true;
					Globals.connected_shop_name = Globals.connect_to_shop_name;
					Globals.connected_shop_url = Globals.connect_to_shop_url;
					Globals.connected_shop_location = Globals.connect_to_shop_location;
					Globals.dbhelper.storeShopLocation(Globals.connected_shop_name, Globals.connected_shop_url, Globals.connected_shop_location.getLatitude(), Globals.connected_shop_location.getLongitude());
					Toast.makeText(Globals.ApplicationContext, ("Welcome to " + Globals.connected_shop_name), Toast.LENGTH_LONG).show();
					Shop.calling_class_object.shop_connected();
					Globals.connect_to_shop_bool = false;
				}

				
			});
		}
		
		else if(Globals.get_shop_list_bool){
			
			serviceProvider.getshoplist(Globals.current_location, new Callback<ArrayList<Shop>>(){

				
				@Override
				public void failure(RetrofitError arg0) {
					// TODO Auto-generated method stub
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					//Controls.dismiss_progress_dialog();
					ServerConnection.recieveResponse(null);
					//shop_name = null;
				}

				@Override
				public void success(ArrayList<Shop> shoplist, Response response) {
					// TODO Auto-generated method stub
					Log.e("Retrofit Success", response.toString());
					//	Log.e("Retrofit Success", shop.getName());
					//Log.e("Retrofit Success", shop.getId());
					//Toast.makeText(getBaseContext(), shop.getUrl().toString(), Toast.LENGTH_LONG).show();
					//CaptureActivity.shop_to_connect = "https://" + shoplist;
					ServerConnection.recieveResponse(response);
					
					if( shoplist != null && shoplist.size() > 0  ){
						//connect_to_shop();
						//Toast.makeText(Globals.ApplicationContext, shoplist.get(0).getUrl().toString(), Toast.LENGTH_LONG).show();
						//shop_name = shop.getName();
						Globals.shop_list = shoplist;
						Globals.shop_list_fetch_success = true;
						Globals.get_shop_list_bool = false;
						Shop.calling_class_object.shop_list_success();
					}
					else{
						//Globals.shop_list = shoplist;
						Globals.shop_list_fetch_success = true;
						Globals.get_shop_list_bool = false;
						Toast.makeText(Globals.ApplicationContext, "No shop nearby", Toast.LENGTH_LONG).show();
						Shop.calling_class_object.shop_list_success();
					}
					
				}

				
			});
		}
		
		
		
		
	}
}
