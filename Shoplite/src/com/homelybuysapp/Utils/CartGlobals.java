package com.homelybuysapp.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackList;
import com.homelybuysapp.models.Product;

public class CartGlobals {
	public static ArrayList<OrderItemDetail> cartList = new ArrayList<OrderItemDetail>();
	
	
	public static LinkedList <PackList> CartServerRequestQueue = new LinkedList<PackList>();
	
	public static ArrayList<Product> recentDeletedItems = new ArrayList<Product>();
		

}
