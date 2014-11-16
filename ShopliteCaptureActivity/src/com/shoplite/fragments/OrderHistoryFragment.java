package com.shoplite.fragments;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shoplite.UI.PreviousOrderAdapter;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Constants.ORDERState;
import com.shoplite.Utils.Globals;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServerConnectionMaker;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.models.PreviousOrder;

import eu.livotov.zxscan.R;

public class OrderHistoryFragment extends Fragment{

		private ListView orderHistoryListView;
		private LinearLayout emptyOrderListView;
		
		private static ArrayList<PreviousOrder> previousOrderLists ;
		private static PreviousOrderAdapter orderListAdapter;
		
		private static ArrayList<PreviousOrder> checkStatusList;
		   
		public ListView getOrderHistoryListView() {
			return orderHistoryListView;
		}
	
		public void setOrderHistoryListView(ListView orderHistoryListView) {
			this.orderHistoryListView = orderHistoryListView;
		}
	
		public LinearLayout getEmptyOrderListView() {
			return emptyOrderListView;
		}
	
		public void setEmptyOrderListView(LinearLayout emptyOrderListView) {
			this.emptyOrderListView = emptyOrderListView;
		}
	
		public static ArrayList<PreviousOrder> getPreviousOrderLists() {
			return previousOrderLists;
		}
	
		public static void setPreviousOrderLists(
				ArrayList<PreviousOrder> previousOrderLists) {
			OrderHistoryFragment.previousOrderLists = previousOrderLists;
		}
	
		public static PreviousOrderAdapter getOrderListAdapter() {
			return orderListAdapter;
		}
	
		public static void setOrderListAdapter(PreviousOrderAdapter orderListAdapter) {
			OrderHistoryFragment.orderListAdapter = orderListAdapter;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.order_history, container, false);
			orderHistoryListView = (ListView) rootView.findViewById(R.id.orderHistory_ListView);
			emptyOrderListView = (LinearLayout) rootView.findViewById(R.id.empty_order_history);
			previousOrderLists = Globals.dbhelper.getAllOrders();
			orderListAdapter = new PreviousOrderAdapter(getActivity(), previousOrderLists);
			
	        return rootView;
	    }
		
		@Override
		public void onActivityCreated (Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);

		    
		}
		
		
		@Override
		public void onResume()
		{
			checkStatusList = new ArrayList<PreviousOrder>();
			if(previousOrderLists!= null){
				for(int i = 0 ; i < previousOrderLists.size() ;i++ ){
					if(previousOrderLists.get(i).getOrderState() != Constants.ORDERState.DELIVERED.ordinal())
					{
						checkStatusList.add(previousOrderLists.get(i));
					}
				}
			}
			if(checkStatusList.size()>0){
				for(int i = 0 ; i < checkStatusList.size();i++){
					OrderStatus orderStatus = new OrderStatus(checkStatusList.get(i));
					orderStatus.getStatus();
				}
			}
			orderHistoryListView.setAdapter(orderListAdapter);
			super.onResume();
			
		}
		
		public class OrderStatus implements ConnectionInterface{

			/* (non-Javadoc)
			 * @see com.shoplite.connection.ConnectionInterface#sendRequest(com.shoplite.connection.ServiceProvider)
			 */
			private PreviousOrder recentOrder;
			private int orderID;
			public OrderStatus(PreviousOrder recentOrder)
			{
				this.orderID = recentOrder.getOrderId();
				this.recentOrder = recentOrder;
			}
			/**
			 * 
			 */
			public void getStatus() {
				// TODO Auto-generated method stub
				
				ServerConnectionMaker.sendRequest(this);
				
			}
			public void updateStatus(int state)
			{
				Globals.dbhelper.updateOrderState(this.orderID, state);
				orderListAdapter.notifyDataSetChanged();
			}
			/**
			 * @param i
			 */
			
			@Override
			public void sendRequest(ServiceProvider serviceProvider) {
				// TODO Auto-generated method stub
								
					serviceProvider.getOrderStatus(this.orderID, new Callback<ORDERState>(){

						@Override
						public void failure(RetrofitError response) {
							// TODO Auto-generated method stub
							ServerConnectionMaker.recieveResponse(null);
							Log.e("Order Status failure", response.getMessage().toString());
						}

						@Override
						public void success(ORDERState state, Response response) {
							// TODO Auto-generated method stub
							ServerConnectionMaker.recieveResponse(response);
							updateStatus(state.ordinal());
							Log.e("Order Status Success", response.toString());
							
							}
					});
				}
				
			}
			
			
		
}
