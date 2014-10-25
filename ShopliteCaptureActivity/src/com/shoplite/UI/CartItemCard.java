package com.shoplite.UI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.shoplite.models.ItemCategory;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;


public class CartItemCard extends BaseItemCard {

		
	protected TextView itemNameView;
	protected ImageView itemImageView ;
	protected TextView currentMeasureView;
	protected TextView currentPriceView;
	protected TextView quantityView;
	protected TextView totalPriceView;
	protected Button itemButton;
	protected LinearLayout itemEditView;
	protected NumberPicker measurePicker;
	protected NumberPicker qtyPicker;
	
	
	public CartItemCard(Context context,ItemCategory item){
		super(item,context);
			
	}
	public void setParentView(Context context,ViewGroup container)
	{
		setUpView(context, container);
	}
	
	public NumberPicker getMeasurePicker() {
		return measurePicker;
	}

	public void setMeasurePicker(NumberPicker measurePicker) {
		this.measurePicker = measurePicker;
	}

	public NumberPicker getQtyPicker() {
		return qtyPicker;
	}

	public void setQtyPicker(NumberPicker qtyPicker) {
		this.qtyPicker = qtyPicker;
	}

	

	
	
	

	public void initMeasurePicker(){
		measurePicker.setMinValue(0);
		measurePicker.setMaxValue(item.getItemList().size()-1);
		measurePicker.setFormatter(new NumberPicker.Formatter() {
			
			@Override
			public String format(int value) {
				// TODO Auto-generated method stub
				currentMeasure = item.getItemList().get(value).getName();
				currentMsrPrice = item.getItemList().get(value).getPrice();
				totalPrice = currentQty * currentMsrPrice;
				totalPrice = Math.round( totalPrice * 100.0 ) / 100.0; 
				updateView();
				return item.getItemList().get(value).getName();
			}
		});
		measurePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
	}
	public void initQtyPicker(){
		qtyPicker.setMaxValue(quantity);
		qtyPicker.setMinValue(1);
		qtyPicker.setFormatter(new NumberPicker.Formatter() {
			
			@Override
			public String format(int value) {
				// TODO Auto-generated method stub
				currentQty = value;
				totalPrice = currentQty * currentMsrPrice;
				totalPrice = Math.round( totalPrice * 100.0 ) / 100.0; 
				return Integer.toString(value) + " Qty";
			}
		});
		
		qtyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}
	
	

	@Override
	public void updateView(){
		itemNameView.setText(item.getName());
		currentMeasureView.setText(currentMeasure);
		currentPriceView.setText("Price:"+currentMsrPrice.toString());
		quantityView.setText(Integer.toString(quantity));
		totalPriceView.setText("Total Price:"+totalPrice.toString());
	}
	
	

	

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#getActionButtonOnClick()
	 */
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		// TODO Auto-generated method stub
		return super.getOnClickActionButtonListener();
		 
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonText()
	 */
	@Override
	public void setActionButtonText(String text) {
		// TODO Auto-generated method stub
		itemButton.setText(text);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#intImage()
	 */
	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonOnClick(com.shoplite.UI.BaseItemCard.OnClickActionButtonListener)
	 */
	@Override
	public void setActionButtonOnClick(	OnClickActionButtonListener actionButtonListener) {
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
	 * @see com.shoplite.UI.BaseItemCard#setUpView()
	 */
	@Override
	public void setUpView(Context context , ViewGroup container) {
		// TODO Auto-generated method stub
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.cart_item_card, container);
		super.setViewLayout(view.getId());
		itemNameView = (TextView) view.findViewById(R.id.item_name);
		itemImageView = (ImageView) view.findViewById(R.id.item_image);
		currentMeasureView = (TextView) view.findViewById(R.id.current_measure);
		currentPriceView = (TextView) view.findViewById(R.id.current_measure_price);
		quantityView = (TextView)view.findViewById(R.id.current_quantity);
		totalPriceView = (TextView)view.findViewById(R.id.total_price);
		setMeasurePicker((NumberPicker)view.findViewById(R.id.item_measure_picker));
		setQtyPicker((NumberPicker)view.findViewById(R.id.item_quantity_picker));
		itemButton = (Button)view.findViewById(R.id.item_button);
		itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActionButtonOnClick().onClick(null, v);
			}
		});
		itemEditView =(LinearLayout)view.findViewById(R.id.item_edit_view);
		
		updateView();
		
		initMeasurePicker();
		initQtyPicker();
		fetchItemImage(null);
		
	}
	
}
