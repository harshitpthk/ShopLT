package com.shoplite.UI;

import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.shoplite.Utils.Globals;



public class MapUI  extends SupportMapFragment{
	public static GoogleMap mMap;									//google map
	public static SupportMapFragment mMapFragment;				//fragment storing google map
	public static boolean mapVisible = true;						// initial visibility set to false of the map
	
	public static void move_map_camera(LatLng coordinate) {
		// TODO Auto-generated method stub
		
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
	    MapUI.mMap.moveCamera(yourLocation);
	}
	public static void search_location(String search_query){
		
	}
	
}
