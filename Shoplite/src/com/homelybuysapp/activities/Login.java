package com.homelybuysapp.activities;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.homelybuysapp.Utils.util;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.LoginInterface;

public class Login implements ConnectionInterface {
	private String email = null;
	private String authCode = null;
	
	private LoginInterface callee;
	
	public  void generateAuthCode(String key) {
		// TODO Auto-generated method stub
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		long time = calendar.getTimeInMillis();
		int factor = (int)time/(63000);
		String seed = util.generateSeed((long)factor,8);
		String authKey = key+seed;
		String authcode="";
		try {
			authcode = util.encrypt(authKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("time "+ time);
		System.out.println("factor "+ factor);
		System.out.println("key "+ key);
		System.out.println("seed "+ seed);
		System.out.println("authcode "+ authcode);
		this.authCode =  authcode;
	}
	public  void login(String key,String email,LoginInterface callee){
		//Controls.show_loading_dialog(context, "Logging In");
		generateAuthCode(key);
		this.email = email;
		this.callee = callee;
		ServerConnectionMaker.sendRequest(this);
	}

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		// TODO Auto-generated method stub
		final Login thisLoginObj = this;
		serviceProvider.login(this.authCode,this.email, new Callback<String>(){

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				if (arg0.isNetworkError()) {
					Log.e("Retrofit error", "503"); // Use another code if you'd prefer
			    }
				//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
				//Log.e("Retrofit error", arg0.getUrl());
				//Log.e("Retrofit error", arg0.getMessage());
				ServerConnectionMaker.recieveResponse(null);
				thisLoginObj.callee.loginFailure();
			}

			@Override
			public void success(String message, Response response) {
				// TODO Auto-generated method stub
				Log.e("Retrofit Success", message);
				
				
				ServerConnectionMaker.recieveResponse(response);

				// code lines to initiate scanner activity
				thisLoginObj.callee.loginSuccess();
				
				
			    
			}});
	}
	
}
