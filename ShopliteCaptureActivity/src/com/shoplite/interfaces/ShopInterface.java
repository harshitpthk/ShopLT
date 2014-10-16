package com.shoplite.interfaces;

import java.util.ArrayList;

import com.shoplite.models.Location;
import com.shoplite.models.Shop;

public interface ShopInterface {
	
	public void shop_list_success(Location areaLocation, ArrayList<Shop> shoplist);
	
	public void shop_connected();
	
}
