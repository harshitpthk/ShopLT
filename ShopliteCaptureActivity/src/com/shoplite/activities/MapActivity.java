package com.shoplite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.shoplite.fragments.MapFragment;
import com.shoplite.interfaces.MapInterface;

import eu.livotov.zxscan.R;

public class MapActivity extends ActionBarActivity implements MapInterface {
	MapFragment mapFrag = new MapFragment();
	SwipeRefreshLayout swipeLayout ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState	);
		setContentView(R.layout.activity_map);
		getSupportFragmentManager().beginTransaction().add(R.id.map_container,mapFrag).commit();
			
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
	 * @see com.shoplite.interfaces.MapInterface#mapShopStart()
	 */
	@Override
	public void mapShopStart() {
		// TODO Auto-generated method stub
		Intent resultIntent = new Intent();
		// TODO Add extras or a data URI to this intent as appropriate.
		setResult(RESULT_OK, resultIntent);
		finish();
	}

	
}
