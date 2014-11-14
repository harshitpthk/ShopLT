/**
 * 
 */
package com.shoplite.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoplite.models.Product;
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
	public DrawerItemCard(Context context,Product item) {
		super(context,item);
				
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
		itemPriceView.setText(item.getCurrentMsrPrice().toString());
		fetchItemImage(null);
		
	}
	
	
	

	
	@Override
	public void setActionButtonOnClick(
			OnClickActionButtonListener actionButtonListener) {
		super.setOnClickActionButtonListener(actionButtonListener);
	}

	
	@Override
	public void fetchItemImage(String url) {
		url = "http://cadbury.screaminteractive.com.my/images/products/CADBURY%20DAIRY%20MILK/Cadbury%20Dairy%20Milk/Cadbury-Dairy-Milk75.png";
		 Picasso.with(getmContext()).load(url).into(itemImageView);
	}



	
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		return  super.getOnClickActionButtonListener();
		 
	}

	
	@Override
	public void setUpView(Context context, ViewGroup container) {
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
				getActionButtonOnClick().onClick(getItem(), v);
			}
		});
		updateView();
		
		
	}
	

	
	@Override
	public void setActionButtonText(String text) {
			
	}
	/**
	 * @param convertView
	 * @param cartItemList
	 * @param itemListAdapter
	 */
	public void setParentView(ViewGroup convertView,
			ArrayList<Product> cartItemList,
			ItemListAdapter itemListAdapter) {
		// TODO Auto-generated method stub
		
	}

}
