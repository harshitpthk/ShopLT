package com.shoplite.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

import com.shoplite.connection.ServerConnection;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;

public class CartGlobals {
	public static ArrayList<OrderItemDetail> cartList = new ArrayList<OrderItemDetail>();
	
	
	public static LinkedList <PackList> CartServerRequestQueue = new LinkedList<PackList>();
	
	public static PackList packingList ;
	
	public static int sentItemCount = 0;
	
	public static void incSetItemCount(){
		CartGlobals.sentItemCount++;
	}
	public static void decSetItemCount(){
		if(CartGlobals.sentItemCount > 0)
			CartGlobals.sentItemCount--;
		
	}

}
