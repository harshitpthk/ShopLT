package com.shoplite.UI;

import java.util.ArrayList;

import com.shoplite.models.Item;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseItemCard {

	protected Context mContext;
	protected int viewLayout = -1;
	
	protected ItemCategory item;
		
	protected OnClickActionButtonListener mOnClickActionButtonListener;
	protected int currentItemId;
	
	protected int currentQty;
	protected String currentMeasure;
	protected Double currentMsrPrice;
	protected Double totalPrice;
	protected int quantity;
	protected String imageUrl;
	
	
	public interface OnClickActionButtonListener {
        public void onClick(ItemCategory itemCategory, View view);
    }
	
	
	
	/*
	 * 
	 * constructor
	 */
	
	public BaseItemCard(ItemCategory item,Context context){
		setItem(item);
		//setQuantity(item.getItemList().get(0).getQuantity());
		setQuantity(5);
		//setCurrentMsrPrice( item.getItemList().get(0).getPrice());
		//setCurrentMeasure(item.getItemList().get(0).getName());
		//setTotalPrice(item.getItemList().get(0).getPrice());
		
		setCurrentMsrPrice(12.50);
		setCurrentMeasure("5gm");
		setTotalPrice(12.50);
		setCurrentQty(1);
		setmContext(context);
		setImageUrl(null);    //null to be replace by the Image URL
	}
	
	/*
	 * getters and setters
	 * 
	 * 
	 */
	public Context getmContext() {
		return mContext;
	}
	
		
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public int getViewLayout() {
		return viewLayout;
	}

	public void setViewLayout(int viewLayout) {
		this.viewLayout = viewLayout;
	}
	
	public int getCurrentItemId() {
		return currentItemId;
	}

	public void setCurrentItemId(int currentItemId) {
		this.currentItemId = currentItemId;
	}
	public ItemCategory getItem() {
		return item;
	}
	public void setItem(ItemCategory item) {
		this.item = item;
	}
	
	public OnClickActionButtonListener getOnClickActionButtonListener() {
		return mOnClickActionButtonListener;
	}
	public void setOnClickActionButtonListener(
			OnClickActionButtonListener mOnClickActionButtonListener) {
		this.mOnClickActionButtonListener = mOnClickActionButtonListener;
	}
	
	
	
	
	public int getCurrentQty() {
		return currentQty;
	}
	
	
	public void setCurrentQty(int currentQty) {
		this.currentQty = currentQty;
	}
	
	public String getCurrentMeasure() {
		return currentMeasure;
	}
	
	public void setCurrentMeasure(String currentMeasure) {
		this.currentMeasure = currentMeasure;
	}
	
	public Double getCurrentMsrPrice() {
		return currentMsrPrice;
	}
	
	public void setCurrentMsrPrice(Double currentMsrPrice) {
		this.currentMsrPrice = currentMsrPrice;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	/* 
	 * Edit Item Methods
	 * 
	 * 
	 */
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public static int countNotSent(ArrayList<BaseItemCard> ItemList){
		int count = 0;
		for(int i = 0 ; i < ItemList.size();i++){
			if(!ItemList.get(i).getItem().isSent()){
				count++;
			}
		}
		return count;
	}
	
	public static void setSentList(ArrayList<BaseItemCard> item_order_list) {
		// TODO Auto-generated method stub
		int i;
		for(i = 0 ; i < item_order_list.size() ;i++){
			if(!item_order_list.get(i).getItem().isSent()){
				item_order_list.get(i).getItem().setSent(true);
			}
		}
	}
	
	public static ArrayList<OrderItemDetail> getToSendList(ArrayList<BaseItemCard> item_order_list)
	{
		int i;
		ArrayList<OrderItemDetail> toSendList = new ArrayList<OrderItemDetail>();
		for(i = 0 ; i < item_order_list.size() ; i++){
			if(!item_order_list.get(i).getItem().isSent()){
				OrderItemDetail itemToOrder = new OrderItemDetail(item_order_list.get(i).getCurrentItemId(),item_order_list.get(i).getCurrentQty());
				toSendList.add(itemToOrder);
			}
		}
		return toSendList;
	}
	
		
	/*
	 *Interface methods to be implemented in respected classes 
	 * 
	 */
		
	abstract public void setActionButtonOnClick(OnClickActionButtonListener actionButtonListener);
	abstract public OnClickActionButtonListener getActionButtonOnClick();
	abstract public void fetchItemImage(String url);
	abstract public void setUpView(Context context , ViewGroup container);
	abstract public void setActionButtonText(String text);
	abstract public void updateView();
	
	
}
