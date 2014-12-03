/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoplite.models.Product;
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
	protected Button itemButton;
	protected CheckBox itemCheckBox;
	
	
	
	//members to notify change in the List view
	protected ArrayList<Product> cartItemList;
	protected ItemListAdapter itemListAdapter;
	
	public BasicCartItemCard(Context context,Product item)
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
			itemButton = (Button)containerView.findViewById(R.id.item_button);
			itemCheckBox = (CheckBox)containerView.findViewById(R.id.item_checkbox);
			ViewHolder holder = new ViewHolder(innerView,itemNameView,itemImageView,
			currentMeasureView,currentPriceView,quantityView,totalPriceView,itemEditView,itemButton,itemCheckBox);
		
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
				itemButton = holder.itemCheckButton;
				itemCheckBox = holder.itemCheckBox;
			}
		
		super.setViewLayout(this.containerView.getId());
		
		
		
		itemEditView.setVisibility(View.GONE);
		itemButton.setVisibility(View.GONE);
		itemCheckBox.setVisibility(View.VISIBLE);
		
		itemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					item.setSelected(true);
				}
				else{
					item.setSelected(false);
				}
				
			}
		});
				
		
		if(item.isSelected()){
			itemCheckBox.setChecked(true);
		}
		else{
			itemCheckBox.setChecked(false);
		}
		
		setCheckBoxText(context.getResources().getString(R.string.pick));
		updateView();
		
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonText(java.lang.String)
	 */
	@Override
	public void setActionButtonText(String text) {
		itemButton.setText(text);
	}
	
	public void setCheckBoxText(String text)
	{
		itemCheckBox.setText(text);
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
			ArrayList<Product> cartItemListRecieved,
			ItemListAdapter itemListAdapter) {
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
		  CheckBox itemCheckBox;
			
		 
		 public  ViewHolder(View innerView, TextView itemNameView, ImageView itemImageView,
		  TextView currentMeasureView, TextView currentPriceView, TextView quantityView
		  , TextView totalPriceView, LinearLayout itemEditView, Button itemCheckButton,CheckBox itemCheckBox){
			 this.innerView = innerView;
			this.itemNameView = itemNameView;
			this.itemImageView = itemImageView;
			this.currentMeasureView = currentMeasureView;
			this.currentPriceView = currentPriceView;
			this.quantityView = quantityView;
			this.totalPriceView = totalPriceView;
			this.itemEditView =itemEditView;
			this.itemCheckButton = itemCheckButton;
			this.itemCheckBox = itemCheckBox;
		 }
	}

}
