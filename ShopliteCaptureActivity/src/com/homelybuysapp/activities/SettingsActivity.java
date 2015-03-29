package com.homelybuysapp.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import eu.livotov.zxscan.R;

public class SettingsActivity extends ActionBarActivity {
	private TextView versionView ;
	private FrameLayout preferenceContainer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		preferenceContainer =(FrameLayout) findViewById(R.id.preference_container);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.settings);
		
//		getFragmentManager().beginTransaction()
//         .replace(R.id.preference_container, new SettingsFragment())
//         .commit();
		 
		 PackageManager packageManager = this.getPackageManager();
		 String versionName = "0.0.1";
		
		 try {
			versionName = packageManager.getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}
		 versionView = (TextView)findViewById(R.id.version_text_view);
		 versionView.setText("Build Version: "+versionName);
		 
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class SettingsFragment extends PreferenceFragment {
	    
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	         addPreferencesFromResource(R.xml.preference);
	         
	        
	    }
	   
	}
}
