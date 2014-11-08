package com.shoplite.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.interfaces.SubmitOrderInterface;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;
import com.shoplite.models.SubmitOrderDetails;
import com.shoplite.models.SubmitOrderStar;

import eu.livotov.zxscan.R;

public class CheckoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
		// Inflate the menu; this adds items to the action bar if it is present.
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
			primaryHomeAddress.setText(Globals.delivery_address);
			
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
			if(isOrderTakeAway){
				submitOrder.setAmount(Globals.cartTotalPrice);
				submitOrder.setUsernameNumber(Globals.dbhelper.getItem("phoneNo"));
				submitOrder.setAddress("takeAway");
				submitOrder.setLatitude(Globals.connected_shop_location.getLatitude());
				submitOrder.setLongitude(Globals.connected_shop_location.getLongitude());
				if(isPayOnline){
					submitOrder.setState(Constants.ORDERState.FORDELIVERY);
					submitOrder.setRefNumber("xyz");
				}
				else{
					submitOrder.setState(Constants.ORDERState.FORPAYMENT);
					}
			}
			else{
				submitOrder.setAmount(Globals.cartTotalPrice);
				submitOrder.setUsernameNumber(Globals.dbhelper.getItem("phoneNo"));
				submitOrder.setAddress(primaryHomeAddress.getText().toString());
				submitOrder.setLatitude(Globals.delivery_location.getLatitude());
				submitOrder.setLongitude(Globals.delivery_location.getLongitude());
				if(isPayOnline){
					submitOrder.setState(Constants.ORDERState.FORHOMEDELIVERY);
					submitOrder.setRefNumber("xyz");
				}
				else{
					submitOrder.setState(Constants.ORDERState.FORHOMEDELIVERYPAYMENT);
						}
			}
			submitOrderToPlanet(submitOrder);
			alertDialog.dismiss();
			Controls.show_loading_dialog(getActivity(), getResources().getString(R.string.confirming_order));
		}

		/**
		 * @param submitOrder
		 */
		private void submitOrderToPlanet(SubmitOrderDetails submitOrder) {
			// TODO Auto-generated method stub
			submitOrder.submitOrderToPlanet(this);
		}
		
		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.SubmitOrderInterface#submitToPlanetSuccess()
		 */
		@Override
		public void submitToPlanetSuccess(Integer orderID) {
			// TODO Auto-generated method stub
			SubmitOrderStar submitOrderStar = new SubmitOrderStar();
			submitOrderStar.submitOrderToStar(this,orderID,Globals.connected_shop_id );
			Toast.makeText(getActivity(), "Submit To Planet Success" + orderID.toString(), Toast.LENGTH_SHORT).show();
		}

		/* (non-Javadoc)
		 * @see com.shoplite.interfaces.SubmitOrderInterface#submitToStarSuccess()
		 */
		@Override
		public void submitToStarSuccess() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Submit To Star Success" ,Toast.LENGTH_SHORT).show();
			
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
