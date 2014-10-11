package com.shoplite.hub.test;

import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.shoplite.models.RegistrationTokenizer;

public class AddUserTest implements TestInterface{
	
	public AddUserTest(RegistrationTokenizer reg)
	{
		super();
		this.reg =reg;
	}
	
	public RegistrationTokenizer reg;
	public String servicename="adduser"; 
	public String serviceType = "POST";
	public String client_id="";
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
		//System.out.println(gson.toJson(this.reg));
		return gson.toJson(this.reg);
		
		
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
