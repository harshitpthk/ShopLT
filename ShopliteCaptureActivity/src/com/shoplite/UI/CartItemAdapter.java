/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.Utils.ThreadPreconditions;
import com.shoplite.models.ItemCategory;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class CartItemAdapter extends BaseAdapter {

	protected ArrayList<BaseItemCard> cartItemList;
	protected Context mContext;
	 
	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public CartItemAdapter(Context context, ArrayList<BaseItemCard> cartItemList) {
		super();
		// TODO Auto-generated constructor stub
		this.cartItemList = cartItemList;
		this.mContext = context;
		
	}
	 @Override
     public int getCount()
     {
         return this.cartItemList != null ? this.cartItemList.size() : 0;
     }
	 
	 @Override
    public BaseItemCard getItem(int position) {
        return cartItemList.get(position);
    }
	 
	 public void updateCart(ArrayList<BaseItemCard> cartItemList) {
		 ThreadPreconditions.checkOnMainThread();   
		 this.cartItemList = cartItemList;
	     notifyDataSetChanged();
	 }
	 
	 
	 @Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	 @Override
     public View getView( int position,  View convertView, final ViewGroup parent)
     {
		  BaseItemCard baseItem = cartItemList.get(position);
		  CartItemCard cartItem = new CartItemCard(mContext,baseItem);
		   
		  if (convertView == null) {
		     // inflate if not recycled
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_item_container, parent, false);
			       	 	       
		  }
		  cartItem.setParentView(mContext, (ViewGroup) convertView,cartItemList,baseItem,this);		 // populate the views
		
		   
		 return convertView;
         
     }
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	
	 
	
}
