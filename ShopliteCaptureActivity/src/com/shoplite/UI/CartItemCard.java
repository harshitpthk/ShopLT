package com.shoplite.UI;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.fragments.CartFragment;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.models.Product;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;

import eu.livotov.zxscan.R;


public class CartItemCard extends BasicCartItemCard implements PackListInterface{

	
	protected CustomNumberPicker measurePicker;
	protected CustomNumberPicker qtyPicker;
	protected ImageButton delete_button;
	
	
	//Animation Control Members
	protected float previouspoint = 0 ;
	protected float startPoint = 0;
	protected Resources r = mContext.getResources();
	protected float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
	protected int SWIPE_MIN_DISTANCE = 50;
	protected boolean animationMode = false;
	   
	
	public CartItemCard(Context context,Product addedItem){
		super(context,addedItem);
	}
	public void setParentView(Context context,ViewGroup container, ArrayList<Product> cartItemListRecieved,
			 ItemListAdapter cartItemAdapter)
	{
		this.cartItemList = cartItemListRecieved;
		this.itemListAdapter = cartItemAdapter;
		setUpView(context, container);
	}
	
	
	public boolean isAnimationMode() {
		return animationMode;
	}
	public void setAnimationMode(boolean animationMode) {
		this.animationMode = animationMode;
	}
		
	
	public Button getItemButton() {
		return itemButton;
	}
	public void setItemButton(Button itemButton) {
		this.itemButton = itemButton;
	}
		
	public NumberPicker getMeasurePicker() {
		return measurePicker;
	}

	public void setMeasurePicker(CustomNumberPicker measurePicker) {
		this.measurePicker = measurePicker;
	}

	public NumberPicker getQtyPicker() {
		return qtyPicker;
	}

	public void setQtyPicker(CustomNumberPicker qtyPicker) {
		this.qtyPicker = qtyPicker;
	}

	
	
	
	
