package com.homelybuysapp.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.UI.ItemListAdapter;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.fragments.CartFragment;
import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.interfaces.ItemInterface;
import com.homelybuysapp.models.Input;
import com.homelybuysapp.models.Product;

import eu.livotov.zxscan.R;

public class ItemsDisplayActivity extends ActionBarActivity implements ControlsInterface,ItemInterface{

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
	private int updateCount = 0;
	private int updateRequiredCount = 0;
	private String instantiator;
	private boolean isProductUpdateFailure = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_app_color));
        }
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_items_display);
		itemsListView = (ListView) findViewById(R.id.items_display_list);
		
		listName = getIntent().getStringExtra("ListName"); 
		String ItemListJson = getIntent().getStringExtra("ItemList");
		instantiator = getIntent().getStringExtra("instantiator");
		if(instantiator.equalsIgnoreCase("savedLists")){
			getSupportActionBar().setTitle(listName);
		}
		else{
			getSupportActionBar().setTitle("Order Number "+listName);
		}
		
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
				isImportAll = false;
				importSelected();
			}
		});
		
		
	}
	
	public int getSelectedCount()
	{
		int count = 0;
		for(int i = 0 ; i < itemList.size() ;i++){
			if(itemList.get(i).isSelected()){
				count++;
			}
		}
		return count;
	}
	public void importAll()
	{
		Controls.show_alert_dialog(this, this, R.layout.confirm_import_all_dialog, 220);
		
		
	}
	public void importSelected()
	{
		if(getSelectedCount() >0)
			Controls.show_alert_dialog(this, this, R.layout.confirm_import_all_dialog, 220);
		else{
			Toast.makeText(this, "Select some items", Toast.LENGTH_SHORT).show();
		}
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
		 if (id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ControlsInterface#positive_button_alert_method()
	 */
	@Override
	public void positive_button_alert_method() {
		boolean itemsAlreadyPresent = false;
		alertDialog.dismiss();
		Controls.show_loading_dialog(this, "Importing to Cart");
		if(isImportAll){
			for(int i = 0 ; i < itemList.size();i++)
			{
				if(!Globals.item_added_list.contains(itemList.get(i).getCurrentItemId())){
					Globals.item_added_list.add(itemList.get(i).getCurrentItemId());
					updateItem(itemList.get(i));
					updateRequiredCount++;
				}
				else{
					itemsAlreadyPresent = true;
				}
			}
						
			
		}
		else{
			for(int i = 0 ; i < itemList.size();i++)
			{
				
				if(itemList.get(i).isSelected()){
					if(!Globals.item_added_list.contains(itemList.get(i).getCurrentItemId())){
						Globals.item_added_list.add(itemList.get(i).getCurrentItemId());
						updateItem(itemList.get(i));
						updateRequiredCount++;
						
					}
					else{
						itemsAlreadyPresent = true;
					}
				}
				
				
			}
			
			
			
		}
		if(updateRequiredCount==0){
			Controls.dismiss_progress_dialog();
			Toast.makeText(this, "Items Already present in the Cart", Toast.LENGTH_LONG).show();
		}

		
	}
	/**
	 * @param product 
	 * 
	 */
	private void updateItem(Product product) {
		Input input = new Input(product.getId(),"productid");
		product.updateItem(input, this);
		
		
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ControlsInterface#negative_button_alert_method()
	 */
	@Override
	public void negative_button_alert_method() {
		alertDialog.dismiss();
		for(int i = 0 ; i < itemList.size() ;i++){
			itemList.get(i).setSelected(false);
		}
		itemAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}
	
	@Override
	public void neutral_button_alert_method() {
		
	}
	
	@Override
	public void getItemList(Product item) {
		
	}
	
	@Override
	public void ItemGetSuccess(Product item) {
		
	}
	
	@Override
	public void ItemGetFailure() {
		
	}
	
	@Override
	public void ItemListGetSuccess(ArrayList<Product> itemFamily) {
		
	}
	
	@Override
	public void getItem() {
		
	}
	
	@Override
	public void updateItemSuccess(Product product) {
		
		Globals.item_order_list.add(product); 
		Globals.cartTotalPrice += product.getTotalPrice();
		CartFragment.updateCart();
		updateCount++;
		if(updateCount == updateRequiredCount){
			updateCount = 0;
			Controls.dismiss_progress_dialog();
			//Toast.makeText(this, "Import Success", Toast.LENGTH_SHORT).show();
			
			if(!isImportAll){
				Toast.makeText(this, "Selected Items Imported Successfully", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this, "List"+ listName +" Imported Successfully", Toast.LENGTH_LONG).show();
			}

			setResult(Activity.RESULT_OK, null);
			finish();
		}
	}
	
	@Override
	public void updateItemFailure() {

		Controls.dismiss_progress_dialog();
		updateCount++;
		if(!isProductUpdateFailure){
			isProductUpdateFailure = true;
			Toast.makeText(this, getString(R.string.product_import_failure), Toast.LENGTH_LONG).show();

		}

	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#productsGetFailure()
	 */
	@Override
	public void productsGetFailure() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#productsGetSuccess()
	 */
	@Override
	public void productsGetSuccess(ArrayList<Product> productList) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#getProducts()
	 */
	@Override
	public void getProducts(Input input) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#searchProductFailure()
	 */
	@Override
	public void searchProductFailure() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#productSearchSuccess(java.util.ArrayList)
	 */
	@Override
	public void productSearchSuccess(ArrayList<Product> productList) {
		// TODO Auto-generated method stub
		
	}
}
