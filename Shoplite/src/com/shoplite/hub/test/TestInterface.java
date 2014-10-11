package com.shoplite.hub.test;

import java.net.HttpURLConnection;

public interface TestInterface {

	public String getServiceName();
	public String getMethodType();
	public String getPostObject();
	public void writeHeaders(HttpURLConnection conn);
	public void readHeaders(HttpURLConnection conn);
	public String onSuccess(String response);
		
}
