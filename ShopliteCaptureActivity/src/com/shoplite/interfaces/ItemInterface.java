package com.shoplite.interfaces;

import java.util.ArrayList;

import com.shoplite.models.ItemCategory;

public interface ItemInterface {

	
	public void getItemList(ItemCategory item);
	
	public void ItemGetSuccess(ItemCategory item);
	
	public void ItemListGetSuccess(ArrayList<ItemCategory> itemFamily);
}
