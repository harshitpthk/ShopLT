package com.shoplite.fragments;


import java.util.Date;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shoplite.UI.AddItemCard;
import com.shoplite.UI.CartItemAdapter;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.Globals;
import com.shoplite.database.DbHelper;
import com.shoplite.interfaces.ControlsInterface;

import eu.livotov.zxscan.R;




public class CartFragment extends Fragment implements ControlsInterface {

	
	protected ListView cartItemsListView;
	protected CartItemAdapter cartAdapter;
	protected LinearLayout emptyCartView;
	private AlertDialog alertDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        
		View root = inflater.inflate(R.layout.activity_cart, container, false);
		root.setOnTouchListener(new OnTouchListener()
	    {
	       

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
	    });
		
		
		
		Button saveCartButton = (Button) root.findViewById(R.id.save_cart_list);
		saveCartButton.setOnClickListener(new OnClickListener() {
	          public void onClick(View v) {
	        	  if(Globals.item_order_list.size() >0){
	        		  show_save_list_dialog();
	        	  }
	          }
	       });
		
		
			
		Button orderButton = (Button) root.findViewById(R.id.order_button);
		orderButton.setOnClickListener(new OnClickListener() {
	          public void onClick(View v) {

	          }
	       });
		
		
		cartItemsListView = (ListView)root.findViewById(R.id.cart_item_list_view);
		
		emptyCartView = (LinearLayout)root.findViewById(R.id.empty_cart_view);
		
	       
        return root;
    }
	@Override
	public void onResume()
	{
		super.onResume();	
		
		if(Globals.item_order_list.size() == 0){
			emptyCartView.setVisibility(View.VISIBLE);
		}
		else{
			emptyCartView.setVisibility(View.GONE);
			cartAdapter = new CartItemAdapter(getActivity(), Globals.item_order_list);
			cartItemsListView.setAdapter(cartAdapter);
		}
		
		
	}
	
		
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		super.onCreateOptionsMenu(menu, getActivity().getMenuInflater());
//		return false;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return true;
		//return super.onOptionsItemSelected(item);
	}
	
	protected void show_save_list_dialog()
	{
		Controls.show_alert_dialog( this, getActivity(), R.layout.save_list_layout,200);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ControlsInterface#positive_button_alert_method()
	 */
	@Override
	public void positive_button_alert_method() {
		// TODO Auto-generated method stub
		EditText listNameInput = (EditText) this.alertDialog.findViewById(R.id.list_save_name_input);
		listNameInput.setError(null);
		if(listNameInput.getText().toString().length() <= 0){
			listNameInput.setError(getString(R.string.error_field_required));
		}
		else{
			//boolean result = DbHelper.storeShoppingList(Globals.item_order_list);
			try{
			
				AddItemCard ad = new AddItemCard(getActivity(),Globals.fetched_item_category);
				String jsonString = new Gson().toJson(ad);
				Toast.makeText(getActivity(), jsonString, Toast.LENGTH_LONG).show();
//			Toast.makeText(getActivity()," list:    " + new Gson().toJson(Globals.item_order_list.get(0)).toString()
//			, Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){
				e.printStackTrace();
				
			}

		}
		Toast.makeText(getActivity(), "positive button", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void negative_button_alert_method() {
		// TODO Auto-generated method stub
		this.alertDialog.dismiss();
		Toast.makeText(getActivity(), "negative button", Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
		// TODO Auto-generated method stub
		this.alertDialog = alertDialog;
	}
	
	@Override
	public void neutral_button_alert_method() {
		// TODO Auto-generated method stub
		
	}
	
	

}
