package com.shoplite.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.shoplite.UI.BaseItemCard;
import com.shoplite.database.DbHelper;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.Shop;


public class Globals {
	public static Context ApplicationContext = null;								//Application Context for use in the non-activity classes
	public static DbHelper dbhelper = null ;										//DB Helper object to read write to DB.
	public static com.shoplite.models.Location current_location = null;				//users current location
	public static com.shoplite.models.Location delivery_location= null;
	public static ArrayList<Shop> shop_list = new ArrayList<Shop>();				// array  to store shop list
	public static HashMap<Double,Shop> near_shop_distance_matrix = new HashMap<Double,Shop>();
		
	public static boolean connected_to_shop_success = false;						// boolean to know whether the user is connected to shop or not used in re-listening of location in the location class
	public static int connected_shop_id = 0;										// stores the connected shop ID
	public static String connected_shop_name = null;								// keeps the name which is to be connected
	public static String connected_shop_url = null; 								// keeps the url of shop which is connected
	public static com.shoplite.models.Location connected_shop_location = null;		// keeps the location of connected 

	public static ItemCategory fetched_item_category;
	public static ArrayList<ItemCategory> simmilar_item_list = new ArrayList<ItemCategory>();
	public static ArrayList<ItemCategory> item_order_list = new ArrayList<ItemCategory>();
	public static Double cartTotalPrice = 0.00;
	
	public static boolean isInsideShop;
	
	//Method to Return Shop object which is at minimum distance from current location inside the 200 meters radius
	public static Shop min_sd_matrix() {
		 List distances = new ArrayList(near_shop_distance_matrix.keySet());
		 Collections.sort(distances);
		 
		 return near_shop_distance_matrix.get(distances.get(0));
		
		
	}
	//Method to add shop Object and its location to shop-distance Matrix 
	public static void add_to_sd_matrix(Shop shpObject ,double lat, double lng) {
		
		double distance = Math.sqrt(Math.pow((Globals.current_location.getLatitude()-lat), 2) + Math.pow((Globals.current_location.getLongitude()-lng),2));
		Globals.near_shop_distance_matrix.put(distance,shpObject);
		
	}
	
	//Google map marker method to return Shop Object when inputed with Location
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
	
}
