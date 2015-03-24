package com.shoplite.models;

import java.util.ArrayList;
import java.util.Comparator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.CategoryInterface;

public class Category implements ConnectionInterface{
	private int id;
	private String name;
	private boolean isPriceUpdateAvailable;
	private ArrayList<Category> childList;
	private CategoryInterface calling_class_object;
	private int rank;
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public ArrayList<Category> getChildList() {
		return childList;
	}
	public void setChildList(ArrayList<Category> childList) {
		this.childList = childList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Category(int id, String name, ArrayList<Category> childList) {
		super();
		this.id = id;
		this.name = name;
		this.childList = childList;
	}
	
	public Category(int id, String name,boolean isPriceUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.isPriceUpdateAvailable=isPriceUpdate;
	}
	/**
	 * 
	 */
	public Category() {
		// TODO Auto-generated constructor stub
	}
	public boolean isPriceUpdateAvailable() {
		return isPriceUpdateAvailable;
	}
	public void setPriceUpdateAvailable(boolean isPriceUpdateAvailable) {
		this.isPriceUpdateAvailable = isPriceUpdateAvailable;
	}
	
	public void getCategories(CategoryInterface callee){
		this.calling_class_object = callee;
		ServerConnectionMaker.sendRequest(this);
	}
	/* (non-Javadoc)
	 * @see com.shoplite.connection.ConnectionInterface#sendRequest(com.shoplite.connection.ServiceProvider)
	 */
	@Override
	public void sendRequest(ServiceProvider sp) {
		// TODO Auto-generated method stub
		final Category thisCategoryObject = this;
		sp.getCategories(new Callback<ArrayList<Category>>(){

			@Override
			public void failure(RetrofitError response) {
				// TODO Auto-generated method stub
				if (response.isNetworkError()) {
					Log.e("Retrofit error", "503"); // Use another code if you'd prefer
			    }
				//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
				Log.e("Retrofit error", response.getUrl());
				Log.e("Retrofit error", response.getMessage());
				ServerConnectionMaker.recieveResponse(null);
			}

			@Override
			public void success(ArrayList<Category> CategoryList, Response response) {
				// TODO Auto-generated method stub
				thisCategoryObject.calling_class_object.getCategorySuccess(CategoryList);
				ServerConnectionMaker.recieveResponse(response);
			}});
	}
	
	public static class CategoryComparator implements Comparator<Category> {
	    @Override
	    public int compare(Category cat1, Category cat2) {
	        return  cat2.getRank()-cat1.getRank();
	    }
	}
}
