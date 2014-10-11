package com.example.shoplite;

import java.net.HttpURLConnection;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sholite.R;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.location;
import com.shoplite.Utils.util;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnection;
import com.shoplite.connection.ServiceProvider;

import eu.livotov.zxscan.ZXScanHelper;

public class Login implements ConnectionInterface {
	private String email = null;
	private String authCode = null;
	private Context mContext = null;

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
	public  void login(String key,String email,Context context){
		Controls.show_loading_dialog(context, "Logging In");
		generateAuthCode(key);
		this.email = email;
		this.mContext = context;
		ServerConnection.sendRequest(this,util.starURL);
	}

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		// TODO Auto-generated method stub
		serviceProvider.login(this.authCode,this.email, new Callback<String>(){

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				if (arg0.isNetworkError()) {
					Log.e("Retrofit error", "503"); // Use another code if you'd prefer
			    }
				//Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
				Log.e("Retrofit error", arg0.getUrl());
				Log.e("Retrofit error", arg0.getMessage());
				ServerConnection.recieveResponse(null);
			}

			@Override
			public void success(String message, Response response) {
				// TODO Auto-generated method stub
				Log.e("Retrofit Success", message);
				
				
				ServerConnection.recieveResponse(response);

				// code lines to initiate scanner activity
				
				Activity act = (Activity) mContext;
				act.finish();
			    ZXScanHelper.setCustomScanSound(R.raw.atone);
			    ZXScanHelper.scan(act,0);
			    
			}});
	}
}
