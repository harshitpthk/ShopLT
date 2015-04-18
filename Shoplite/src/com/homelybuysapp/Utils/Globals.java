package com.homelybuysapp.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.homelybuysapp.database.DbHelper;
import com.homelybuysapp.fragments.CartFragment;
import com.homelybuysapp.models.Address;
import com.homelybuysapp.models.Location;
import com.homelybuysapp.models.Product;
import com.homelybuysapp.models.Shop;


public class Globals {
	public static Context ApplicationContext = null;								//Application Context for use in the non-activity classes
	public static DbHelper dbhelper = null ;										//DB Helper object to read write to DB.
	public static com.homelybuysapp.models.Location current_location = null;				//users current HomelyBuysLocation
//	public static com.homelybuysapp.models.Location delivery_location= null;				//delivery HomelyBuysLocation
//	public static String delivery_address;
	public static Address deliveryAddress = new Address(); 
	public static ArrayList<Shop> shop_list = new ArrayList<Shop>();				// array  to store shop list
	public static HashMap<Double,Shop> near_shop_distance_matrix = new HashMap<Double,Shop>();
		
	public static boolean connected_to_shop_success = false;						// boolean to know whether the user is connected to shop or not used in re-listening of HomelyBuysLocation in the HomelyBuysLocation class
	public static Shop connectedShop;
	
	public static Product fetched_item_category;
	public static ArrayList<Product> simmilar_item_list = new ArrayList<Product>();
	public static ArrayList<Product> item_order_list = new ArrayList<Product>();
	public static ArrayList<Integer> item_added_list = new ArrayList<Integer>();	//keeps track of items added
	
	public static Double cartTotalPrice = 0.00;
	public static String PREFS_NAME = "Dictionary";
	public static String PREFS_KEY = "LastAddress";
	public static boolean isInsideShop;
	public static boolean usedPreviousAddress = true;
	
	//Method to Return Shop object which is at minimum distance from current HomelyBuysLocation inside the 200 meters radius
	public static Shop min_sd_matrix() {
		 List distances = new ArrayList(near_shop_distance_matrix.keySet());
		 Collections.sort(distances);
		 
		 return near_shop_distance_matrix.get(distances.get(0));
		
		
	}
	//Method to add shop Object and its HomelyBuysLocation to shop-distance Matrix 
	public static void add_to_sd_matrix(Shop shpObject ,double lat, double lng, Location location) {
		
		double distance = Math.sqrt(Math.pow((location.getLatitude()-lat), 2) + Math.pow((location.getLongitude()-lng),2));
		Globals.near_shop_distance_matrix.put(distance,shpObject);
		
	}
	
	//Google map marker method to return Shop Object when inputed with HomelyBuysLocation
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
	/**
	 * 
	 */
	public static void resetCartData() {
		
		item_added_list.clear();
		item_order_list.clear();
		cartTotalPrice = 0.00;
		CartFragment.updateCart();
	}
	
}
