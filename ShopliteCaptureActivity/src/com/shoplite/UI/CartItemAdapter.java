
package com.shoplite.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shoplite.Utils.ThreadPreconditions;
import com.shoplite.models.ItemCategory;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class CartItemAdapter extends BaseAdapter {

	protected ArrayList<ItemCategory> cartItemList;
	protected Context mContext;
	 
	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public CartItemAdapter(Context context, ArrayList<ItemCategory> item_order_list) {
		super();
		this.cartItemList = item_order_list;
		this.mContext = context;
		
	}
	 @Override
     public int getCount()
     {
         return this.cartItemList != null ? this.cartItemList.size() : 0;
     }
	 
	 @Override
    public ItemCategory getItem(int position) {
        return cartItemList.get(position);
    }
	 
	 //update method to ask view to redraw themselves in the list view
	 public void updateCart(ArrayList<ItemCategory> cartItemList) {
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
		  ItemCategory itemAdded = cartItemList.get(position);
		  CartItemCard cartItem = new CartItemCard(mContext,itemAdded);
		   
		  if (convertView == null) {
		      convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_item_container, parent, false);  //inflating if convert view is not present to be recycled
			       	 	       
		  }
		  cartItem.setParentView(mContext, (ViewGroup) convertView,cartItemList,this);		 // populate the views by sending convert view as parent to the cart Item card
		
		   
		 return convertView;
         
     }
	
	
	 
	
}
