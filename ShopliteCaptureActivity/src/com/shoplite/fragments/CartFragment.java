package com.shoplite.fragments;


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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.shoplite.UI.CartItemAdapter;
import com.shoplite.Utils.Globals;
import eu.livotov.zxscan.R;




public class CartFragment extends Fragment {

	
	protected ListView cartItemsListView;
	protected CartItemAdapter cartAdapter;
	protected LinearLayout emptyCartView;
	
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
	
	

}
