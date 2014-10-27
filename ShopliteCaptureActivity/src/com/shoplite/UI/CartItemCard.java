package com.shoplite.UI;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.nfc.cardemulation.CardEmulation;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;
import com.squareup.picasso.Picasso;

import eu.livotov.zxscan.R;


public class CartItemCard extends BaseItemCard implements PackListInterface{

		
	

	protected TextView itemNameView;
	protected ImageView itemImageView ;
	protected TextView currentMeasureView;
	protected TextView currentPriceView;
	protected TextView quantityView;
	protected TextView totalPriceView;
	protected Button itemButton;
	protected LinearLayout itemEditView;
	protected CustomNumberPicker measurePicker;
	protected CustomNumberPicker qtyPicker;
	protected View containerView;
	protected View innerView;
	
	float previouspoint = 0 ;
    float startPoint=0;
    Resources r = mContext.getResources();
	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
	
	public View getInnerView() {
		return innerView;
	}
	public void setInnerView(View innerView) {
		this.innerView = innerView;
	}

	private static final int SWIPE_MIN_DISTANCE = 50;
    private boolean animationMode = false;
	private ArrayList<BaseItemCard> cartItemList;
	private BaseItemCard baseItem;
	private CartItemAdapter cartItemAdapter;
	
    
    
    
    public ArrayList<BaseItemCard> getCartItemAdapter() {
		return cartItemList;
	}
	public void setCartItemAdapter(ArrayList<BaseItemCard> cartItemAdapter) {
		this.cartItemList = cartItemAdapter;
	}
	public boolean isAnimationMode() {
		return animationMode;
	}
	public void setAnimationMode(boolean animationMode) {
		this.animationMode = animationMode;
	}
	public CartItemCard(Context context,BaseItemCard addedItem){
		super(addedItem.getItem(),context);
		setCurrentMsrPrice( addedItem.getCurrentMsrPrice());
		setCurrentMeasure(addedItem.getCurrentMeasure());
		setTotalPrice(addedItem.getTotalPrice());
		setCurrentItemId(addedItem.getCurrentItemId());
		setCurrentQty(addedItem.getCurrentQty());
			
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
	public Button getItemButton() {
		return itemButton;
	}
	public void setItemButton(Button itemButton) {
		this.itemButton = itemButton;
	}
	public LinearLayout getItemEditView() {
		return itemEditView;
	}
	public void setItemEditView(LinearLayout itemEditView) {
		this.itemEditView = itemEditView;
	}
	
	public void setParentView(Context context,ViewGroup container, ArrayList<BaseItemCard> cartItemList,
			BaseItemCard baseItem, CartItemAdapter cartItemAdapter)
	{
		this.cartItemList = cartItemList;
		this.baseItem = baseItem;
		this.cartItemAdapter = cartItemAdapter;
		setUpView(context, container);
	}
	
	public void setCartItemAdapter(CartItemAdapter cartItemAdapter) {
		this.cartItemAdapter = cartItemAdapter;
	}
	public BaseItemCard getBaseItem() {
		return baseItem;
	}
	public void setBaseItem(BaseItemCard baseItem) {
		this.baseItem = baseItem;
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

	

	
	
	

	public void initMeasurePicker(){
		try{
			measurePicker.setMinValue(0);
			measurePicker.setMaxValue(item.getItemList().size()-1);
			measurePicker.setFormatter(new NumberPicker.Formatter() {
				
				@Override
				public String format(int value) {
					// TODO Auto-generated method stub
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
		catch(Exception e){
			e.printStackTrace();
		}
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
		View view;
		if(container.findViewById(R.id.cart_item_view) == null){
			view = lp.inflate(R.layout.cart_item_card, container);
		}
		else{
			view = container;
		}
		this.containerView = view;
		super.setViewLayout(view.getId());
		innerView = containerView.findViewById(R.id.cart_item_view);
		itemNameView = (TextView) containerView.findViewById(R.id.item_name);
		itemImageView = (ImageView) containerView.findViewById(R.id.item_image);
		currentMeasureView = (TextView) containerView.findViewById(R.id.current_measure);
		currentPriceView = (TextView) containerView.findViewById(R.id.current_measure_price);
		quantityView = (TextView)containerView.findViewById(R.id.current_quantity);
		totalPriceView = (TextView)containerView.findViewById(R.id.total_price);
		setMeasurePicker((CustomNumberPicker)containerView.findViewById(R.id.item_measure_picker));
		setQtyPicker((CustomNumberPicker)containerView.findViewById(R.id.item_quantity_picker));
		itemButton = (Button)containerView.findViewById(R.id.item_button);
		ImageButton delete_button = (ImageButton)containerView.findViewById(R.id.delete_cart_item);
        delete_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isAnimationMode()){
					
					Toast.makeText(mContext, "Cart Array Adapter delete", Toast.LENGTH_SHORT).show();
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
				    
					
					//notifyDataSetChanged();
				}
				
				//packlist to be sent for item deleted
			}
		});
		itemButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//getActionButtonOnClick().onClick(null, v);
				Animation animFadeOut = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
		    	 Animation animFadeIn = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
		    	 
		    	 
					if(getItemEditView().getVisibility() == View.GONE){
						getItemEditView().setAnimation(animFadeIn);
					    getItemEditView().setVisibility(View.VISIBLE);
						setActionButtonText("Done");
						
						// Packlist to be sent for item edited
					}
					else{
						getItemEditView().setAnimation(animFadeOut);
				    	getItemEditView().setVisibility(View.GONE);
						setActionButtonText("Edit");
						editPackList();
					}
			}
		});
		itemEditView =(LinearLayout)view.findViewById(R.id.item_edit_view);
		itemEditView.setVisibility(View.GONE);
		updateView();
		
