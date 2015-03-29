package com.homelybuysapp.interfaces;

import java.util.ArrayList;

import com.homelybuysapp.models.Location;
import com.homelybuysapp.models.Shop;

public interface ShopInterface {
	
	public void shop_list_success(Location areaLocation, ArrayList<Shop> shoplist);
	
	public void shop_connected();
	
}
