/**
 * 
 */
package com.shoplite.models;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.shoplite.Utils.Constants;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.SubmitOrderInterface;

/**
 * @author I300291
 *
 */
public class SubmitOrderDetails   implements ConnectionInterface {

	Constants.ORDERStatus status;
	String address;
	double latitude;
	double longitude;
	double amount;
	String refNumber;
	private SubmitOrderInterface calling_class_object;
	

	public Constants.ORDERStatus getState() {
		return status;
	}

	public void setState(Constants.ORDERStatus state) {
		this.status = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public void submitOrder(SubmitOrderInterface callee)
	{
		this.calling_class_object = callee;
		
		try {
			ServerConnectionMaker.sendRequest(this);
			Log.e("sendPackList"," called");
			}
		catch (RetrofitError e) {
			  System.out.println(e.getResponse().getStatus());
		}
	}

	/* (non-Javadoc)
	 * @see com.shoplite.connection.ConnectionInterface#sendRequest(com.shoplite.connection.ServiceProvider)
	 */
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
		final SubmitOrderDetails submitOrder = this;
		final SubmitOrderInterface callee =  submitOrder.calling_class_object;
		submitOrder.calling_class_object = null;
		serviceProvider.submitOrder(submitOrder, new Callback<Integer>(){
			
			@Override
			public void failure(RetrofitError response) {
				
				if (response.isNetworkError()) {
					Log.e("SubmitOrderPlanet error", "503"); 
			    }
				//Log.e("SubmitOrderPlanet error", response.getUrl());
				//Log.e("SubmitOrderPlanet error", response.getMessage());
				//Log.e("SubmitOrderPlanet error", response.getStackTrace().toString());
				ServerConnectionMaker.recieveResponse(null);
				Log.e("Submit order error","error");
					
			}

			@Override
			public void success(Integer orderID, Response response) {
				
				ServerConnectionMaker.recieveResponse(response);
				Log.e("SubmitOrderSuccess", response.toString());
				callee.submitOrderSuccess(orderID);
				
				
			}
			
		});
	
	
	}
}
	

