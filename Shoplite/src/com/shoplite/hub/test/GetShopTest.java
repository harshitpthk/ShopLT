package com.shoplite.hub.test;

import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.shoplite.models.Location;

public class GetShopTest implements TestInterface{
	public String servicename="getshop"; 
	public String serviceType = "POST";
	
	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return servicename;
	}
	@Override
	public String getMethodType() {
		// TODO Auto-generated method stub
		return serviceType;
	}
	@Override
	public String getPostObject() {
		// TODO Auto-generated method stub
		
		Location loc = new Location(22.222, 7.2222);
		
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(loc));
		return gson.toJson(loc);
		
		
	}
	@Override
	public void writeHeaders(HttpURLConnection conn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void readHeaders(HttpURLConnection conn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String onSuccess(String response) {
		// TODO Auto-generated method stub
		return null;
	}
}
