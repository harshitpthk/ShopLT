package com.shoplite.models;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.google.gson.JsonObject;
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.PackListInterface;

public class PackList implements ConnectionInterface {

    public DBState state;
	public ArrayList<OrderItemDetail> items;
	public  PackListInterface calling_class_object;
	
	public  void sendPackList(PackListInterface calling_class_object)
	{
		this.calling_class_object = calling_class_object;
		String shopURL = null;
		if(Globals.connected_shop_url != null)
			shopURL  = "https://" + Globals.connected_shop_url;
		else
			shopURL  = "https://" + "planetp1940097444trial.hanatrial.ondemand.com/shop-sys/";
		
		try {
			ServerConnectionMaker.sendRequest(this, shopURL);
			Log.e("sendPackList"," called");
			}
		catch (RetrofitError e) {
			  System.out.println(e.getResponse().getStatus());
		}
	}
	
	
	
	

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
			final PackList packlist = this;
			final  PackListInterface calling_class_object_internal = packlist.calling_class_object;
			packlist.calling_class_object = null;
			
			serviceProvider.packList(packlist,new Callback<JsonObject>(){
				
				@Override
				public void failure(RetrofitError response) {
					
					if (response.isNetworkError()) {
						Log.e("Packlist error", "503"); 
				    }
					Log.e("PackList error", response.getUrl());
					Log.e("PackList error", response.getMessage());
					Log.e("PackList error", response.getStackTrace().toString());
					ServerConnectionMaker.recieveResponse(null);
					String shopURL = null;
					if(Globals.connected_shop_url != null)
						shopURL  = "https://" + Globals.connected_shop_url;
					else
						shopURL  = "https://" + "planetp1940097444trial.hanatrial.ondemand.com/shop-sys/";
			
					ServerConnectionMaker.sendRequest(CartGlobals.CartServerRequestQueue.peekFirst(), shopURL);
					
				}

				@Override
				public void success(JsonObject message, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Log.e("PackList", response.toString());
					calling_class_object_internal.PackListSuccess(packlist);
					CartGlobals.CartServerRequestQueue.removeFirst();
					if(!CartGlobals.CartServerRequestQueue.isEmpty()){
						String shopURL = null;
						if(Globals.connected_shop_url != null)
							shopURL  = "https://" + Globals.connected_shop_url;
						else
							shopURL  = "https://" + "planetp1940097444trial.hanatrial.ondemand.com/shop-sys/";
						ServerConnectionMaker.sendRequest(CartGlobals.CartServerRequestQueue.peekFirst(), shopURL);
					}
					
					
				}
				
			});
		
		
	}
	
}