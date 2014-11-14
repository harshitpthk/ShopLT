package com.shoplite.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoplite.UI.Controls;
import com.shoplite.UI.ItemListAdapter;
import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.models.Product;

import eu.livotov.zxscan.R;

public class ItemsDisplayActivity extends Activity implements ControlsInterface{

	private ArrayList<Product> itemList;
	private String listName;
	private Type listType = new TypeToken<ArrayList<Product>>() {
    }.getType();
    private ItemListAdapter itemAdapter;
    private ListView itemsListView;
    
    private Button importAllItems;
    private Button importSelectedItems;
	private AlertDialog alertDialog;
	private boolean isImportAll;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_items_display);
		itemsListView = (ListView) findViewById(R.id.items_display_list);
		
		listName = getIntent().getStringExtra("ListName"); 
		getActionBar().setTitle(listName);
		
		String ItemListJson = getIntent().getStringExtra("ItemList");
		Gson gson = new Gson();
		itemList = gson.fromJson(ItemListJson, listType);
		importAllItems = (Button) findViewById(R.id.import_all_button);
		importSelectedItems = (Button) findViewById(R.id.import_selected_button);
		
		
		
	}
	@Override
	public void onResume()
	{
		super.onResume();
		itemAdapter = new ItemListAdapter(this, itemList, "basiccartitem");
		itemsListView.setAdapter(itemAdapter);
		importAllItems.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i = 0 ; i < itemList.size() ;i++){
					itemList.get(i).setSelected(true);
				}
				itemAdapter.notifyDataSetChanged();
				isImportAll = true;
				importAll();
			}
		});
		
		importSelectedItems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isImportAll = false;
				importSelected();
			}
		});
		
		
	}
	public void importAll()
	{
		Controls.show_alert_dialog(this, this, R.layout.confirm_import_all_dialog, 250);
		
		
	}
	public void importSelected()
	{
		
		Controls.show_alert_dialog(this, this, R.layout.confirm_import_all_dialog, 250);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds products to the action bar if it is present.
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
		else if (id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ControlsInterface#positive_button_alert_method()
	 */
	@Override
	public void positive_button_alert_method() {
		// TODO Auto-generated method stub
		boolean itemsAlreadyPresent = false;
		if(isImportAll){
			Controls.show_loading_dialog(this, "Importing List to Cart");
			for(int i = 0 ; i < itemList.size();i++)
			{
				if(!Globals.item_added_list.contains(itemList.get(i).getCurrentItemId())){
					Globals.item_added_list.add(itemList.get(i).getCurrentItemId());
					
					Globals.item_order_list.add(itemList.get(i)); 
					Globals.cartTotalPrice += itemList.get(i).getTotalPrice();
				}
				else{
					itemsAlreadyPresent = true;
				}
			}
			Controls.dismiss_progress_dialog();
			if(itemsAlreadyPresent){
				Toast.makeText(this, "List"+ listName +" Imported Successfully with certain Items already present", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this, "List"+ listName +" Imported Successfully", Toast.LENGTH_LONG).show();
				
			}
			finish();
		}
		else{
			for(int i = 0 ; i < itemList.size();i++)
			{
				Controls.show_loading_dialog(this, "Importing products to Cart");
				
				if(itemList.get(i).isSelected()){
					if(!Globals.item_added_list.contains(itemList.get(i).getCurrentItemId())){
						Globals.item_added_list.add(itemList.get(i).getCurrentItemId());
						Globals.item_order_list.add(itemList.get(i)); 
						Globals.cartTotalPrice += itemList.get(i).getTotalPrice();
					}
					else{
						itemsAlreadyPresent = true;
					}
				}
				Controls.dismiss_progress_dialog();
				if(itemsAlreadyPresent){
					Toast.makeText(this, "Selected Items Imported Successfully with certain Items already present", Toast.LENGTH_LONG).show();
				
				}
				else{
					Toast.makeText(this, "Selected Items Imported Successfully", Toast.LENGTH_LONG).show();
				}
				finish();
			}
			
		}
		alertDialog.dismiss();
		
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ControlsInterface#negative_button_alert_method()
	 */
	@Override
	public void negative_button_alert_method() {
		// TODO Auto-generated method stub
		alertDialog.dismiss();
		for(int i = 0 ; i < itemList.size() ;i++){
			itemList.get(i).setSelected(false);
		}
		itemAdapter.notifyDataSetChanged();
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ControlsInterface#save_alert_dialog(android.app.AlertDialog)
	 */
	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
		// TODO Auto-generated method stub
		this.alertDialog = alertDialog;
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ControlsInterface#neutral_button_alert_method()
	 */
	@Override
	public void neutral_button_alert_method() {
		// TODO Auto-generated method stub
		
	}
}
