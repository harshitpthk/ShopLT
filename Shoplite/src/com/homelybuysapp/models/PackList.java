package com.homelybuysapp.models;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.PackListInterface;

public class PackList implements ConnectionInterface {

    public PackProducts pckProd;
	public  PackListInterface calling_class_object;
	
	public  void sendPackList(PackListInterface calling_class_object)
	{
		this.calling_class_object = calling_class_object;
		try {
			ServerConnectionMaker.sendRequest(this);
			
			}
		catch (RetrofitError e) {
			  System.out.println(e.getResponse().getStatus());
		}
	}
	
	
	
	

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
		 final PackListInterface calling_class_object_internal = this.calling_class_object;
		final PackList packlist = this;
			
			serviceProvider.packList(packlist.pckProd,new Callback<JsonObject>(){
				
				@Override
				public void failure(RetrofitError response) {
					
					if (response.getKind().equals(RetrofitError.Kind.NETWORK)) {
						Toast.makeText(Globals.ApplicationContext, "Network Problem", Toast.LENGTH_SHORT).show();
				    }
					
					ServerConnectionMaker.recieveResponse(null);
					
					
				}

				@Override
				public void success(JsonObject message, Response response) {
					
					ServerConnectionMaker.recieveResponse(response);
					
					calling_class_object_internal.PackListSuccess(packlist);

					
					
				}
				
			});
		
		
	}
	
}