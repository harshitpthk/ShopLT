package com.shoplite.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoplite.models.ItemCategory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import eu.livotov.zxscan.R;

public class ItemsDisplayActivity extends Activity {

	private ArrayList<ItemCategory> itemList;
	private String listName;
	private Type listType = new TypeToken<ArrayList<ItemCategory>>() {
    }.getType();
    
    private ListView itemsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_items_display);
		itemsListView = (ListView) findViewById(R.id.items_display_list);
		
		listName = getIntent().getStringExtra("ListName"); 
		getActionBar().setTitle(listName);
		
		String ItemListJson = getIntent().getStringExtra("ItemList");
		Gson gson = new Gson();
		itemList = gson.fromJson(ItemListJson, listType);
		
	}
	@Override
	public void onResume()
	{
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.items_display, menu);
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
}
