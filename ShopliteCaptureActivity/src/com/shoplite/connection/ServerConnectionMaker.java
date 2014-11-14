package com.shoplite.connection;


import java.util.List;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.util;
import android.util.Log;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.Response;


public class ServerConnectionMaker  {
	
	
	
	public static String star_sessionID = null;
	public static String userSessionID = null;

	public  static ServiceProvider serviceProvider = null;

	 
	
	public static  void sendRequest(ConnectionInterface ci){
			
		//adding of custom headers in the service provider
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			  

			@Override
			  public void intercept(RequestFacade request) {
			    request.addHeader("Access-Control-Allow-Star", "shoplite");
			    request.addHeader("Cookie", star_sessionID);
			    			   
			  }
			  
			  
			};
				
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setEndpoint(util.starURL)
		.setRequestInterceptor(requestInterceptor)
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();
		
		if(CaptureActivity.decorView != null && !CaptureActivity.isProgressBarAdded){
			CaptureActivity.isProgressBarAdded = true;
			CaptureActivity.decorView.addView(CaptureActivity.progressBar);
		}
		serviceProvider = restAdapter.create(ServiceProvider.class);
		
		ci.sendRequest(serviceProvider);
		
	}
	
	public static void recieveResponse(Response response)
	{
		
		if(response != null){
			List<Header> headers  = response.getHeaders();
			for(Header header : headers){
				if("set-cookie".equalsIgnoreCase(header.getName())){
					if(header.getValue().contains("JSESSIONID="))
						star_sessionID = header.getValue();
						Log.e("star session ID",star_sessionID);
						
						//add_sessionID_request.addHeader("Access-Control-Allow-Star", "shoplite");
						
						break;
				}
				else if(util.session_user_header.equalsIgnoreCase(header.getName())){
					userSessionID = header.getValue();
				}
			}
		}
		//progress dialog removal
		if(CaptureActivity.decorView != null ){
			CaptureActivity.isProgressBarAdded = false;
			CaptureActivity.decorView.removeView(CaptureActivity.progressBar);
		}
		Controls.dismiss_progress_dialog();
		
	}
	

	
}
