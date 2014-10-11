package com.shoplite.hub.test;

import java.net.HttpURLConnection;

import com.google.gson.Gson;


public class GetItemTest implements TestInterface {

	public String servicename="getitem"; 
	public String serviceType = "GET";
	
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
		Gson gson = new Gson();
		gson.toJson("");
		return gson.toString();
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
