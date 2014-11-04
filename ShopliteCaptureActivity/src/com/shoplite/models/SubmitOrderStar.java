/**
 * 
 */
package com.shoplite.models;

import org.apache.http.conn.params.ConnConnectionPNames;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.SubmitOrderInterface;

/**
 * @author I300291
 *
 */
public class SubmitOrderStar implements ConnectionInterface {

	int orderId;

	int shopId;

	private SubmitOrderInterface calling_class_object;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	
	public void submitOrderToStar(SubmitOrderInterface callee,int orderID,int shopId)
	{
		this.calling_class_object = callee;
		this.orderId = orderID;
		this.shopId = shopId;
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

	/* (non-Javadoc)
	 * @see com.shoplite.connection.ConnectionInterface#sendRequest(com.shoplite.connection.ServiceProvider)
	 */
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		// TODO Auto-generated method stub

		final SubmitOrderStar submitOrderStar = this;
		final SubmitOrderInterface callee =  submitOrderStar.calling_class_object;
		submitOrderStar.calling_class_object = null;
		serviceProvider.createOrderStar(submitOrderStar, new Callback<String>(){
			
			@Override
			public void failure(RetrofitError response) {
				
				if (response.isNetworkError()) {
					Log.e("SubmitOrderPlanet error", "503"); 
			    }
				//Log.e("SubmitOrderPlanet error", response.getUrl());
				//Log.e("SubmitOrderPlanet error", response.getMessage());
				//Log.e("SubmitOrderPlanet error", response.getStackTrace().toString());
				ServerConnectionMaker.recieveResponse(null);
				
					
			}

			@Override
			public void success(String message, Response response) {
				
				ServerConnectionMaker.recieveResponse(response);
				Log.e("SubmitOrderSuccess", response.toString());
				callee.submitToStarSuccess();
				
				
			}
			
		});
	
	}
}
