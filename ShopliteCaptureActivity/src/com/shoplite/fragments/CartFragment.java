package com.shoplite.fragments;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.Card.OnUndoSwipeListListener;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

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

	private  Card card;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private CardArrayAdapter mCardArrayAdapter;
	CardHeader header ;
	private TextView empty_cart_view ;
	ArrayList<ItemCategory> recent_deleted_items = new ArrayList<ItemCategory>();
	private CardListView cartlistView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_cart);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,        
          //      WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.activity_cart, container, false);
		Button saveCartButton = (Button) root.findViewById(R.id.save_cart_list);
		saveCartButton.setOnClickListener(new OnClickListener() {
	          public void onClick(View v) {

	          }
	       });
		
		root.setOnTouchListener(new OnTouchListener()
	    {
	       

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
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
		mCardArrayAdapter = new CardArrayAdapter(this.getActivity(),cards);
		mCardArrayAdapter.setEnableUndo(true);
		
		cartlistView = (CardListView) this.getActivity().findViewById(R.id.cart_items);
		if (cartlistView!=null){
            cartlistView.setAdapter(mCardArrayAdapter);
        }
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
		
		 //Create a Card
			
			 card = new Card(this.getActivity());
			 card.setId(Integer.toString(Globals.item_order_list.size()-1));
			 header = new CardHeader(this.getActivity());
			 header.setTitle(Globals.item_order_list.get(Globals.item_order_list.size()-1).getName());
			 card.addCardHeader(header);
			 card.setSwipeable(true);
			 card.setClickable(true);
			 card.setOnClickListener(new Card.OnCardClickListener() {
				
				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					Toast.makeText(Globals.ApplicationContext, "Card Clicked",2000 ).show();
				}
			});
			 //cards.add(card);
			
			 card.setOnSwipeListener(new Card.OnSwipeListener() {
		            @Override
		            public void onSwipe(Card card) {
		                //Do something
		            	int remove_index = Globals.find_item_index_from_title(card.getCardHeader().getTitle());
		            	
		            	remove_scanned_item("cart_fragment",remove_index);
		            	
		            }
		        });
			
			 card.setOnUndoSwipeListListener(new OnUndoSwipeListListener() {
	                @Override
	                public void onUndoSwipe(Card card) {
	                    //Do something
	                	//have to do undo swipe listener with adding items back to the queue and creating a send request again
	                	//Globals.item_order_list.add(recent_deleted_item);
	                	for(int i = 0 ; i < recent_deleted_items.size();i++ ){
	                		
	                	}
	                	if(Globals.item_order_list.size()>0){
	                		empty_cart_view.setVisibility(-1);
	                	}
	                }
	          });
			 mCardArrayAdapter.add(card);
		 
					
			
		        
			
	}
	
	public void remove_scanned_item(String calling_activity,int position)
	{
		if(Globals.item_order_list != null && Globals.item_order_list.size() > 0){
			if(calling_activity.equals("capture_activity")){
				mCardArrayAdapter.remove(mCardArrayAdapter.getItem(position));
			}
			
			OrderItemDetail itemToDelete = new OrderItemDetail(Globals.item_order_list.get(position).getItemList().get(0).getId(),Globals.item_order_list.get(position).getItemList().get(0).getQuantity());
			
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return true;
		//return super.onOptionsItemSelected(item);
	}
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
		for(int i = 0 ;i < obj.items.size() ; i++){
			if(CartGlobals.cartList.contains(obj.items.get(i)))
				CartGlobals.cartList.remove(obj.items.get(i));
			if(recent_deleted_items.contains(obj.items.get(i)))
				recent_deleted_items.remove(obj.items.get(i));
		}
		
	}
	else if (obj.state == DBState.INSERT){
		for(int i = 0 ;i < obj.items.size() ; i++){
			CartGlobals.cartList.add(obj.items.get(i));
		}
	}
	else{
		
	}
}
@Override
public void deletePackList(OrderItemDetail itemToDelete) {
	// TODO Auto-generated method stub
	PackList pl = new PackList();
	pl.items = new ArrayList<OrderItemDetail>();
	pl.items.add(itemToDelete);
	pl.state = DBState.DELETE;
	CartGlobals.CartServerRequestQueue.add(pl);
	pl.sendPackList(this);
}
}
