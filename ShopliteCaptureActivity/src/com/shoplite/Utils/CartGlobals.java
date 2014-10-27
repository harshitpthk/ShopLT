package com.shoplite.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

import com.shoplite.UI.BaseItemCard;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;

public class CartGlobals {
	public static ArrayList<OrderItemDetail> cartList = new ArrayList<OrderItemDetail>();
	
	
	public static LinkedList <PackList> CartServerRequestQueue = new LinkedList<PackList>();
	
	public static ArrayList<BaseItemCard> recentDeletedItems = new ArrayList<BaseItemCard>();
		

}
