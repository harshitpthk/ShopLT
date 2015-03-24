package com.shoplite.fragments;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
		private SwipeRefreshLayout swipeLayout;
		
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
			swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.order_swipe_refresh_layout);
	        swipeLayout.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					checkOrderStatuses();
				}
			});
	        swipeLayout.setColorSchemeColors(R.color.dark_app_color);
			
	        return rootView;
	        
	        
	    }
	     
	     public void checkOrderStatuses(){
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
				else{
					swipeLayout.setRefreshing(false);
				}
				orderHistoryListView.setAdapter(orderListAdapter);
	     }
	    
	    
		
		@Override
		public void onActivityCreated (Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);

		    
		}
		
		
		@Override
		public void onResume()
		{
			checkOrderStatuses();
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
				
				ServerConnectionMaker.sendRequest(this);
				
			}
			public void updateStatus(int state)
			{
				Globals.dbhelper.updateOrderState(this.orderID, state);
				previousOrderLists = Globals.dbhelper.getAllOrders();
				orderListAdapter.updateOrderLists(previousOrderLists);
			}
			/**
			 * @param i
			 */
			
			@Override
			public void sendRequest(ServiceProvider serviceProvider) {
								
					serviceProvider.getOrderStatus(this.orderID, new Callback<ORDERState>(){

						@Override
						public void failure(RetrofitError response) {
							ServerConnectionMaker.recieveResponse(null);
							Log.e("Order Status failure", response.getMessage().toString());
							swipeLayout.setRefreshing(false);
							//Toast.makeText(getActivity(), "Network Problem Occured, Please Retry.", Toast.LENGTH_LONG).show();
						}

						@Override
						public void success(ORDERState state, Response response) {
							ServerConnectionMaker.recieveResponse(response);
							updateStatus(state.ordinal());
							Log.e("Order Status Success", response.toString());
							swipeLayout.setRefreshing(false);
							//Toast.makeText(getActivity(), "Order Statuses Refreshed.", Toast.LENGTH_LONG).show();

							}
					});
				}
				
			}
			
			
		
}
