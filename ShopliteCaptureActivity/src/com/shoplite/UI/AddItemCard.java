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
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.shoplite.models.Product;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class AddItemCard extends BaseItemCard {

	protected Button itemButton;
	protected TextView itemNameView;
	protected ImageView itemImageView;
	protected TextView totalPriceView;
	protected NumberPicker measurePicker;
	protected NumberPicker qtyPicker;
	/**
	 * @param item
	 */
	//constructor
	public AddItemCard()
	{
		
	}
	public AddItemCard(Context context,Product item) {
		super(context,item);
		item.setCurrentItemId(item.getItemList().get(0).getId());
		item.setCurrentMeasure(item.getItemList().get(0).getName());
		item.setCurrentQty(1);
		item.setCurrentMsrPrice(item.getItemList().get(0).getPrice());
		item.setTotalPrice(item.getCurrentMsrPrice() * item.getCurrentQty());
	}
	
	public void setParentView(Context context,ViewGroup container)
	{
		setUpView(context, container);
	}
	
	@Override
	public void setUpView(Context context, ViewGroup container) {
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.add_item_card, container);
		super.setViewLayout(view.getId());
		
		itemNameView = (TextView) view.findViewById(R.id.item_name);
		itemImageView = (ImageView) view.findViewById(R.id.item_image);
//		itemButton = (Button)view.findViewById(R.id.item_button);
//		itemButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				getActionButtonOnClick().onClick(null, view);
//				
//			}
//		});
		totalPriceView = (TextView) view.findViewById(R.id.total_price);
		measurePicker= (NumberPicker) view.findViewById(R.id.item_measure_picker);
		qtyPicker = (NumberPicker) view.findViewById(R.id.item_quantity_picker);
		updateView();
		initMeasurePicker();
		initQtyPicker();
		String url = "http://s3-ap-southeast-1.amazonaws.com/static.shoplite/product_image/"+item.getId()+".jpg";
		
		fetchItemImage(url);
	}
	@Override
	public void updateView() {
		itemNameView.setText(item.getName());
		totalPriceView.setText("Total Price:" + item.getTotalPrice().toString());
		
	}
	
	public void initMeasurePicker(){
		try{
			measurePicker.setMinValue(0);
			measurePicker.setMaxValue(item.getItemList().size()-1);
			measurePicker.setFormatter(new NumberPicker.Formatter() {
				
				@Override
				public String format(int value) {
					
					return item.getItemList().get(value).getName();
				}
			});
			measurePicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					// TODO Auto-generated method stub
					int id = item.getItemList().get(newVal).getId();
					double currentMeasurePrice = item.getItemList().get(newVal).getPrice();
					String currentMeausureName = item.getItemList().get(newVal).getName();
					item.setCurrentItemId(id);
					item.setCurrentMsrPrice(currentMeasurePrice);
					item.setCurrentMeasure(currentMeausureName);
					
					double totalPrize = item.getCurrentMsrPrice() * item.getCurrentQty();
					item.setTotalPrice(totalPrize);
					updateView();
				}
			});
			measurePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void initQtyPicker(){
		try{
			
		
			qtyPicker.setMaxValue(5);
			qtyPicker.setMinValue(1);
			qtyPicker.setFormatter(new NumberPicker.Formatter() {
				
				@Override
				public String format(int value) {
					// TODO Auto-generated method stub
					
					return Integer.toString(value) + " Qty";
				}
			});
			qtyPicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					
					// TODO Auto-generated method stub
					int qty = newVal;
					item.setCurrentQty(qty);
					double totalPrice = item.getCurrentQty() * item.getCurrentMsrPrice();
					item.setTotalPrice(totalPrice);
					updateView();
				}
			});
			qtyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
	@Override
	public void setActionButtonOnClick(
			OnClickActionButtonListener actionButtonListener) {
		super.setOnClickActionButtonListener(actionButtonListener);
		
	}

	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		 
		return super.getOnClickActionButtonListener();
	}
	
	@Override
	public void fetchItemImage(String url) {
		 Picasso.with(getmContext())
		 .load(url)
		 .placeholder(R.drawable.productplaceholder)
		 .into(itemImageView);
	}
		
	@Override
	public void setActionButtonText(String text) {
		
	}

	
	

}
