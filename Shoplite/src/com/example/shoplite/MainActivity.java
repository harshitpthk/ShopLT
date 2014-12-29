package com.example.shoplite;

import io.fabric.sdk.android.Fabric;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.sholite.R;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.database.DbHelper;
import com.shoplite.models.User;

import eu.livotov.zxscan.ZXScanHelper;

public class MainActivity extends Activity implements ConnectionInterface  {


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
    	   String email = Globals.dbhelper.getItem("email");
    	   Login ln = new Login();
    	   ln.login(ClientID.toString(), email ,this);
       }
       else if(auth_token != null){
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
	
    
    
    
    
    public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		        // String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         // Handle successful scan
		         String scannedCode = ZXScanHelper.getScannedCode(intent);
		        boolean val = true;
		       // Controls.show_alert_with_input("The product found", scannedCode, this);
		         while(val){
		        	 
		         
		        }
		        // Toast.makeText(this, "returned OK" + scannedCode, Toast.LENGTH_SHORT).show();
		         
		         
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		    	//  Toast.makeText(this, "returned null  ", Toast.LENGTH_SHORT).show();
		      }
		   }
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds products to the action bar if it is present.
       
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
			public void failure(RetrofitError arg0) {
				
				if (arg0.isNetworkError()) {
					Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();
			    }
				Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_LONG).show();
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


    
}
