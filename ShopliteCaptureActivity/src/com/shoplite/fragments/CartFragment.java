package com.shoplite.fragments;


import java.util.ArrayList;

import com.shoplite.UI.BaseItemCard;
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;

import eu.livotov.zxscan.R;


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
import android.widget.TextView;
import android.widget.Toast;

public class CartFragment extends Fragment implements PackListInterface{

	
	private TextView empty_cart_view ;
	ArrayList<BaseItemCard> recent_deleted_items = new ArrayList<BaseItemCard>();

	
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

	          }
	       });
		
		
			
		Button orderButton = (Button) root.findViewById(R.id.order_button);
		orderButton.setOnClickListener(new OnClickListener() {
	          public void onClick(View v) {

	          }
	       });
		
		

	       
        return root;
    }
	@Override
	public void onResume()
	{
		super.onResume();	
		empty_cart_view = (TextView)this.getActivity().findViewById(R.id.empty_cart_textview);
		
		if(Globals.item_order_list.size() == 0){
			empty_cart_view.setVisibility(1);
		}
		else{
			empty_cart_view.setVisibility(-1);
		}
	}
	
	public void add_scanned_items()
	{
		if(Globals.item_order_list.size() == 0){
			empty_cart_view.setVisibility(1);
		}
		else{
			empty_cart_view.setVisibility(-1);
		}
		
					
			
		        
			
	}
	
	public void remove_scanned_item(String calling_activity,int position)
	{
		if(Globals.item_order_list != null && Globals.item_order_list.size() > 0){
			if(calling_activity.equals("capture_activity")){
				//mCardArrayAdapter.remove(mCardArrayAdapter.getItem(position));
			}
			
			OrderItemDetail itemToDelete = new OrderItemDetail(Globals.item_order_list.get(position).getCurrentItemId(),Globals.item_order_list.get(position).getCurrentQty());
			
			recent_deleted_items.add(Globals.item_order_list.get(position));
			Globals.item_order_list.remove(position);
			deletePackList(itemToDelete);
			
			if(Globals.item_order_list.size() == 0){
       		 
    			empty_cart_view.setVisibility(1);
        	}
			Toast.makeText(Globals.ApplicationContext, "Last Scanned Item Removed", Toast.LENGTH_SHORT).show();
		}
    	else{
    		Toast.makeText(Globals.ApplicationContext, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
    		
    	}
    	
	}

	public void order(View v)
	{
		
	}
	public  void save_cart_list(View v)
	{
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.cart, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return true;
		//return super.onOptionsItemSelected(item);
	}
	
	
	//PackListInterface
	
	@Override
	public void sendPackList() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void editPackList() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void PackListSuccess(PackList obj) {
		// TODO Auto-generated method stub
		if(obj.state==DBState.DELETE){
			for(int i = 0 ;i < obj.orderedItems.size() ; i++){
				if(CartGlobals.cartList.contains(obj.orderedItems.get(i)))
					CartGlobals.cartList.remove(obj.orderedItems.get(i));
				if(recent_deleted_items.contains(obj.orderedItems.get(i)))
					recent_deleted_items.remove(obj.orderedItems.get(i));
			}
			
		}
		else if (obj.state == DBState.INSERT){
			for(int i = 0 ;i < obj.orderedItems.size() ; i++){
				CartGlobals.cartList.add(obj.orderedItems.get(i));
			}
		}
		else{
			
		}
	}
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		// TODO Auto-generated method stub
		PackList pl = new PackList();
		pl.orderedItems = new ArrayList<OrderItemDetail>();
		pl.orderedItems.add(itemToDelete);
		pl.state = DBState.DELETE;
		CartGlobals.CartServerRequestQueue.add(pl);
		pl.sendPackList(this);
	}
}
