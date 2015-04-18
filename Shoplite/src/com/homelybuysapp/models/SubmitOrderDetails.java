/**
 * 
 */
package com.homelybuysapp.models;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import android.widget.Toast;

import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.SubmitOrderInterface;

/**
 * @author I300291
 *
 */
public class SubmitOrderDetails   implements ConnectionInterface {

	
	private SubmitOrderInterface calling_class_object;
	private OrderDetail oDetail;

	
	public SubmitOrderInterface getCalling_class_object() {
		return calling_class_object;
	}

	public void setCalling_class_object(SubmitOrderInterface calling_class_object) {
		this.calling_class_object = calling_class_object;
	}

	public OrderDetail getoDetail() {
		return oDetail;
	}

	public void setoDetail(OrderDetail oDetail) {
		this.oDetail = oDetail;
	}

	public void submitOrder(SubmitOrderInterface callee)
	{
		this.calling_class_object = callee;
		
		try {
			ServerConnectionMaker.sendRequest(this);
			}
		catch (RetrofitError e) {
			  System.out.println(e.getResponse().getStatus());
		}
	}

	/* (non-Javadoc)
	 * @see com.homelybuysapp.connection.ConnectionInterface#sendRequest(com.homelybuysapp.connection.ServiceProvider)
	 */
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
		
		
		final SubmitOrderInterface callee =  this.calling_class_object;
		
		serviceProvider.submitOrder(this.getoDetail(), new Callback<Integer>(){
			
			@Override
			public void failure(RetrofitError response) {
				
				if (response.getKind().equals(RetrofitError.Kind.NETWORK)) {
					Toast.makeText(Globals.ApplicationContext, "Network Problem", Toast.LENGTH_SHORT).show();
			    }
				
				ServerConnectionMaker.recieveResponse(null);
				callee.submitOrderFailure();
					
			}

			@Override
			public void success(Integer orderID, Response response) {
				
				ServerConnectionMaker.recieveResponse(response);
				callee.submitOrderSuccess(orderID);
				
				
			}
			
		});
	
	
	}
}
	

