package com.homelybuysapp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.homelybuysapp.interfaces.LocationInterface;

public class location  {
	public Activity mActivity;
	public Intent mIntent;
	public static Location  current_location = null;
	public static boolean made_use_of_location = false;
	private int location_listened_count = 0;
	private final static LocationManager lm = (LocationManager) Globals.ApplicationContext.getSystemService(Context.LOCATION_SERVICE);
	private static LocationListener locationListener;
	
	
	public void getLocation(final LocationInterface calling_class_object,Activity activity) {
			
		mActivity= activity;
		
		try {
			
			boolean network_enabled = false;
			
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			
			 locationListener = new LocationListener() {
				
			   

				public void onLocationChanged(Location mlocation) {
			      // Called when a new location is found by the network location provider.
			       
			    	location.current_location = mlocation;
			    	Globals.current_location = new com.homelybuysapp.models.Location(mlocation.getLatitude(),mlocation.getLongitude());
			    	Log.v("Latitude && Longitude",Double.toString(mlocation.getLatitude())+"&&"+Double.toString(mlocation.getLongitude()));
			    	if(!made_use_of_location)
			    		calling_class_object.make_use_of_location();
			    	
			    	if(location_listened_count > 10){
			    		lm.removeUpdates(this);
			    	}
			    	location_listened_count++;
			    	
				}

				@Override
				public void onProviderDisabled(String arg0) {
					
					Message msg = handler.obtainMessage();
				    msg.arg1 = 1;
				    handler.sendMessage(msg);
				}

				@Override
				public void onProviderEnabled(String arg0) {
					
				}

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
					
					
				}
			    	
			    		
			  };
				
			if (network_enabled){
					lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
					Log.v("network location is enabled", Boolean.toString(network_enabled) );
				    
					//android.os.Debug.waitForDebugger();
					location.current_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if(!made_use_of_location && location.current_location !=null ){
						Globals.current_location = new com.homelybuysapp.models.Location(location.current_location.getLatitude(),location.current_location.getLongitude());
					
			    		calling_class_object.make_use_of_location();
					}
			    	
			}
			else{
				Message msg = handler.obtainMessage();
			    msg.arg1 = 1;
			    handler.sendMessage(msg);
				
			}
			  
			
					
				
        } catch (Exception e) {
            e.printStackTrace();
            for(StackTraceElement ste : e.getStackTrace()){
            	Log.e("Location Error", ste.toString());
            }
           
        }
		
		
		
    }
	
	public static void removeLocationListener(){
		if(lm != null){
			lm.removeUpdates(locationListener);
		}
		
	}
	private final Handler handler = new Handler() {
	     public void handleMessage(Message msg) {
	          if(msg.arg1 == 1){
	        	
	               if (!mActivity.isFinishing()) { // Without this in certain cases application will show ANR
	                    AlertDialog.Builder builder = new AlertDialog.Builder( mActivity );
	                    builder.setMessage("Your Location Service is disabled! Would you like to enable it?").setCancelable(false).setPositiveButton("Enable Location/GPS", new DialogInterface.OnClickListener() {
	                         public void onClick(DialogInterface dialog, int id) {
	                              Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                              mActivity.startActivityForResult(gpsOptionsIntent, 1);
	                          }
	                     });
	                     builder.setNegativeButton("Do nothing", new DialogInterface.OnClickListener() {
	                          public void onClick(DialogInterface dialog, int id) {
	                               dialog.cancel();
	                          }
	                     });
	                     AlertDialog alert = builder.create();
	                     alert.show();
	                }
	          	}

	       }
	};
	public void onActivityResult(int requestCode)
	{
		if(requestCode == 1){
			if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				
			}
		}
	}

	
	
}
