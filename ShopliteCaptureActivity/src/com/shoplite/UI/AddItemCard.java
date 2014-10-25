/**
 * 
 */
package com.shoplite.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.models.ItemCategory;
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
	public AddItemCard(Context context,ItemCategory item) {
		super(item,context);
		
		// TODO Auto-generated constructor stub
	}
	public void setParentView(Context context,ViewGroup container)
	{
		setUpView(context, container);
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
				setCurrentItemId(item.getItemList().get(newVal).getId());
				setCurrentMeasure(item.getItemList().get(newVal).getName());
				setCurrentMsrPrice( item.getItemList().get(newVal).getPrice());
				setTotalPrice( (double) Math.round((getCurrentQty()*getCurrentMsrPrice() * 100.0)/100.0));
				
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
			
		
		qtyPicker.setMaxValue(quantity);
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
				setCurrentQty( newVal);
				setTotalPrice( (double) Math.round((getCurrentQty()*getCurrentMsrPrice() * 100.0)/100.0));
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
	 * @see com.shoplite.UI.BaseItemCard#getActionButtonOnClick()
	 */
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		// TODO Auto-generated method stub
		 
		return super.getOnClickActionButtonListener();
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#fetchItemImage()
	 */
	@Override
	public void fetchItemImage(String url) {
		// TODO Auto-generated method stub
		url = "http://qph.is.quoracdn.net/main-qimg-1f61abfcc3055f0cef7cc5233c5904db?convert_to_webp=true";
		 Picasso.with(getmContext()).load(url).into(itemImageView);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setUpView()
	 */
	
	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setUpView(android.content.Context, android.view.ViewGroup, com.shoplite.models.ItemCategory)
	 */
	@Override
	public void setUpView(Context context, ViewGroup container) {
		// TODO Auto-generated method stub
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.add_item_card, container);
		super.setViewLayout(view.getId());
		itemNameView = (TextView) view.findViewById(R.id.item_name);
		
		itemImageView = (ImageView) view.findViewById(R.id.item_image);
		itemButton = (Button)view.findViewById(R.id.item_button);
		itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				getActionButtonOnClick().onClick(null, view);
				
			}
		});
		 totalPriceView = (TextView) view.findViewById(R.id.total_price);
		 measurePicker= (NumberPicker) view.findViewById(R.id.item_measure_picker);
		qtyPicker = (NumberPicker) view.findViewById(R.id.item_quantity_picker);
		updateView();
		initMeasurePicker();
		initQtyPicker();
		fetchItemImage(null);
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#setActionButtonText(java.lang.String)
	 */
	@Override
	public void setActionButtonText(String text) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.UI.BaseItemCard#updateView()
	 */
	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		itemNameView.setText(item.getName());
		totalPriceView.setText("Total Price:"+totalPrice.toString());
		
	}

}