		initMeasurePicker();
		initQtyPicker();
		fetchItemImage(null);
				
		view.setOnTouchListener(new OnTouchListener() {
			
			
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
		
		
		
		
	}
	/**
	 * 
	 */
	protected void delete_from_list() {
		
		TranslateAnimation Anim = new TranslateAnimation(-px, 0, 0, 0);
	 	Anim.setDuration(10);
	 	Anim.setFillAfter(true);
	 	innerView.startAnimation(Anim);
	 	animationMode = false;
	 	getItemButton().setEnabled(true);
	 	
		// TODO Auto-generated method stub
	 	
		cartItemList.remove(getBaseItem());
		if(getBaseItem().isSent()){
			OrderItemDetail itemToDelete = new OrderItemDetail(getCurrentItemId(), getCurrentQty());
			deletePackList(itemToDelete);
		}
		cartItemAdapter.updateCart(cartItemList);
		
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#sendPackList()
	 */
	@Override
	public void sendPackList() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#PackListSuccess(com.shoplite.models.PackList)
	 */
	@Override
	public void PackListSuccess(PackList obj) {
		// TODO Auto-generated method stub
		if(obj.state==DBState.DELETE){
			for(int i = 0 ;i < obj.orderedItems.size() ; i++){
				if(CartGlobals.cartList.contains(obj.orderedItems.get(i)))
					CartGlobals.cartList.remove(obj.orderedItems.get(i));
				if(CartGlobals.recentDeletedItems.contains(obj.orderedItems.get(i)))
					CartGlobals.recentDeletedItems.remove(obj.orderedItems.get(i));
			}
			
		}
		else if (obj.state == DBState.INSERT){
			for(int i = 0 ;i < obj.orderedItems.size() ; i++){
				CartGlobals.cartList.add(obj.orderedItems.get(i));
			}
		}
		else{
			
		}
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#editPackList()
	 */
	@Override
	public void editPackList() {
		// TODO Auto-generated method stub
		if(getBaseItem().isSent()){
			OrderItemDetail itemToDelete = new OrderItemDetail(getCurrentItemId(), getCurrentQty());
			PackList pl = new PackList();
		}
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#deletePackList(com.shoplite.models.OrderItemDetail)
	 */
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		// TODO Auto-generated method stub
		ArrayList<OrderItemDetail> deleteList = new ArrayList<OrderItemDetail>();
		deleteList.add(itemToDelete);
		PackList pl = new PackList();
		pl.state = Constants.DBState.DELETE;
		pl.orderedItems = deleteList;
		CartGlobals.CartServerRequestQueue.add(pl);
		pl.sendPackList(this);
	}
	

	
}
