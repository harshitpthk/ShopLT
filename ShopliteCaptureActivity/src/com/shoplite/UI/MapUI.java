package com.shoplite.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
	
	class MapOverlay extends com.google.android.maps.Overlay
	{
	    public boolean draw(Canvas canvas, MapView mapView, 
	    boolean shadow, long when) 
	    {
	        super.draw(canvas, mapView, shadow);                   

	        //---translate the GeoPoint to screen pixels---
	        Point screenPts = new Point();
	        mapView.getProjection().toPixels(p, screenPts);

	        //---add the marker---
	        Bitmap bmp = BitmapFactory.decodeResource(
	            getResources(), R.drawable.pink);            
	        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-32, null);         
	        return true;
	    }
	} 
}
