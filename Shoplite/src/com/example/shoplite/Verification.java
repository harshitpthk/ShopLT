package com.example.shoplite;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sholite.R;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.models.User;

public class Verification extends Activity implements ConnectionInterface {

	private String phoneNo = null;
	private EditText phoneView = null;
	private EditText authTokenView = null; 
	private boolean verify = false;
	private boolean resend = false;
	final Verification vf = this;
	private Integer authToken = 0;
	
	private IntentFilter inf = new IntentFilter();
	
	private BroadcastReceiver VerificationSMSReciever = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
	            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
	            SmsMessage[] msgs = null;
	            String msg_from;
	            if (bundle != null){
	                //---retrieve the SMS message received---
	            	String msgBody = null;
	                try{
	                    Object[] pdus = (Object[]) bundle.get("pdus");
	                    msgs = new SmsMessage[pdus.length];
	                    for(int i=0; i<msgs.length; i++){
	                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	                        msg_from = msgs[i].getOriginatingAddress();
	                        msgBody = msgs[i].getMessageBody();
	                    }
	                    StringBuffer sBuffer = new StringBuffer();
	                    Pattern p  = Pattern.compile("-?\\d+");
	                    Matcher m  = p.matcher(msgBody);
	                    while (m.find()) {
	                        sBuffer.append(m.group());
	                    }
	                    Integer code = Integer.parseInt(sBuffer.toString());
	                    displayCodeVerify(code);
	                    Toast.makeText(getApplicationContext(), msgBody, Toast.LENGTH_LONG).show();
	                }catch(Exception e){
                            Log.e("Exception caught",e.getMessage());
	                }
	            }
	        }
		}

		
	};
	private void displayCodeVerify(Integer code) {
		// TODO Auto-generated method stub
		this.authToken = code;
		authTokenView.setText(this.authToken.toString());
		verify(null);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		
		 phoneNo = Globals.dbhelper.getItem("phoneNo");
		 phoneView = (EditText) findViewById(R.id.phone_no_verification);
		 phoneView.setText(phoneNo,TextView.BufferType.EDITABLE);
		 authTokenView = (EditText)findViewById(R.id.verification_input);
		 inf.addAction("android.provider.Telephony.SMS_RECEIVED");
		 registerReceiver(VerificationSMSReciever, inf);
		 String auth_token = Globals.dbhelper.getItem("auth-token");
		 authToken = Integer.parseInt(auth_token);
			
		 		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds products to the action bar if it is present.
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
	@Override
	 protected void onStop() {
	  // TODO Auto-generated method stub
	  unregisterReceiver(VerificationSMSReciever);
	  super.onStop();
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
		ServerConnectionMaker.sendRequest(this);
		
		
	}
	
	public void verify(View view)
	{
		Controls.show_loading_dialog(this, "Verifying");
		verify = true;
		if( ServerConnectionMaker.star_sessionID == null  ){
	    	
			ServerConnectionMaker.star_sessionID = Globals.dbhelper.getItem("JSESSIONID_STAR" );
    	
		}
		ServerConnectionMaker.sendRequest(this);
	}

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
			
		if(verify){
			
			
			serviceProvider.addUser(authToken, new Callback<ArrayList<String>>(){

				@Override
				public void failure(RetrofitError arg0) {
					
					if (arg0.isNetworkError()) {
						Log.e("Retrofit error", "503"); // Use another code if you'd prefer
				    }
					Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					Log.e("Retrofit error", arg0.getUrl());
					Log.e("Retrofit error", arg0.getMessage());
					ServerConnectionMaker.recieveResponse(null);
				}

				@Override
				public void success(ArrayList<String> clientID, Response response) {
					
					Globals.dbhelper.setItem("cliendID", clientID.get(0).toString());
					String email = Globals.dbhelper.getItem("email");
					ServerConnectionMaker.recieveResponse(response);
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
					ServerConnectionMaker.recieveResponse(null);
					
				}

				@Override
				public void success(Integer arg0, Response response) {
					Integer auth_token = arg0;
					Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
					ServerConnectionMaker.recieveResponse(response);
					Globals.dbhelper.setItem("auth-token", auth_token.toString() );
					authToken = auth_token;
				}
			});
		}
		
	}
	

	
}
