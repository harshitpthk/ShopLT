/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.Utils.Globals;
import com.shoplite.models.ItemCategory;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class BasicCartItemCard extends BaseItemCard{

	protected View containerView;
	protected View innerView;
	protected TextView itemNameView;
	protected ImageView itemImageView ;
	protected TextView currentMeasureView;
	protected TextView currentPriceView;
	protected TextView quantityView;
	protected TextView totalPriceView;
	protected LinearLayout itemEditView;
	protected Button itemCheckButton;
	
	
	
	//members to notify change in the List view
	protected ArrayList<ItemCategory> cartItemList;
	protected ItemListAdapter itemListAdapter;
	
	public BasicCartItemCard(Context context,ItemCategory item)
	{
		super(context,item);
	}
	public View getInnerView() {
		return innerView;
	}
	public void setInnerView(View innerView) {
		this.innerView = innerView;
	}
	public ItemListAdapter getCartItemAdapter() {
			return itemListAdapter;
	}
	public void setCartItemAdapter(ItemListAdapter cartItemAdapter) {
			this.itemListAdapter = cartItemAdapter;
	}
	public TextView getItemNameView() {
		return itemNameView;
	}
	public void setItemNameView(TextView itemNameView) {
		this.itemNameView = itemNameView;
	}
	public ImageView getItemImageView() {
		return itemImageView;
	}
	public void setItemImageView(ImageView itemImageView) {
		this.itemImageView = itemImageView;
	}
	public TextView getCurrentMeasureView() {
		return currentMeasureView;
	}
	public void setCurrentMeasureView(TextView currentMeasureView) {
		this.currentMeasureView = currentMeasureView;
	}
	public TextView getCurrentPriceView() {
		return currentPriceView;
	}
	public void setCurrentPriceView(TextView currentPriceView) {
		this.currentPriceView = currentPriceView;
	}
	public TextView getQuantityView() {
		return quantityView;
	}
	public void setQuantityView(TextView quantityView) {
		this.quantityView = quantityView;
	}
	public TextView getTotalPriceView() {
		return totalPriceView;
	}
	public void setTotalPriceView(TextView totalPriceView) {
		this.totalPriceView = totalPriceView;
	}
	public LinearLayout getItemEditView() {
		return itemEditView;
	}
	public void setItemEditView(LinearLayout itemEditView) {
		this.itemEditView = itemEditView;
	}
	
	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonOnClick(com.shoplite.UI.BaseItemCard.OnClickActionButtonListener)
	 */
	@Override
	public void setActionButtonOnClick(
			OnClickActionButtonListener actionButtonListener) {
		super.setOnClickActionButtonListener(actionButtonListener);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#getActionButtonOnClick()
	 */
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		return super.getOnClickActionButtonListener();
			
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#fetchItemImage(java.lang.String)
	 */
	@Override
	public void fetchItemImage(String url) {
		 Picasso.with(getmContext()).load(url).into(itemImageView);

	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setUpView(android.content.Context, android.view.ViewGroup)
	 */
	@Override
	public void setUpView(Context context, ViewGroup container) {
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		if(container.findViewById(R.id.cart_item_view) == null){
			this.containerView  = lp.inflate(R.layout.cart_item_card, container);
			
			innerView = (View)containerView.findViewById(R.id.cart_item_view);
			itemNameView = (TextView) containerView.findViewById(R.id.item_name);
			itemImageView = (ImageView) containerView.findViewById(R.id.item_image);
			currentMeasureView = (TextView) containerView.findViewById(R.id.current_measure);
			currentPriceView = (TextView) containerView.findViewById(R.id.current_measure_price);
			quantityView = (TextView)containerView.findViewById(R.id.current_quantity);
			totalPriceView = (TextView)containerView.findViewById(R.id.total_price);
			itemEditView =(LinearLayout)containerView.findViewById(R.id.item_edit_view);
			itemCheckButton = (Button)containerView.findViewById(R.id.item_button);
			ViewHolder holder = new ViewHolder(innerView,itemNameView,itemImageView,
			currentMeasureView,currentPriceView,quantityView,totalPriceView,itemEditView,itemCheckButton);
		
			container.setTag(holder);
			
		}
		else{
			
			this.containerView  = container;
			ViewHolder holder = (ViewHolder) this.containerView.getTag();
				innerView = holder.innerView;
				itemNameView = holder.itemNameView;
				itemImageView = holder.itemImageView;
				currentMeasureView = holder.currentMeasureView;
				currentPriceView = holder.currentPriceView;
				quantityView = holder.quantityView;
				totalPriceView = holder.totalPriceView;
				itemEditView = holder.itemEditView;
				itemCheckButton = holder.itemCheckButton;
			}
		
		super.setViewLayout(this.containerView.getId());
		
		
		
		itemEditView.setVisibility(View.GONE);
		itemCheckButton.setVisibility(View.VISIBLE);
		
		itemCheckButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		setActionButtonText(context.getResources().getString(R.string.pick));
		updateView();
		
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonText(java.lang.String)
	 */
	@Override
	public void setActionButtonText(String text) {
		itemCheckButton.setText(text);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#updateView()
	 */
	@Override
	public void updateView() {
		itemNameView.setText(item.getName());
		currentMeasureView.setText(item.getCurrentMeasure());
		currentPriceView.setText("Price:"+item.getCurrentMsrPrice().toString());
		quantityView.setText(Integer.toString(item.getCurrentQty()));
		totalPriceView.setText("Total Price:"+item.getTotalPrice().toString());
	}
	/**
	 * @param mContext
	 * @param convertView
	 * @param cartItemList2
	 * @param itemListAdapter
	 */
	public void setParentView(ViewGroup container,
			ArrayList<ItemCategory> cartItemListRecieved,
			ItemListAdapter itemListAdapter) {
		// TODO Auto-generated method stub
		this.cartItemList = cartItemListRecieved;
		this.itemListAdapter = itemListAdapter;
		setUpView(getmContext(), container);
		
	}
	
	public static class ViewHolder{
		 View containerView;
		 View innerView;
		 TextView itemNameView;
		 ImageView itemImageView ;
		 TextView currentMeasureView;
		 TextView currentPriceView;
		 TextView quantityView;
		 TextView totalPriceView;
		 LinearLayout itemEditView;
		 Button itemCheckButton;
		 
		 public  ViewHolder(View innerView, TextView itemNameView, ImageView itemImageView,
		  TextView currentMeasureView, TextView currentPriceView, TextView quantityView
		  , TextView totalPriceView, LinearLayout itemEditView, Button itemCheckButton){
			 this.innerView = innerView;
			this.itemNameView = itemNameView;
			this.itemImageView = itemImageView;
			this.currentMeasureView = currentMeasureView;
			this.currentPriceView = currentPriceView;
			this.quantityView = quantityView;
			this.totalPriceView = totalPriceView;
			this.itemEditView =itemEditView;
			this.itemCheckButton = itemCheckButton;
		
		 }
	}

}
