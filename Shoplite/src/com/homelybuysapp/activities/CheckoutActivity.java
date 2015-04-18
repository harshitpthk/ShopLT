	package com.homelybuysapp.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.Utils.Constants;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.Utils.Constants.DBState;
import com.homelybuysapp.fragments.OrderHistoryFragment;
import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.interfaces.PackListInterface;
import com.homelybuysapp.interfaces.SubmitOrderInterface;
import com.homelybuysapp.models.Address;
import com.homelybuysapp.models.OrderDetail;
import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackList;
import com.homelybuysapp.models.PackProducts;
import com.homelybuysapp.models.Product;
import com.homelybuysapp.models.SubmitOrderDetails;

public class CheckoutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_app_color));
        }
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
//		if (id == R.id.action_settings) {
//			return true;
//		}
		if (id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements ControlsInterface,SubmitOrderInterface,PackListInterface  {

		
		
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
			totalPriceView.setText(	Double.toString((Math.round((Globals.cartTotalPrice - ((Globals.cartTotalPrice*15)/100))*100.0/100.0))) + getResources().getString(R.string.currency) + " 15% Off");
			totalItemsOrderedView = (TextView) rootView.findViewById(R.id.order_total_items);
			totalItemsOrderedView.setText(Integer.toString(Globals.item_order_list.size()) + " Items");
			
			homeDeliveryRadio = (RadioButton) rootView.findViewById(R.id.radio_home_delivery);
			//shopPickupRadio = (RadioButton) rootView.findViewById(R.id.radio_shop_pickup);
			
			addressContainer = (LinearLayout) rootView.findViewById(R.id.address_details_view);
			shopAddress = (TextView) rootView.findViewById(R.id.shop_address_details);
			homeAddress = (LinearLayout) rootView.findViewById(R.id.home_address_details);
			primaryHomeAddress = (EditText) rootView.findViewById(R.id.home_address_primary);
			primaryHomeAddress.setText(Globals.deliveryAddress.getAddressString());
			primaryHomeAddress.setEnabled(false);
			
			//second Version
			//payOnlineRadio = (RadioButton) rootView.findViewById(R.id.pay_by_card);
			payCashRadio = (RadioButton) rootView.findViewById(R.id.pay_by_cash);
			
			shopAddress.setText(Globals.connectedShop.getName());
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
			//second version
//			shopPickupRadio.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					addressContainer.setVisibility(View.VISIBLE);
//					homeAddress.setVisibility(View.GONE);
//					shopAddress.setVisibility(View.VISIBLE);
//					isOrderTakeAway = true;
//				}
//			});
			
			
			
//			payOnlineRadio.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					isPayOnline = true;
//					confirmOrderButton.setVisibility(View.VISIBLE);
//					
//				}
//			});
			
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
			
			homeDeliveryRadio.setChecked(true);
			homeDeliveryRadio.callOnClick();
			payCashRadio.setChecked(true);
			payCashRadio.callOnClick();
			return rootView;
		}
		
		public void finalizeOrder(){
			
			Controls.show_alert_dialog(this, getActivity(), R.layout.confirm_order_dialog, 220);
			
		}
		

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.ControlsInterface#positive_button_alert_method()
		 */
		@Override
		public void positive_button_alert_method() {
			// TODO Auto-generated method stub
			alertDialog.dismiss();
			sendPackList();
			Controls.show_loading_dialog(getActivity(), "Sending Products Info..");

			
		}

		/**
		 * @param submit_order
		 */
		private void submitOrder(SubmitOrderDetails submit_order) {
			// TODO Auto-generated method stub
			submit_order.submitOrder(this);
		}
		
		
		@Override
		public void submitOrderSuccess(Integer orderID) {
			Toast.makeText(getActivity(), "Order Submitted Successfully,Check Order Status for more info.", Toast.LENGTH_LONG).show();
			Globals.dbhelper.storeOrder(orderID, Globals.cartTotalPrice - ((Globals.cartTotalPrice*15)/100)*100.0/100.0, Globals.item_order_list.size(),Constants.ORDERState.PACKING.ordinal());
			OrderHistoryFragment.getPreviousOrderLists().clear();
			OrderHistoryFragment.getPreviousOrderLists().addAll(Globals.dbhelper.getAllOrders());
			OrderHistoryFragment.getOrderListAdapter().updateOrderLists(Globals.dbhelper.getAllOrders());
			Globals.resetCartData();
			
			if(Globals.usedPreviousAddress != true ){
				Globals.usedPreviousAddress = true;
				boolean deliveryAddressStored = Globals.dbhelper.storeAddress(Globals.deliveryAddress);
			}
			
			getActivity().finish();
		}

	
		
		@Override
		public void negative_button_alert_method() {
			alertDialog.dismiss();
		}

		@Override
		public void save_alert_dialog(AlertDialog alertDialog) {
			this.alertDialog = alertDialog;
		}

		
		@Override
		public void neutral_button_alert_method() {
			
		}

		@Override
		public void submitOrderFailure() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "An error has occured, please try again", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void sendPackList() {
			
			
			PackList pl = new PackList();
			pl.pckProd = new PackProducts( DBState.INSERT, Product.getToSendList(Globals.item_order_list));
			
		
			//Product.setSentList(Globals.item_order_list);
			
			pl.sendPackList(this);
			
//			if(CartGlobals.CartServerRequestQueue.size() == 0){
//				CartGlobals.CartServerRequestQueue.add(pl);
//				
//			}
//			else{
//				CartGlobals.CartServerRequestQueue.add(pl);
//			}
//				
		
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.PackListInterface#PackListSuccess(com.homelybuysapp.models.PackList)
		 */
		@Override
		public void PackListSuccess(PackList obj) {
			// TODO Auto-generated method stub

			SubmitOrderDetails submitOrder = new SubmitOrderDetails();
			OrderDetail oDetail = new OrderDetail();
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
				oDetail.setAmount(Globals.cartTotalPrice);
				oDetail.setAddress("takeAway");
				oDetail.setLatitude(Globals.connectedShop.getLocation().getLatitude());
				oDetail.setLongitude(Globals.connectedShop.getLocation().getLongitude());
				if(isPayOnline){
					oDetail.setState(Constants.ORDERStatus.FORDELIVERY);
					oDetail.setRefNumber("xyz");
				}
				else{
					oDetail.setState(Constants.ORDERStatus.FORPAYMENT);
					}
			}
			else{
				oDetail.setAmount(Globals.cartTotalPrice);
				oDetail.setAddress(primaryHomeAddress.getText().toString());
				oDetail.setLatitude(Globals.deliveryAddress.getDeliveryLocation().getLatitude());
				oDetail.setLongitude(Globals.deliveryAddress.getDeliveryLocation().getLongitude());
				if(isPayOnline){
					oDetail.setState(Constants.ORDERStatus.FORHOMEDELIVERY);
					oDetail.setRefNumber("xyz");
				}
				else{
					oDetail.setState(Constants.ORDERStatus.FORHOMEDELIVERYPAYMENT);
				}
			}
			submitOrder.setoDetail(oDetail);
			submitOrder(submitOrder);
			
			Controls.show_loading_dialog(getActivity(), getResources().getString(R.string.confirming_order));
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.PackListInterface#editPackList()
		 */
		@Override
		public void editPackList() {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.homelybuysapp.interfaces.PackListInterface#deletePackList(com.homelybuysapp.models.OrderItemDetail)
		 */
		@Override
		public void deletePackList(OrderItemDetail itemToDelete) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void packListFailure() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "An error has occured, please try again.", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
}
