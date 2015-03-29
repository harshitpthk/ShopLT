package com.homelybuysapp.activities;

import io.fabric.sdk.android.Fabric;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.example.sholite.R;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.database.DbHelper;
import com.homelybuysapp.interfaces.LoginInterface;
import com.homelybuysapp.models.User;

public class MainActivity extends ActionBarActivity implements ConnectionInterface,LoginInterface  {


	LayoutInflater inflater;
	ViewGroup container;
	
	//signup buttons
	private String mEmail;
	private String mPhoneNo;
	private String mName;
	
	// UI references.
	private EditText mEmailView;
	private EditText mNameView;
	private EditText mPhoneNoView ;
	static final int PICK_DELIVERY_REQUEST = 1;
	private ProgressBar splashProgress;
	
	 private void setGlobals() {
		 Globals.ApplicationContext = this.getApplicationContext();
		 Globals.dbhelper = new DbHelper(Globals.ApplicationContext);
	 }
	 
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
        setGlobals();
        String auth_token = Globals.dbhelper.getItem("auth-token");
        String ClientID = Globals.dbhelper.getItem("cliendID");
         
       if(ClientID != null){
    	   setContentView(R.layout.splash_screen);
    	   splashProgress = (ProgressBar)findViewById(R.id.splash_progress_bar);
    	      
    	   String email = Globals.dbhelper.getItem("email");
    	   Login ln = new Login();
    	   ln.login(ClientID.toString(), email ,this);
       }
       else if(auth_token != null){

    	    setContentView(R.layout.splash_screen);
    	    splashProgress = (ProgressBar)findViewById(R.id.splash_progress_bar);
    	      
        	String phoneNo = Globals.dbhelper.getItem("phoneNo");
        	String email = Globals.dbhelper.getItem("email");
        	Bundle user_details = new Bundle();
        	user_details.putString("phoneNo",phoneNo );
        	user_details.putString("email", email);
        	
        	Intent i = new Intent(this, Verification.class);
        	i.putExtras(user_details);
        	startActivity(i);
        	this.finish();
        	
        }
       else{
    	   setContentView(R.layout.activity_main);
    	   
       }
        
       
    	
    }
    
   

	public void start_signup(View v)
    {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        mEmail = possibleEmail;
		    }
		}
    	
    	 setContentView(R.layout.activity_signup_main);
    	 
    	 
 		// Set up the login form.
 		mEmailView = (EditText) findViewById(R.id.email);
 		mEmailView.setText(mEmail);
 		
 		mPhoneNoView = (EditText) findViewById(R.id.phone);
 		mPhoneNoView.setText(mPhoneNo);
 		
 		mNameView = (EditText) findViewById(R.id.name);
 		mNameView.setText(mName);

 		
 		findViewById(R.id.sign_up_button).setOnClickListener(
 				new View.OnClickListener() {
 					@Override
 					public void onClick(View view) {
 						attemptLogin();
 						
 					}
 				});
    }
   

	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   
		if (requestCode == PICK_DELIVERY_REQUEST) {
		      if (resultCode == RESULT_OK) {
//		    	  finish();
//		    	  ZXScanHelper.setCustomScanSound(R.raw.atone);
//				  ZXScanHelper.scan(this,0);
		         
		      } else if (resultCode == RESULT_CANCELED) {
		         
		      }
		}
		   
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        return true;
    }
    
    public void attemptLogin() {
		
		
		
		//hide keyboard
		InputMethodManager inputManager = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
        InputMethodManager.HIDE_NOT_ALWAYS);
		
		
		// Reset errors.
		mEmailView.setError(null);
		mPhoneNoView.setError(null);
		mNameView.setError(null);
		
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPhoneNo= mPhoneNoView.getText().toString();
		mName = mNameView.getText().toString();
		
		mName = mName.trim();
		mPhoneNo = mPhoneNo.trim();
		mEmail = mEmail.trim();
		
		mNameView.setText(mName);
		mPhoneNoView.setText(mPhoneNo);
		mNameView.setText(mName);
		
		
		
		boolean cancel = false;
		View focusViewEmail = null;
		View focusViewPhoneNo = null;
		View focusViewName = null;

		// Check for a valid email address, phone-number & name.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusViewEmail = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@") ) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusViewEmail = mEmailView;
			cancel = true;
		}
		if (TextUtils.isEmpty(mPhoneNo)) {
			mPhoneNoView.setError(getString(R.string.error_field_required));
			focusViewPhoneNo = mPhoneNoView;
			cancel = true;
		} else if (!mPhoneNo.matches("^[0-9]{10}$") ) {
			mPhoneNoView.setError(getString(R.string.error_invalid_phoneNo));
			focusViewPhoneNo = mPhoneNoView;
			cancel = true;
		}
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusViewName = mNameView;
			cancel = true;
		} else if (!mName.matches("^[a-zA-z][a-zA-Z. ]+$")) {
			mNameView.setError(getString(R.string.error_invalid_name));
			focusViewName = mNameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			if(focusViewPhoneNo != null)
			{
				focusViewPhoneNo.requestFocus();
			}
			
			if (focusViewEmail != null)
			{
				focusViewEmail.requestFocus();
			}
			
			if(focusViewName != null ){
				focusViewName.requestFocus();
			}
			
			
			
			
			
		} else {
			
			Controls.show_loading_dialog(this, "Signing Up");
			ServerConnectionMaker.sendRequest(this);
			
			 
		}
	}

	

	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		
		
		User user = new User();
		user.setEmail(mEmail);
		user.setPhno(mPhoneNo);
		user.setName(mName);
		

		
		serviceProvider.signup(user, new Callback<Integer>(){

			@Override
			public void failure(RetrofitError response) {
				
//				if (response.httpError(response.getUrl(), response, converter, successType)) {
//					Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();
//			    }
//				else if(response.networkError(response.getUrl(), ))
				
				ServerConnectionMaker.recieveResponse(null);
			}

			@Override
			public void success(Integer auth_token, Response response) {
				Globals.dbhelper.setItem("name", mName);
				Globals.dbhelper.setItem("email", mEmail);
				Globals.dbhelper.setItem("phoneNo", mPhoneNo);
				Globals.dbhelper.setItem("auth-token", auth_token.toString() ); //currently setting auth_code here later will be received by sms
				ServerConnectionMaker.recieveResponse(response);
				Globals.dbhelper.setItem("JSESSIONID",ServerConnectionMaker.star_sessionID );
				startVerification();
				
			}
			
    		
    	});
		
	}


	protected void startVerification() {
		this.finish();
    	Intent i = new Intent(this, Verification.class);
    	startActivity(i);
	}
	public void retry(View v){
		Controls.show_loading_dialog(this, "Retrying...");
		 String ClientID = Globals.dbhelper.getItem("cliendID");
	       if(ClientID != null){
			String email = Globals.dbhelper.getItem("email");
			 Login ln = new Login();
		 	   ln.login(ClientID.toString(), email ,this);
	       }
	}

	@Override
	public void loginSuccess() {
		SharedPreferences preferencesReader = getSharedPreferences(Globals.PREFS_NAME, Context.MODE_PRIVATE);
		String serializedDataFromPreference = preferencesReader.getString(Globals.PREFS_KEY, null);
		com.homelybuysapp.models.Address lastRecentAddress  = com.homelybuysapp.models.Address.create(serializedDataFromPreference);
		if(lastRecentAddress == null){
			Intent in = new Intent(this,com.homelybuysapp.activities.MapActivity.class);
			in.putExtra("instantiator","mainactivity");
			startActivityForResult(in, PICK_DELIVERY_REQUEST);
			finish();	
		}
		else{
			Globals.deliveryAddress = lastRecentAddress;
			 Intent in = new Intent(this,HomeActivity.class);
			 startActivity(in);
		}
		
		
	}


	@Override
	public void loginFailure() {
		
		RelativeLayout networkErrorCon = (RelativeLayout)findViewById(R.id.network_error_container);
		networkErrorCon.setVisibility(View.VISIBLE);
		if(splashProgress != null)
		splashProgress.setVisibility(View.GONE);
		
	}


    
}
