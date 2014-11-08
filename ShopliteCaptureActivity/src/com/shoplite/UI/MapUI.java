package com.shoplite.UI;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;



public class MapUI  extends SupportMapFragment{
	public static GoogleMap mMap;									//google map
	public static SupportMapFragment mMapFragment;				//fragment storing google map
	public static boolean mapVisible = true;						// initial visibility set to false of the map
	public static MapController  mMapController;
	public static MapView mMapView;
	public static void move_map_camera(LatLng coordinate) {
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
	    MapUI.mMap.moveCamera(yourLocation);
	}
	public static void zoomInDeliveryLocation(LatLng coordinate)
	{
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
	    MapUI.mMap.moveCamera(yourLocation);
	}
	public static void zoomOutDeliveryLocation(LatLng coordinate,final GoogleMap.CancelableCallback callback)
	{
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
	    MapUI.mMap.animateCamera(yourLocation, new CancelableCallback(){

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				callback.onFinish();
			}
	    	
	    });
	    
	}
	public static ArrayList<Marker> markerList = new ArrayList<Marker>();
	
	
}
