/**
 * 
 */
package com.homelybuysapp.UI;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.homelybuysapp.Utils.Constants;
import com.homelybuysapp.activities.ItemsDisplayActivity;
import com.homelybuysapp.connection.ConnectionInterface;
import com.homelybuysapp.connection.ServerConnectionMaker;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PreviousOrder;
import com.homelybuysapp.models.Product;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class PreviousOrderCard implements ConnectionInterface{
	protected int innerView;
	protected TextView previousOrderIdView;
	protected TextView dateView;
	protected TextView totalItemsView;
	protected TextView amountView;
	protected ImageView orderStatusView;
	protected PreviousOrder previousOrder;
	protected ArrayList<PreviousOrder> previosOrdersList;
	protected PreviousOrderAdapter previousOrderAdapter;
	protected Context context;
	
	public PreviousOrderCard(PreviousOrder previosOrder)
	{
		this.previousOrder = previosOrder;
	}
	public void setParentView(Context context, ViewGroup container, ArrayList<PreviousOrder> previousOrdersList
			, PreviousOrderAdapter previousOrderAdapter)
	{
		this.context = context;
	
		this.previosOrdersList = previousOrdersList;
		this.previousOrderAdapter = previousOrderAdapter;
		setUpView(context,container);
	}
	private void fetchOrderItems() {
		// TODO Auto-generated method stub
		Controls.show_loading_dialog(context,"Fetching Order Details");
		ServerConnectionMaker.sendRequest(this);
		
	}
	
	private void setUpView(final Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		LayoutInflater lp = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lp.inflate(R.layout.previous_order_card, root);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchOrderItems();
			}

			

			
		});
		setPreviousOrderIdView((TextView)view.findViewById(R.id.order_number_view));
		setAmountView((TextView)view.findViewById(R.id.order_amount_view));
		setDateView((TextView)view.findViewById(R.id.order_date_view));
		setTotalItemsView((TextView)view.findViewById(R.id.order_total_items_view));
		setOrderStatusView((ImageView)view.findViewById(R.id.order_status_view));
		updateView();
	}
	public void updateView()
	{
		getPreviousOrderIdView().setText("#"+Integer.toString(getPreviosOrder().getOrderId()));
		getAmountView().setText(Double.toString(getPreviosOrder().getOrderAmount())+
				context.getResources().getString(R.string.currency));
		getDateView().setText(getPreviosOrder().getOrderDate());
		if(getPreviosOrder().getOrderTotalItems()!=1){
			getTotalItemsView().setText(Integer.toString(getPreviosOrder().getOrderTotalItems())+
					" " +context.getResources().getString(R.string.items));
		}
		else{
			getTotalItemsView().setText(Integer.toString(getPreviosOrder().getOrderTotalItems())+
					" "+context.getResources().getString(R.string.item));
	
		}
		int orderState = getPreviosOrder().getOrderState();
		
		if(orderState == Constants.ORDERState.PACKING.ordinal()){
			getOrderStatusView().setImageDrawable(context.getResources().getDrawable(R.drawable.packing));
			
		}
		else if(orderState == Constants.ORDERState.PACKED.ordinal()){
			getOrderStatusView().setImageDrawable(context.getResources().getDrawable(R.drawable.packed));
			
		}
		else if( orderState == Constants.ORDERState.FORDELIVERY.ordinal()){
			getOrderStatusView().setImageDrawable(context.getResources().getDrawable(R.drawable.intransit));
			
		}
		else{
			getOrderStatusView().setImageDrawable(context.getResources().getDrawable(R.drawable.delivered));
					
		}
		
		
	}
	public int getInnerView() {
		return innerView;
	}
	public void setInnerView(int innerView) {
		this.innerView = innerView;
	}
	public TextView getPreviousOrderIdView() {
		return previousOrderIdView;
	}
	public void setPreviousOrderIdView(TextView previousOrderIdView) {
		this.previousOrderIdView = previousOrderIdView;
	}
	public TextView getDateView() {
		return dateView;
	}
	public void setDateView(TextView dateView) {
		this.dateView = dateView;
	}
	public TextView getTotalItemsView() {
		return totalItemsView;
	}
	public void setTotalItemsView(TextView totalItemsView) {
		this.totalItemsView = totalItemsView;
	}
	public TextView getAmountView() {
		return amountView;
	}
	public void setAmountView(TextView amountView) {
		this.amountView = amountView;
	}
	public ImageView getOrderStatusView() {
		return orderStatusView;
	}
	public void setOrderStatusView(ImageView orderStatusView) {
		this.orderStatusView = orderStatusView;
	}
	public PreviousOrder getPreviosOrder() {
		return previousOrder;
	}
	public void setPreviosOrder(PreviousOrder previosOrder) {
		this.previousOrder = previosOrder;
	}
	public ArrayList<PreviousOrder> getPreviosOrdersList() {
		return previosOrdersList;
	}
	public void setPreviosOrdersList(ArrayList<PreviousOrder> previosOrdersList) {
		this.previosOrdersList = previosOrdersList;
	}
	public PreviousOrderAdapter getPreviousOrderAdapter() {
		return previousOrderAdapter;
	}
	public void setPreviousOrderAdapter(PreviousOrderAdapter previousOrderAdapter) {
		this.previousOrderAdapter = previousOrderAdapter;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	/* (non-Javadoc)
	 * @see com.homelybuysapp.connection.ConnectionInterface#sendRequest(com.homelybuysapp.connection.ServiceProvider)
	 */
	@Override
	public void sendRequest(ServiceProvider serviceProvider) {
		// TODO Auto-generated method stub
		serviceProvider.getOrderDetails(getPreviosOrder().getOrderId(),
				new Callback<ArrayList<OrderItemDetail>>(){

					@Override
					public void failure(RetrofitError response) {
						ServerConnectionMaker.recieveResponse(null);
						//Log.e("order items fetch failure",response.getMessage());
						orderedProductFetchFailure();
					}

					

					@Override
					public void success(ArrayList<OrderItemDetail> productList,
							Response response) {
						ServerConnectionMaker.recieveResponse(response);
						Log.e("order items fetch success",response.toString());
						orderedProductsFetchSuccess(productList);
					}

					
			
			
		});
	}
	private void orderedProductFetchFailure() {
		// TODO Auto-generated method stub
		Toast.makeText(context, context.getString(R.string.order_detail_fetch_failure),Toast.LENGTH_LONG).show();
	}
	public void orderedProductsFetchSuccess(
			ArrayList<OrderItemDetail> productList) {
		// TODO Auto-generated method stub
		
		ArrayList<Product> orderedProductList= new ArrayList<Product>();
		for(int i = 0 ; i < productList.size() ;i++){
			
			Product product = new Product(productList.get(i).getProductId(),
					productList.get(i).getProductName());
			product.setCurrentItemId(productList.get(i).getVarianceId());
			product.setCurrentMeasure(productList.get(i).getVarianceName());
			product.setCurrentQty(productList.get(i).getQuantity());
			product.setCurrentMsrPrice(productList.get(i).getPrice());
			product.setTotalPrice(product.getCurrentMsrPrice()*product.getCurrentQty());
			product.setId(productList.get(i).getProductId());
			orderedProductList.add(product);
		}
		Controls.dismiss_progress_dialog();

		//Toast.makeText(context, "onclick saved list", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(context,ItemsDisplayActivity.class);
		i.putExtra("ListName", Integer.toString(productList.get(0).getOrderId()));
		i.putExtra("instantiator","ordersHistory");
		Gson gson = new Gson();
		String jsonString = gson.toJson(orderedProductList);
		i.putExtra("ItemList", jsonString);
		context.startActivity(i);
	}
}
