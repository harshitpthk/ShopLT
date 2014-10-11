package com.example.shoplite;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.shoplite.UI.Controls;
import com.example.sholite.R;
import com.example.shoplite.Login;
import com.google.gson.JsonObject;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.util;
import com.shoplite.connection.ServerConnection;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.database.DbHelper;
import com.shoplite.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Verification extends Activity implements ConnectionInterface {

	private String phoneNo = null;
	private EditText phoneView = null;
	private boolean verify = false;
	private boolean resend = false;
	final Verification vf = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		
		 phoneNo = Globals.dbhelper.getItem("phoneNo");
		 phoneView = (EditText) findViewById(R.id.phone_no_verification);
		 phoneView.setText(phoneNo,TextView.BufferType.EDITABLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verification, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void signup_again(View view)
	{
		Globals.dbhelper.setItem("auth-token",null);
		Globals.dbhelper.setItem("name",null);
		Globals.dbhelper.setItem("phoneNo",null);
		Globals.dbhelper.setItem("email",null);
		Globals.dbhelper.setItem("cliendID",null);
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void resend(View view)
	{
		Controls.show_loading_dialog(this, "Resending");
		resend = true;
		ServerConnection.sendRequest(this,util.starURL);
		
	}
	
	public void verify(View view)
	{
		Controls.show_loading_dialog(this, "Verifying");
		verify = true;
		if( ServerConnection.star_sessionID == null  ){
	    	
			ServerConnection.star_sessionID = Globals.dbhelper.getItem("JSESSIONID_STAR" );
    	
		}
		ServerConnection.sendRequest(this,util.starURL);
	}

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
			
		if(verify){
			
			String auth_token = Globals.dbhelper.getItem("auth-token");
			Integer auth_token_int = Integer.parseInt(auth_token);
			
			serviceProvider.addUser(auth_token_int, new Callback<ArrayList<String>>(){

				@Override
				public void failure(RetrofitError arg0) {
					
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					ServerConnection.recieveResponse(null);
				}

				@Override
				public void success(ArrayList<String> clientID, Response response) {
					
					Globals.dbhelper.setItem("cliendID", clientID.get(0).toString());
					String email = Globals.dbhelper.getItem("email");
					ServerConnection.recieveResponse(response);
					
					
					Toast.makeText(getBaseContext(), clientID.get(0).toString(), Toast.LENGTH_LONG).show();
					Login ln = new Login();
					ln.login(clientID.get(0).toString(), email ,vf);
					
				}
			});
		}
		
		else if(resend){
			User user = new User();
			String phoneNo = Globals.dbhelper.getItem("phoneNo");
        	String email = Globals.dbhelper.getItem("email");
        	String name = Globals.dbhelper.getItem("name");
			user.setEmail(email);
			user.setPhno(phoneNo);
			//user.setLocation(loc);
			user.setName(name);
			
			
			serviceProvider.signup(user, new Callback<Integer>(){

				@Override
				public void failure(RetrofitError arg0) {
					
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					ServerConnection.recieveResponse(null);
					
				}

				@Override
				public void success(Integer arg0, Response response) {
					
					
					Integer auth_token = arg0;
					Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					ServerConnection.recieveResponse(response);
					Globals.dbhelper.setItem("auth-token", auth_token.toString() );
										
				}
			});
		}
		
	}
}
