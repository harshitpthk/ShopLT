package com.homelybuysapp.connection;


import java.io.IOException;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.Utils.util;


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
		NetworkConnectivityManager ncm = new ServerConnectionMaker.NetworkConnectivityManager();
		
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setEndpoint(util.starURL)
		.setClient(new ServerConnectionMaker.ConnectivityAwareUrlClient(new OkClient(), ncm))
		.setRequestInterceptor(requestInterceptor)
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();
//
		if( CaptureActivity.butteryProgressBar != null){
			 CaptureActivity.butteryProgressBar.setVisibility(View.VISIBLE);
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
		if( CaptureActivity.butteryProgressBar != null){
			 CaptureActivity.butteryProgressBar.setVisibility(View.INVISIBLE);
		}
		Controls.dismiss_progress_dialog();
		
	}
	public static class ConnectivityAwareUrlClient implements Client {
	   // Logger log = LoggerFactory.getLogger(ConnectivityAwareUrlClient.class);

	    public ConnectivityAwareUrlClient(Client wrappedClient, NetworkConnectivityManager ncm) {
	        this.wrappedClient = wrappedClient;
	        this.ncm = ncm;
	    }

	    Client wrappedClient;
	    private NetworkConnectivityManager ncm;

	    @Override
	    public Response execute(Request request) throws IOException {
	        if (!ncm.isConnected()) {
	            Log.e("No connectivity %s ", request.toString());
	            Toast.makeText(Globals.ApplicationContext,"No Internet Connection",Toast.LENGTH_LONG).show();
	            try {
					throw new NoConnectivityException("No connectivity");
				} catch (NoConnectivityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
	        }
	       
	        	return wrappedClient.execute(request);
	        
	      }

		/* (non-Javadoc)
		 * @see retrofit.client.Client#execute(retrofit.client.Request)
		 */
		
	}
	private static class NoConnectivityException extends Exception {
        public NoConnectivityException() {
                super();
        }

        public NoConnectivityException(String s) {
                super(s);
        }
}
	private static class NetworkConnectivityManager{
		
		/**
		 * 
		 */
		public boolean isConnected() {
			// TODO Auto-generated constructor stub
			ConnectivityManager cm =
			        (ConnectivityManager)com.homelybuysapp.Utils.Globals.ApplicationContext.getSystemService(com.homelybuysapp.Utils.Globals.ApplicationContext.CONNECTIVITY_SERVICE);
			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null &&
			                      activeNetwork.isConnectedOrConnecting();
			return isConnected;
		}
	}
	
}
