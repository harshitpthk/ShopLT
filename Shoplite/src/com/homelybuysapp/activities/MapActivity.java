package com.homelybuysapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.fragments.MapFragment;
import com.homelybuysapp.interfaces.MapInterface;

public class MapActivity extends ActionBarActivity implements MapInterface {
	
	private String instantiator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState	);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    
		Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_app_color));
        }
		setContentView(R.layout.activity_map);
		
		new LoadUI().execute();
		instantiator = getIntent().getStringExtra("instantiator");
		
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
		
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.MapInterface#mapShopStart()
	 */
	@Override
	public void mapShopStart() {
		// TODO Auto-generated method stub
		Intent resultIntent = new Intent();
		// TODO Add extras or a data URI to this intent as appropriate.
		
		if(instantiator.equalsIgnoreCase("captureactivity")){
			setResult(RESULT_OK, resultIntent);
			finish();
		}
		else{
			 Intent in = new Intent(this,HomeActivity.class);
			 startActivity(in);
		}
		
	}
	private class LoadUI extends AsyncTask<String, Integer, String> {
		   @Override
		   protected void onPreExecute() {
		      super.onPreExecute();
		   }

		   @Override
		   protected String doInBackground(String... params) {
		     
			MapFragment mapFrag = new MapFragment();
				
			getSupportFragmentManager().beginTransaction().add(R.id.map_container,mapFrag).commit();
				
		     return "ok";
		   }

		   @Override
		   protected void onProgressUpdate(Integer... values) {
		      super.onProgressUpdate(values);
		      
		   }

		   @Override
		   protected void onPostExecute(String result) {
		      super.onPostExecute(result);
		   }
	    			
	    			
		      
		   }
	
}
