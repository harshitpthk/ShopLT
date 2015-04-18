/**
 * 
 */
package com.homelybuysapp.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.models.Product;
import com.squareup.picasso.Picasso;

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
		String[]  measureStringArray = new String[item.getItemList().size()] ;
		for(int i = 0 ; i < item.getItemList().size();i++ ){
			measureStringArray[i] = item.getItemList().get(i).getName();
		}
		try{
			measurePicker.setMinValue(0);
			measurePicker.setMaxValue(0);
			measurePicker.setDisplayedValues(measureStringArray);
			measurePicker.setMaxValue(item.getItemList().size()-1);
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
			
			String[]  qtyStringArray = {"1 Qty", "2 Qty", "3 Qty", "4 Qty","5 Qty"};
			
			
			qtyPicker.setMaxValue(0);
			qtyPicker.setMinValue(0);
			qtyPicker.setDisplayedValues(qtyStringArray);
			qtyPicker.setMaxValue(4);

			qtyPicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					
					item.setCurrentQty( newVal + 1);
					item.setTotalPrice( (double) Math.round((item.getCurrentQty()*item.getCurrentMsrPrice() * 100.0)/100.0));
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
