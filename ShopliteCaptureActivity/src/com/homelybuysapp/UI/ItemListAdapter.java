
package com.homelybuysapp.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homelybuysapp.Utils.ThreadPreconditions;
import com.homelybuysapp.models.Product;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class ItemListAdapter extends BaseAdapter {

	protected ArrayList<Product> cartItemList;
	protected Context mContext;
	protected String type;
	 
	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public ItemListAdapter(Context context, ArrayList<Product> item_order_list,String type) {
		super();
		this.cartItemList = item_order_list;
		this.mContext = context;
		this.type = type;
		
	}
	 @Override
     public int getCount()
     {
         return this.cartItemList != null ? this.cartItemList.size() : 0;
     }
	 
	 @Override
    public Product getItem(int position) {
        return cartItemList.get(position);
    }
	 
	 //update method to ask view to redraw themselves in the list view
	 public void updateCart(ArrayList<Product> cartItemList) {
		 ThreadPreconditions.checkOnMainThread();   
		 this.cartItemList = cartItemList;
	     notifyDataSetChanged();
	     
	 }
	 
	 
	 @Override
	public long getItemId(int position) {
		return position;
	}
	
	 @Override
     public View getView( int position,  View convertView, final ViewGroup parent)
     {
		 Product itemAdded = cartItemList.get(position);
			
		  if (convertView == null) {
		      convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_item_container, parent, false);  //inflating if convert view is not present to be recycled
		  }
		 
		 	 
		 
		  if(type.equalsIgnoreCase("cartItem")){
			  CartItemCard itemCard = new CartItemCard(mContext,itemAdded);
			  itemCard.setParentView((ViewGroup) convertView,cartItemList,this);		 // populate the views by sending convert view as parent to the cart ProductVariance card
		  }
		  else if(type.equalsIgnoreCase("drawerItem")){
			  DrawerItemCard itemCard = new DrawerItemCard(mContext, itemAdded);
			  itemCard.setParentView((ViewGroup) convertView,cartItemList,this);		 // populate the views by sending convert view as parent to the cart ProductVariance card
		 }
		  else if(type.equalsIgnoreCase("basicCartItem")){
			  BasicCartItemCard itemCard = new BasicCartItemCard( mContext,itemAdded);
			  itemCard.setParentView((ViewGroup) convertView,cartItemList,this);		 // populate the views by sending convert view as parent to the cart ProductVariance card
				
				
		  }
		  //add other cards if necessary 
		    
		   
		 return convertView;
         
     }
	
	
	 
	
}
