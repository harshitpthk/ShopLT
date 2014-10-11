package com.shoplite.hub.test;

import java.net.HttpURLConnection;

import android.content.Context;

import com.google.gson.Gson;
import com.shoplite.models.Location;
import com.shoplite.models.User;

public class RegisterUserTest implements TestInterface{
	public String servicename="registeruser"; 
	public String serviceType = "POST";
	public String user_id = "";
	public User user=null;
	public String email = null;
	public String phoneNo = null;
	public String name = null;
	public String dob = null;
	public String lon = null;
	public String lat = null;
	public String response = null;
	
	public RegisterUserTest(){
		
		
		
	}
	public void set_email(String email)
	{
		this.email = email;
		
	}
	
	public void set_phoneNo(String phoneNo)
	{
		this.phoneNo = phoneNo;
	}
	
	public void set_name(String name)
	{
		this.name = name;
	}
	
	public void set_dob(String dob)
	{
		this.dob = dob;
	}
	public void set_location(String lon, String lat)
	{
		this.lon = lon;
		this.lat = lat;
	}
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
		//Location loc = new Location(this.lon, this.lat);
		
		user = new User();
		user.setEmail(this.email);
		user.setPhno(this.phoneNo);
	//	user.setLocation(loc);
		user.setName(this.name);
		user.setDob(this.dob);
		user_id = user.getEmail()+"-"+user.getPhno();
		Gson gson = new Gson();
		System.out.println(gson.toJson(user.getEmail()));
		return gson.toJson(user);
		
		
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
	public String onSuccess(String response)
	{
		this.response = response;
		return this.response;
		
		
	}
}
