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

import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.interfaces.LocationInterface;

public class HomelyBuysLocation  {
	public static Activity mActivity;
	public Intent mIntent;
	public static Location  current_location = null;
	public static boolean hasLocation = false;
	

	private static int location_listened_count = 0;
	private final static LocationManager lm = (LocationManager) Globals.ApplicationContext.getSystemService(Context.LOCATION_SERVICE);
	private static LocationInterface calling_class_object;
	
	public static boolean isHasLocation() {
		return hasLocation;
	}

	public static void setHasLocation(boolean hasLocation) {
		HomelyBuysLocation.hasLocation = hasLocation;
	}

	
	private static LocationListener gpsLocationListener = new LocationListener() {
		
		   

		

		public void onLocationChanged(Location mlocation) {
	      // Called when a new HomelyBuysLocation is found by the network HomelyBuysLocation provider.
	       
	    	HomelyBuysLocation.current_location = mlocation;
	    	Globals.current_location = new com.homelybuysapp.models.Location(mlocation.getLatitude(),mlocation.getLongitude());
	    	Log.v("Latitude && Longitude",Double.toString(mlocation.getLatitude())+"&&"+Double.toString(mlocation.getLongitude()));
	    	if(!hasLocation)
	    		calling_class_object.make_use_of_location();
	    	
	    	if(location_listened_count > 4){
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
	
	private static LocationListener networkLocationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location mlocation) {
			// TODO Auto-generated method stub
			HomelyBuysLocation.current_location = mlocation;
	    	Globals.current_location = new com.homelybuysapp.models.Location(mlocation.getLatitude(),mlocation.getLongitude());
	    	Log.v("Latitude && Longitude",Double.toString(mlocation.getLatitude())+"&&"+Double.toString(mlocation.getLongitude()));
	    	if(!hasLocation)
	    		calling_class_object.make_use_of_location();
	    	
	    	if(location_listened_count > 4){
	    		lm.removeUpdates(this);
	    	}
	    	location_listened_count++;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		
	};
	
	
	public static void getLocation(final LocationInterface calling_class_object,Activity activity) {
			
		mActivity= activity;
		HomelyBuysLocation.calling_class_object = calling_class_object;
		
		Controls.show_loading_dialog(activity, "Waiting for Location...");

		try {
			boolean is_network_enabled= false;
			boolean is_gps_enabled = false;
			
			is_gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			is_network_enabled  = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			 
			
			 if(is_network_enabled){
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
				Log.v("network HomelyBuysLocation is enabled", Boolean.toString(is_gps_enabled) );
			    HomelyBuysLocation.current_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			    
				if(!hasLocation && HomelyBuysLocation.current_location !=null ){
					Globals.current_location = new com.homelybuysapp.models.Location(HomelyBuysLocation.current_location.getLatitude(),HomelyBuysLocation.current_location.getLongitude());
					calling_class_object.make_use_of_location();
				}
			}
			 else if (is_gps_enabled){
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
					Log.v("network HomelyBuysLocation is enabled", Boolean.toString(is_gps_enabled) );
				    HomelyBuysLocation.current_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				    
					if(!hasLocation && HomelyBuysLocation.current_location !=null ){
						Globals.current_location = new com.homelybuysapp.models.Location(HomelyBuysLocation.current_location.getLatitude(),HomelyBuysLocation.current_location.getLongitude());
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
            	Log.e("HomelyBuysLocation Error", ste.toString());
            }
           
        }
		
		
		
    }
	
	public static void removeLocationListener(){
		if(lm != null){
			lm.removeUpdates(gpsLocationListener);
		}
		
	}
	
	
	private final static Handler handler = new Handler() {
	     public void handleMessage(Message msg) {
	          if(msg.arg1 == 1){
	        	
	               if (!mActivity.isFinishing()) { // Without this in certain cases application will show ANR
	                    AlertDialog.Builder builder = new AlertDialog.Builder( mActivity );
	                    builder.setMessage("Your Location Service is disabled! Enable it to start shopping").setCancelable(false).setPositiveButton("Enable Location/GPS", new DialogInterface.OnClickListener() {
	                         public void onClick(DialogInterface dialog, int id) {
	                              Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                              mActivity.startActivityForResult(gpsOptionsIntent, 1);
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
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsLocationListener);
				
			}
		}
	}

	
	
}
