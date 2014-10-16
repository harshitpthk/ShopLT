package com.shoplite.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoplite.database.DbHelper;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.Shop;

import android.content.Context;


public class Globals {
	public static Context ApplicationContext;
	public static DbHelper dbhelper ;
	public static com.shoplite.models.Location current_location = null;				//users current location
	
	
	public static boolean shop_list_fetch_success = false;							// boolean to know whether shops were fetched
	public static ArrayList<Shop> shop_list = null;									// array  to store shoplist
	public static ArrayList<Shop> shop_list_near = new ArrayList<Shop>();
	public static HashMap<Double,Shop> near_shop_distance_matrix = new HashMap<Double,Shop>();
	
	public static String shop_to_connect;											// URL storing which shop to connect after measuring least distance
	public static String connect_to_shop_name = null;								// keeps the name
	public static String connect_to_shop_url = null; 								// keeps the url of shop which is to be connected
	public static com.shoplite.models.Location connect_to_shop_location = null;
	
	public static boolean connected_to_shop_success = false;						// boolean to know whether the user is connected to shop or not used in re-listening of location in the location class
	public static String connected_shop_name = null;								// keeps the name which is to be connected
	public static String connected_shop_url = null; 								// keeps the url of shop which is connected
	public static com.shoplite.models.Location connected_shop_location = null;		// keeps the location of connected 

	
	public static ItemCategory fetched_item_category;
	public static ArrayList<ItemCategory> simmilar_item_list = new ArrayList<ItemCategory>();
	public static ArrayList<ItemCategory> item_order_list = new ArrayList<ItemCategory>();
	
	
	
	public static Shop min_sd_matrix() {
		 List distances = new ArrayList(near_shop_distance_matrix.keySet());
		 Collections.sort(distances);
		 
		 return near_shop_distance_matrix.get(distances.get(0));
		
		
	}
	
	public static void add_to_sd_matrix(Shop shpObject ,double lat, double lng) {
		Globals.shop_list_near.add(shpObject);
		double distance = Math.sqrt(Math.pow((Globals.current_location.getLatitude()-lat), 2) + Math.pow((Globals.current_location.getLongitude()-lng),2));
		Globals.near_shop_distance_matrix.put(distance,shpObject);
		
	}
	public static Shop get_shop_from_location(LatLng loc) {
		if(Globals.shop_list != null){
			for(int i = 0 ; i < Globals.shop_list.size(); i++){
				Shop shopObject = Globals.shop_list.get(i);
				double lat = shopObject.getLocation().getLatitude();
				double lng = shopObject.getLocation().getLongitude();
				LatLng coordinate = new LatLng(lat, lng);

				if(coordinate.equals(loc)){
					return shopObject;
				}
			}
			return null;
		}
		else{
			return null;
		}
	}
	public static int find_item_index_from_title(String name) {
		// TODO Auto-generated method stub
		int remove_index = -1;
		for(int i = 0 ; i < Globals.item_order_list.size();i++){
			if(Globals.item_order_list.get(i).getName().equals(name)){
				remove_index = i;
				break;
			}
			
		}
		return remove_index;
		
	}
}
