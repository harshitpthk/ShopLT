package com.shoplite.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shoplite.UI.Controls;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Globals;
import com.shoplite.fragments.OrderHistoryFragment;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.interfaces.SubmitOrderInterface;
import com.shoplite.models.Address;
import com.shoplite.models.SubmitOrderDetails;

import eu.livotov.zxscan.R;

public class CheckoutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds products to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements ControlsInterface,SubmitOrderInterface  {

		
		
		private TextView totalPriceView;
		private TextView totalItemsOrderedView;
		
		private LinearLayout addressContainer;
		private TextView shopAddress;
		private LinearLayout homeAddress;
		private EditText  primaryHomeAddress;
		private RadioButton shopPickupRadio;
		private RadioButton homeDeliveryRadio;
		
		private RadioButton payOnlineRadio;
		private RadioButton payCashRadio;
		
		private Button confirmOrderButton;
		
		boolean isOrderTakeAway;
		boolean isPayOnline;
		private AlertDialog alertDialog;
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_checkout,container, false);
			
			totalPriceView = (TextView) rootView.findViewById(R.id.order_total_price);
			totalPriceView.setText(Globals.cartTotalPrice.toString() + getResources().getString(R.string.currency));
			totalItemsOrderedView = (TextView) rootView.findViewById(R.id.order_total_items);
			totalItemsOrderedView.setText(Integer.toString(Globals.item_order_list.size()));
			
			homeDeliveryRadio = (RadioButton) rootView.findViewById(R.id.radio_home_delivery);
			shopPickupRadio = (RadioButton) rootView.findViewById(R.id.radio_shop_pickup);
			
			addressContainer = (LinearLayout) rootView.findViewById(R.id.address_details_view);
			shopAddress = (TextView) rootView.findViewById(R.id.shop_address_details);
			homeAddress = (LinearLayout) rootView.findViewById(R.id.home_address_details);
			primaryHomeAddress = (EditText) rootView.findViewById(R.id.home_address_primary);
			primaryHomeAddress.setText(Globals.deliveryAddress.getAddressString());
			
			payOnlineRadio = (RadioButton) rootView.findViewById(R.id.pay_by_card);
			payCashRadio = (RadioButton) rootView.findViewById(R.id.pay_by_cash);
			
			
			confirmOrderButton = (Button) rootView.findViewById(R.id.confirm_order_button);
			
			homeDeliveryRadio.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addressContainer.setVisibility(View.VISIBLE);
					homeAddress.setVisibility(View.VISIBLE);
					
					shopAddress.setVisibility(View.GONE);
					isOrderTakeAway = false;
				}
			});
			shopPickupRadio.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					addressContainer.setVisibility(View.VISIBLE);
					homeAddress.setVisibility(View.GONE);
					shopAddress.setVisibility(View.VISIBLE);
					isOrderTakeAway = true;
				}
			});
			
			payOnlineRadio.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isPayOnline = true;
					confirmOrderButton.setVisibility(View.VISIBLE);
					
				}
			});
			
			payCashRadio.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isPayOnline = false;
					if(primaryHomeAddress.getText().toString().length()> 0 )
						confirmOrderButton.setVisibility(View.VISIBLE);
					
				}
			});
			
			confirmOrderButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(primaryHomeAddress.getText().toString().length()> 0 )
						finalizeOrder();
					else{
						primaryHomeAddress.setError(getResources().getText(R.string.error_field_required));
					}
				}
			});
			
			return rootView;
		}
		
		public void finalizeOrder(){
			
			Controls.show_alert_dialog(this, getActivity(), R.layout.confirm_order_dialog, 200);
			
		}
		

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#positive_button_alert_method()
		 */
		@Override
		public void positive_button_alert_method() {
			// TODO Auto-generated method stub
			
			SubmitOrderDetails submitOrder = new SubmitOrderDetails();
			
			Address lastAddress = Globals.deliveryAddress;
			
			/* try {
				InternalStorage.writeObject(getActivity(), "lastAddress", lastAddress);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				Address lastadd = (Address) InternalStorage.readObject(getActivity(), "lastAddress");
				Toast.makeText(getActivity(), lastadd.getAddressString(), Toast.LENGTH_LONG).show();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 */

			
			// Storing last address data into shared preference
			String serializedData = lastAddress.serialize();
			SharedPreferences preferencesReader = Globals.ApplicationContext.getSharedPreferences(Globals.PREFS_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferencesReader.edit();
			editor.putString(Globals.PREFS_KEY, serializedData);
			editor.commit();
			 
			if(isOrderTakeAway){
				submitOrder.setAmount(Globals.cartTotalPrice);
				submitOrder.setAddress("takeAway");
				submitOrder.setLatitude(Globals.connectedShop.getLocation().getLatitude());
				submitOrder.setLongitude(Globals.connectedShop.getLocation().getLongitude());
				if(isPayOnline){
					submitOrder.setState(Constants.ORDERStatus.FORDELIVERY);
					submitOrder.setRefNumber("xyz");
				}
				else{
					submitOrder.setState(Constants.ORDERStatus.FORPAYMENT);
					}
			}
			else{
				submitOrder.setAmount(Globals.cartTotalPrice);
				submitOrder.setAddress(primaryHomeAddress.getText().toString());
				submitOrder.setLatitude(Globals.deliveryAddress.getDeliveryLocation().getLatitude());
				submitOrder.setLongitude(Globals.deliveryAddress.getDeliveryLocation().getLongitude());
				if(isPayOnline){
					submitOrder.setState(Constants.ORDERStatus.FORHOMEDELIVERY);
					submitOrder.setRefNumber("xyz");
				}
				else{
					submitOrder.setState(Constants.ORDERStatus.FORHOMEDELIVERYPAYMENT);
						}
			}
			
			submitOrder(submitOrder);
			alertDialog.dismiss();
			Controls.show_loading_dialog(getActivity(), getResources().getString(R.string.confirming_order));
		}

		/**
		 * @param submit_order
		 */
		private void submitOrder(SubmitOrderDetails submit_order) {
			// TODO Auto-generated method stub
			submit_order.submitOrder(this);
		}
		
		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.SubmitOrderInterface#submitToPlanetSuccess()
		 */
		@Override
		public void submitOrderSuccess(Integer orderID) {
			Toast.makeText(getActivity(), "Order Submitted Successfully,Check Order Status for more info.", Toast.LENGTH_LONG).show();
			Globals.dbhelper.storeOrder(orderID, Globals.cartTotalPrice, Globals.item_order_list.size(),Constants.ORDERState.PACKING.ordinal());
			OrderHistoryFragment.getPreviousOrderLists().clear();
			OrderHistoryFragment.getPreviousOrderLists().addAll(Globals.dbhelper.getAllOrders());
			OrderHistoryFragment.getOrderListAdapter().updateOrderLists(Globals.dbhelper.getAllOrders());
			Globals.resetCartData();
			boolean deliveryAddressStored = Globals.dbhelper.storeAddress(Globals.deliveryAddress);
			Toast.makeText(getActivity(), Boolean.toString(deliveryAddressStored), Toast.LENGTH_LONG).show();
			getActivity().finish();
		}

	
		/**
		 * 
		 */
		

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#negative_button_alert_method()
		 */
		@Override
		public void negative_button_alert_method() {
			alertDialog.dismiss();
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#save_alert_dialog(android.app.AlertDialog)
		 */
		@Override
		public void save_alert_dialog(AlertDialog alertDialog) {
			this.alertDialog = alertDialog;
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.ControlsInterface#neutral_button_alert_method()
		 */
		@Override
		public void neutral_button_alert_method() {
			
		}

		
		
	}
	
}
