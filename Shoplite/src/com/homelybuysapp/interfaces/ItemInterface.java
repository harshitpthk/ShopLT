package com.homelybuysapp.interfaces;

import java.util.ArrayList;

import com.homelybuysapp.models.Input;
import com.homelybuysapp.models.Product;

public interface ItemInterface {

	
	public void getItemList(Product item);
	
	public void ItemGetSuccess(Product item);
	
	public void ItemGetFailure();
	
	public void ItemListGetSuccess(ArrayList<Product> itemFamily);
	
	public void getItem();
	
	public void updateItemSuccess(Product product);
	
	public void updateItemFailure();

	/**
	 * 
	 */
	public void productsGetFailure();

	/**
	 * @param productList 
	 * 
	 */
	public void productsGetSuccess(ArrayList<Product> productList);
	public void getProducts(Input input);

	/**
	 * 
	 */
	public void searchProductFailure();

	/**
	 * @param productList
	 */
	public void productSearchSuccess(ArrayList<Product> productList);
}
