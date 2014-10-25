/**
 * 
 */
package com.shoplite.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.models.ItemCategory;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class DrawerItemCard extends BaseItemCard {
	
	protected Button itemButton;
	protected TextView itemNameView;
	protected ImageView itemImageView;
	protected TextView itemPriceView;

	
	/**
	 * @param item
	 * @param container 
	 */
	public DrawerItemCard(Context context,ItemCategory item) {
		super(item,context);
		
		// TODO Auto-generated constructor stub
	}
	public void setParentView(Context context,ViewGroup container)
	{
		setUpView(context, container);
	}
	
	public Button getItemButton() {
		return itemButton;
	}

	public void setItemButton(Button itemButton) {
		this.itemButton = itemButton;
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

	public TextView getItemPriceView() {
		return itemPriceView;
	}

	public void setItemPriceView(TextView itemPriceView) {
		this.itemPriceView = itemPriceView;
	}

	@Override
	public void updateView(){
		itemNameView.setText(item.getName());
		itemPriceView.setText(currentMsrPrice.toString());
		fetchItemImage(null);
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonOnClick(com.shoplite.UI.BaseItemCard.OnClickActionButtonListener)
	 */
	@Override
	public void setActionButtonOnClick(
			OnClickActionButtonListener actionButtonListener) {
		// TODO Auto-generated method stub
		super.setOnClickActionButtonListener(actionButtonListener);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#fetchItemImage()
	 */
	@Override
	public void fetchItemImage(String url) {
		// TODO Auto-generated method stub
		url = "http://cadbury.screaminteractive.com.my/images/products/CADBURY%20DAIRY%20MILK/Cadbury%20Dairy%20Milk/Cadbury-Dairy-Milk75.png";
		 Picasso.with(getmContext()).load(url).into(itemImageView);
	}



	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#getActionButtonOnClick()
	 */
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		// TODO Auto-generated method stub
		return  super.getOnClickActionButtonListener();
		 
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setUpView(android.content.Context, android.view.ViewGroup, com.shoplite.models.ItemCategory)
	 */
	@Override
	public void setUpView(Context context, ViewGroup container) {
		// TODO Auto-generated method stub
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.drawer_item_card, container);
		super.setViewLayout(view.getId());
		itemNameView = (TextView) view.findViewById(R.id.item_name);
		itemPriceView = (TextView) view.findViewById(R.id.item_price);
		itemImageView = (ImageView) view.findViewById(R.id.item_image);
		itemButton = (Button)view.findViewById(R.id.item_button);
		itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActionButtonOnClick().onClick(getItem(), v);
			}
		});
		updateView();
		
		
	}
	

	/**
	 * 
	 */
	


	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonText(java.lang.String)
	 */
	@Override
	public void setActionButtonText(String text) {
		// TODO Auto-generated method stub
		
	}

}
