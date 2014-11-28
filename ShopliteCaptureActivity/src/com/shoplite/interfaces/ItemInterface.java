package com.shoplite.interfaces;

import java.util.ArrayList;

import com.shoplite.models.Product;

public interface ItemInterface {

	
	public void getItemList(Product item);
	
	public void ItemGetSuccess(Product item);
	
	public void ItemGetFailure();
	
	public void ItemListGetSuccess(ArrayList<Product> itemFamily);
	
	public void getItem();
	
	public void updateItemSuccess(Product product);
	
	public void updateItemFailure();
}
