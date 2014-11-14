package com.shoplite.UI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.shoplite.models.Product;

public abstract class BaseItemCard {

	protected Context mContext;
	protected int viewLayout = -1;
	protected Product item;
	protected OnClickActionButtonListener mOnClickActionButtonListener;
	public interface OnClickActionButtonListener {
        public void onClick(Product itemCategory, View view);
    }
		
	//constructors
	public BaseItemCard()
	{
		
	}
	
	public BaseItemCard(Context context,Product item){
		setItem(item);
		setmContext(context);
	}
	
	//getters & setters
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
	
	
	public Product getItem() {
		return item;
	}
	public void setItem(Product item) {
		this.item = item;
	}
	
	public OnClickActionButtonListener getOnClickActionButtonListener() {
		return mOnClickActionButtonListener;
	}
	public void setOnClickActionButtonListener(
			OnClickActionButtonListener mOnClickActionButtonListener) {
		this.mOnClickActionButtonListener = mOnClickActionButtonListener;
	}
			
	/*
	 *Interface methods to be implemented in child classes 
	 * 
	 */
	abstract public void setActionButtonOnClick(OnClickActionButtonListener actionButtonListener);
	abstract public OnClickActionButtonListener getActionButtonOnClick();
	abstract public void fetchItemImage(String url);
	abstract public void setUpView(Context context , ViewGroup container);
	abstract public void setActionButtonText(String text);
	abstract public void updateView();
	
	
}