	@Override
	public void setUpView(Context context , ViewGroup container) {
		super.setUpView(context, container);
		
		setMeasurePicker((CustomNumberPicker)containerView.findViewById(R.id.item_measure_picker));
		setQtyPicker((CustomNumberPicker)containerView.findViewById(R.id.item_quantity_picker));
		
		itemEditView.setVisibility(View.GONE);
		itemCheckBox.setVisibility(View.GONE);
		itemButton.setVisibility(View.VISIBLE);
		
		
		delete_button = (ImageButton)containerView.findViewById(R.id.delete_cart_item);
      	delete_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isAnimationMode()){
					
					final Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slideout);
				    animation.setAnimationListener(new AnimationListener() {
				        @Override
				        public void onAnimationStart(Animation animation) {
				        }

				        @Override
				        public void onAnimationRepeat(Animation animation) {
				        }

				        @Override
				        public void onAnimationEnd(Animation animation) {
				        	delete_from_list();
				        }
				    });
				    containerView.startAnimation(animation);
				    
					
					
				}
				
				
			}
		});
	
		
		itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation animFadeOut = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
		    	 Animation animFadeIn = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
		    	 
		    	 //initiate edit
					if(getItemEditView().getVisibility() == View.GONE){
						getItemEditView().setAnimation(animFadeIn);
					    getItemEditView().setVisibility(View.VISIBLE);
						setActionButtonText("Done");
						if(Globals.cartTotalPrice>0)
							Globals.cartTotalPrice -= item.getTotalPrice();
						// Packlist to be sent for item edited
					}
				//finalize edit
					else{
						Globals.cartTotalPrice += item.getTotalPrice();
						CaptureActivity.actionBar.setTitle(r.getText(R.string.shopping_cart)+
        				"    " + Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0)) +" "+ r.getText(R.string.currency));
						getItemEditView().setAnimation(animFadeOut);
				    	getItemEditView().setVisibility(View.GONE);
						setActionButtonText("Edit");
						editPackList();
					}
			}
		});
		setActionButtonText(context.getResources().getString(R.string.edit));
		containerView.setOnTouchListener(new OnTouchListener() {
			
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch(event.getAction())
                {
	                case MotionEvent.ACTION_DOWN:
	                {          
	                    startPoint=event.getX();
	                                           
	                }
	                break;
	                case MotionEvent.ACTION_MOVE:
	                {       
	                	
	                	previouspoint=event.getX();
		                   
	                	 if(previouspoint - startPoint > SWIPE_MIN_DISTANCE){
	                		 //Right side swap
	                		 if(animationMode){
	                			TranslateAnimation Anim = new TranslateAnimation(-px, 0, 0, 0);
		           			 	Anim.setInterpolator(new BounceInterpolator());
		           			 	Anim.setDuration(300);
		           			 	Anim.setFillAfter(true);
		           			 	innerView.startAnimation(Anim);
		           			 	animationMode = false;
		           			 	getItemButton().setEnabled(true);
		                    }
	
	                    }else if(startPoint - previouspoint > SWIPE_MIN_DISTANCE){
	                    	// Left side swap
	                    	if(!animationMode){
	                    		TranslateAnimation Anim = new TranslateAnimation(0, -px, 0, 0);
	                    		Anim.setInterpolator(new BounceInterpolator());
		           			 	Anim.setDuration(300);
		           			 	Anim.setFillAfter(true);
		           			 	innerView.startAnimation(Anim);
		           			 	getItemButton().setEnabled(false);
		           			 	animationMode = true;
	                    	}
	                    }
	                }break;
	                case MotionEvent.ACTION_CANCEL:
	                {       
                	
	
	                }break;
                }
				
				return true;
			}
		});
		
		initMeasurePicker();
		initQtyPicker();
		updateView();
		
		String  url = "http://cadbury.screaminteractive.com.my/images/products/CADBURY%20DAIRY%20MILK/Cadbury%20Dairy%20Milk/Cadbury-Dairy-Milk75.png";
		fetchItemImage(url);
				
		
		
		
		
		
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
					item.setCurrentItemId(item.getItemList().get(newVal).getId());
					item.setCurrentMeasure(item.getItemList().get(newVal).getName());
					item.setCurrentMsrPrice( item.getItemList().get(newVal).getPrice());
					item.setTotalPrice( (double) Math.round((item.getCurrentQty()* item.getCurrentMsrPrice() * 100.0)/100.0));
					
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
					
					return Integer.toString(value) + " Qty";
				}
			});
			qtyPicker.setOnValueChangedListener(new OnValueChangeListener() {
				
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
					
					item.setCurrentQty( newVal);
					item.setTotalPrice( (double) Math.round((item.getCurrentQty()*item.getCurrentMsrPrice() * 100.0)/100.0));
					updateView();
				}
			});
			qtyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void updateView(){
		super.updateView();
		
	}
			
	@Override
	public OnClickActionButtonListener getActionButtonOnClick() {
		return super.getOnClickActionButtonListener();
		 
	}
	
	@Override
	public void setActionButtonText(String text) {
		super.setActionButtonText(text);
	}

	@Override
	public void setActionButtonOnClick(	OnClickActionButtonListener actionButtonListener) {
		super.setOnClickActionButtonListener(actionButtonListener);
	}
	
	@Override
	public void fetchItemImage(String url) {
		super.fetchItemImage(url);
	}

	
	
	//Packlist Interface Methods
	
	protected void delete_from_list() {
		
		TranslateAnimation Anim = new TranslateAnimation(-px, 0, 0, 0);
	 	Anim.setDuration(10);
	 	Anim.setFillAfter(true);
	 	innerView.startAnimation(Anim);
	 	animationMode = false;
	 	getItemButton().setEnabled(true);
	 	cartItemList.remove(getItem());
	 	Globals.item_added_list.remove(Globals.item_added_list.indexOf(getItem().getCurrentItemId()));
	 	Globals.cartTotalPrice -= item.getTotalPrice();
	 	CaptureActivity.actionBar.setTitle(r.getText(R.string.shopping_cart)+
				 "    "+  Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0))+" " + r.getText(R.string.currency));
	 	
		if(getItem().isSent()){
			OrderItemDetail itemToDelete = new OrderItemDetail(item.getCurrentItemId(), item.getCurrentQty());
			deletePackList(itemToDelete);
		}
		if(cartItemList.isEmpty()){
			CartFragment.emptyCartState();
		}
		itemListAdapter.updateCart(cartItemList);
		
	}
	
	@Override
	public void sendPackList() {
		
	}
	
	@Override
	public void PackListSuccess(PackList obj) {
		if(obj.state==DBState.DELETE){
			for(int i = 0 ;i < obj.products.size() ; i++){
				if(CartGlobals.cartList.contains(obj.products.get(i)))
					CartGlobals.cartList.remove(obj.products.get(i));
				if(CartGlobals.recentDeletedItems.contains(obj.products.get(i)))
					CartGlobals.recentDeletedItems.remove(obj.products.get(i));
			}
			
		}
		else if (obj.state == DBState.INSERT){
			for(int i = 0 ;i < obj.products.size() ; i++){
				CartGlobals.cartList.add(obj.products.get(i));
			}
		}
		else{
			
		}
	}
	
	@Override
	public void editPackList() {
		if(item.isSent()){
			OrderItemDetail itemToDelete = new OrderItemDetail(item.getCurrentItemId(), item.getCurrentQty());
			PackList pl = new PackList();
		}
	}
	
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		ArrayList<OrderItemDetail> deleteList = new ArrayList<OrderItemDetail>();
		deleteList.add(itemToDelete);
		PackList pl = new PackList();
		pl.state = Constants.DBState.DELETE;
		pl.products = deleteList;
		CartGlobals.CartServerRequestQueue.add(pl);
		pl.sendPackList(this);
	}
	

	
}
