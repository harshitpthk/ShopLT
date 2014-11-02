package com.shoplite.UI;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
	public static ArrayList<Marker> markerList = new ArrayList<Marker>();
	
	
}
