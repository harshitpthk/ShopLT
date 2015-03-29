/**
 * 
 */
package com.homelybuysapp.UI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homelybuysapp.models.PreviousOrder;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class PreviousOrderAdapter extends BaseAdapter {

	private ArrayList<PreviousOrder> orderList;
	private Context context;
	
	public PreviousOrderAdapter(Context context, ArrayList<PreviousOrder> orderList)
	{
		this.context = context;
		this.orderList = orderList;
	}
	public  void updateOrderLists(ArrayList<PreviousOrder> orderList)
	{
		this.orderList = orderList;
		notifyDataSetChanged();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return !orderList.isEmpty() ? orderList.size(): 0;

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return orderList.get(position);

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		// TODO Auto-generated method stub
		PreviousOrder previousOrder = orderList.get(position);
		PreviousOrderCard previousOrderCard = new PreviousOrderCard(previousOrder);
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.save_list_container, root,false);
			previousOrderCard.setParentView(context, (ViewGroup) convertView, orderList, this);
			
		}
		return convertView;
	}

}
