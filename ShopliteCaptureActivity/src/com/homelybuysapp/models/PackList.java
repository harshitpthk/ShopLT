package com.homelybuysapp.models;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.google.gson.JsonObject;
import com.homelybuysapp.Utils.CartGlobals;
import com.homelybuysapp.Utils.Constants.DBState;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.PackListInterface;

public class PackList implements ConnectionInterface {

    public DBState state;
	public ArrayList<OrderItemDetail> products;
	public  PackListInterface calling_class_object;
	
	public  void sendPackList(PackListInterface calling_class_object)
	{
		this.calling_class_object = calling_class_object;
		try {
			ServerConnectionMaker.sendRequest(this);
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
					
					ServerConnectionMaker.sendRequest(CartGlobals.CartServerRequestQueue.peekFirst());
					
				}

				@Override
				public void success(JsonObject message, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					Log.e("PackList", response.toString());
					calling_class_object_internal.PackListSuccess(packlist);
					CartGlobals.CartServerRequestQueue.removeFirst();
					if(!CartGlobals.CartServerRequestQueue.isEmpty()){
						ServerConnectionMaker.sendRequest(CartGlobals.CartServerRequestQueue.peekFirst());
					}
					
					
				}
				
			});
		
		
	}
	
}