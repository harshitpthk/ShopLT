/**
 * 
 */
package com.shoplite.fragments;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shoplite.UI.PlacesSearchAdapter;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.location;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.interfaces.LocationInterface;
import com.shoplite.interfaces.MapInterface;
import com.shoplite.interfaces.ShopInterface;
import com.shoplite.models.Location;
import com.shoplite.models.PlacePrediction;
import com.shoplite.models.Shop;

import eu.livotov.zxscan.R;

/**
 * @author I300291
 *
 */
public class MapFragment extends BaseMapfragment implements ShopInterface,OnMarkerClickListener,LocationInterface{
	static View  rootView;
	private TextView shopDetailHeading;
	private LinearLayout shopDetailsView;
	private TextView shopDetailDescription;
    private Button startShop;
    private LinearLayout mapDialogView;
    private LinearLayout deliveryAddressView;
    private String addressText ;
    private TextView secondaryAddress;
    private Marker deliveryMarker;
    private EditText deliveryAddressInput;
    private Menu MenuReference;
	private Circle circle;
	public static  SearchView shopSearchView = null;
    private boolean animationFlipClockWise;
    public static GoogleMap mMap;									//google map
	public static SupportMapFragment mMapFragment;				//fragment storing google map
	public static boolean mapVisible = true;						// initial visibility set to false of the map
	public static MapController  mMapController;
	public static MapView mMapView;
	private Button setDeliveryLocation;
	private ImageButton userAddresses;
	private FrameLayout map_container;
	MapInterface mCallback = null;
	ArrayList<com.shoplite.models.Address> userAddress = Globals.dbhelper.getStoreAddress();
	public static ArrayList<Marker> markerList = new ArrayList<Marker>();
	private static PlacesSearchAdapter searchAdapter;	
	
	public static SimpleCursorAdapter  suggestionAdapter;
    private OnCameraChangeListener onCameraChange = new OnCameraChangeListener() {
		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
//			if(animationFlipClockWise){
//				ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.anim.flip_anti_clockwise); 
//				anim.setTarget(mapDialogView);
//				anim.setDuration(500);
//				anim.addListener(new AnimatorListener() {
//					@Override
//					public void onAnimationStart(Animator animation) {
//						// TODO Auto-generated method stub
//						shopDetailsView.setRotationX(0);
//						deliveryAddressView.setRotationX(0);
//						deliveryAddressView.setVisibility(View.VISIBLE);
//						shopDetailsView.setVisibility(View.GONE);
//						
//						animationFlipClockWise = false;
//				
//					}
//					@Override
//					public void onAnimationRepeat(Animator animation) {
//						// TODO Auto-generated method stub
//					}
//					@Override
//					public void onAnimationEnd(Animator animation) {
//						// TODO Auto-generated method stub
//						
//							}
//					@Override
//					public void onAnimationCancel(Animator animation) {
//						// TODO Auto-generated method stub
//					}
//				});
//				anim.start();
//			}
//			else{
//				
//			}
			mMap.clear();
			addressText = null;
			Geocoder geocoder =
	                new Geocoder(getActivity(), Locale.getDefault());
			List<Address> addresses = null;
			try {
	            /*
	             * Return 1 address.
	             */
	            addresses = geocoder.getFromLocation(position.target.latitude,
	            		position.target.longitude, 1);
	        } catch (IOException e1) {
	        	Log.e("LocationSampleActivity",
	                "IO Exception in getFromLocation()");
	        	e1.printStackTrace();
	        		
	        } catch (IllegalArgumentException e2) {
	            // Error message to post in the log
	            String errorString = "Illegal arguments " +
	                    Double.toString(position.target.latitude) +
	                    " , " +
	                    Double.toString(position.target.longitude) +
	                    " passed to address service";
	            Log.e("LocationSampleActivity", errorString);
	            e2.printStackTrace();
	            
	        }
			if (addresses != null && addresses.size() > 0) {
	           
	            Address address = addresses.get(0);
	            /*
	             * Format the first line of address (if available),
	             * city, and country name.
	             */
	             addressText = String.format(
	                    "%s,%s, %s",
	                    // If there's a street address, add it
	                    address.getMaxAddressLineIndex() > 0 ?
	                            address.getAddressLine(0)  : "",address.getAddressLine(1),
	                    // Locality is usually a city
	                    address.getLocality()
	                   
	                   );
	            // Return the text
	            
	        } else {
	        	  addressText = "No address found";
	        }
			//shopDetailDescription.setText(addressText);
			deliveryAddressInput.setText("");
			secondaryAddress.setText(addressText);
			LatLng coordinate = mMap.getCameraPosition().target;
			get_shop_list(new Location(coordinate.latitude,coordinate.longitude));
			Log.e("camera change listener",coordinate.toString());
			
		}
	};
		
	private OnClickListener userAddressFetch = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(userAddress != null){
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                    getActivity());
	            builderSingle.setIcon(R.drawable.home_small);
	            builderSingle.setTitle("Select Delivery Address");
	            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                    getActivity(),
	                    android.R.layout.select_dialog_singlechoice);
	            for(int i = 0 ; i < userAddress.size();i++){
	            	arrayAdapter.add(userAddress.get(i).getAddressString());
	            }
	            

	            builderSingle.setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                        }
	                    });

	            builderSingle.setAdapter(arrayAdapter,
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            String deliveryAddress = arrayAdapter.getItem(which);
	                            Location deliveryLocation = userAddress.get(which).getDeliveryLocation();
	                            deliveryAddressInput.setText(deliveryAddress);
	                            
	                            move_map_camera(new LatLng(deliveryLocation.getLatitude(), deliveryLocation.getLongitude()),
	                            		new CancelableCallback() {
											
											@Override
											public void onFinish() {
												Globals.usedPreviousAddress = true;
												setDeliveryAnchor.onClick(null);
											}
											
											@Override
											public void onCancel() {
												
											}
										});
	                            	
	                            
	                        }
	                    });
	            builderSingle.show();
			}
		}
	};
	
	private OnClickListener mapShopContinue = new OnClickListener() {
			
		
			@Override
			public void onClick(View v) {
				location.removeLocationListener();
				mCallback.mapShopStart();
		
			}
		};
	
	private OnClickListener setDeliveryAnchor = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				
			if(deliveryAddressInput.getText().toString().length() <=0 ){
				deliveryAddressInput.setError(getResources().getString(R.string.error_field_required));
			}
			else{
				deliveryAddressInput.setError(null);
			
				//hide keyboard
				InputMethodManager inputManager = (InputMethodManager)
		            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
		            InputMethodManager.HIDE_NOT_ALWAYS);
		     
			
		    //get camera center
			VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
			Point x = mMap.getProjection().toScreenLocation(visibleRegion.farRight);
			Point y = mMap.getProjection().toScreenLocation(visibleRegion.nearLeft);
			
			Point centerPoint = new Point(x.x / 2, y.y / 2);
	
			LatLng centerFromPoint = mMap.getProjection().fromScreenLocation(
			                    centerPoint);
			if(deliveryMarker == null){
			 deliveryMarker = mMap.addMarker(new MarkerOptions().position(centerFromPoint)
					.draggable(false).title(getResources().getString(R.string.delivery_anchor_title)));
					//.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_small)));
			}
			else{
				deliveryMarker.remove();
				deliveryMarker = mMap.addMarker(new MarkerOptions().position(centerFromPoint)
						.draggable(false).title(getResources().getString(R.string.delivery_anchor_title)));
				
			}
			
			
			
		    
//			zoomOutDeliveryLocation(centerFromPoint, new CancelableCallback() {
//				
//				@Override
//				public void onFinish() {
//					// TODO Auto-generated method stub
//					      /* Do something… */
//					final Handler handler = new Handler();
//					handler.postDelayed(new Runnable() {
//					  @Override
//					  public void run() {
//					   
//						  mMap.setOnCameraChangeListener(onCameraChange);
//							//Do something after 100ms
//					  }
//					}, 500);  
//							
//					  
//				}
//				
//				@Override
//				public void onCancel() {
//					// TODO Auto-generated method stub
//				}
//			});
			
			/*
			 * removing circle for first version 
			 */
			 
			//Make circle
//			if(circle == null){
//			  circle = mMap.addCircle(new CircleOptions()
//		     .center(centerFromPoint)
//		     .radius(2000)
//		     .strokeColor(getResources().getColor(R.color.app_color))
//		     .strokeWidth(5)
//		     .fillColor(getResources().getColor(R.color.transparent_white)));
//			}
//			else{
//				circle.remove();
//				circle = mMap.addCircle(new CircleOptions()
//			     .center(centerFromPoint)
//			     .radius(2000)
//			     .strokeColor(getResources().getColor(R.color.app_color))
//			     .strokeWidth(5)
//			     .fillColor(getResources().getColor(R.color.transparent_white)));
//			}
			
//			ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.anim.flip_clockwise); 
//			anim.setTarget(mapDialogView);
//			anim.setDuration(500);
//
//			
//			
//			anim.addListener(new AnimatorListener() {
//				
//				
//				@Override
//				public void onAnimationStart(Animator animation) {
//					// TODO Auto-generated method stub
//					shopDetailsView.setRotationX(180);
//					deliveryAddressView.setRotationX(180);
//					deliveryAddressView.setVisibility(View.GONE);
//					shopDetailsView.setVisibility(View.VISIBLE);
//					animationFlipClockWise = true;
//					
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animator animation) {
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animator animation) {
//						
//					//MapUI.mMap.setOnCameraChangeListener(onCameraChange);
//					Globals.deliveryAddress.setDeliveryLocation( new Location(mMap.getCameraPosition().target.latitude,
//							mMap.getCameraPosition().target.longitude));
//					
//					
//					if(deliveryAddressInput.getText().toString().length()>0){
//						Globals.deliveryAddress.setAddressString((String) deliveryAddressInput.getText().toString() + " " +addressText);
//					}
//				    
//				}
//				
//				@Override
//				public void onAnimationCancel(Animator animation) {
//						
//				}
//			});
//			anim.start();
//			
			
			//MapUI.mMap.setOnCameraChangeListener(onCameraChange);
			Globals.deliveryAddress.setDeliveryLocation( new Location(mMap.getCameraPosition().target.latitude,
					mMap.getCameraPosition().target.longitude));
			
			
			if(deliveryAddressInput.getText().toString().length()>0){
				Globals.deliveryAddress.setAddressString((String) deliveryAddressInput.getText().toString() + " " +addressText);
			}
			if(!Globals.near_shop_distance_matrix.isEmpty()){
				Shop shpObject = Globals.min_sd_matrix();				//find the shop which is very near to the location set by the camera
					//inside the shop
				connect_to_shop(shpObject);
//				Globals.isInsideShop = true;
//				shopDetailHeading.setText("Welcome to " + shpObject.getName());
//				shopDetailDescription.setText("Happy Shopping!");
//				startShop.setVisibility(View.VISIBLE);	
			}
			
			
//			mMap.setOnCameraChangeListener(onCameraChange);
//			animationFlipClockWise = true;
		}
		
		}
	};
	private MatrixCursor suggestionCursor;
	private ArrayList<String> placesNameList;
	
	
	public interface MoveCameraInterface{
		public void moveCameraFinish();
		public void moveCameraCancel();
	}	
	
	
	public static void move_map_camera(LatLng coordinate, final CancelableCallback callBack) {
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
	   mMap.animateCamera(yourLocation, callBack);
		//mMap.moveCamera(yourLocation);
	    
	}
	public static void zoomInDeliveryLocation(LatLng coordinate)
	{
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
	    mMap.moveCamera(yourLocation);
	}
	public static void zoomOutDeliveryLocation(LatLng coordinate,final GoogleMap.CancelableCallback callback)
	{
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
	    mMap.animateCamera(yourLocation, new CancelableCallback(){

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				callback.onFinish();
			}
	    	
	    });
	    
	}
	
	
	public void get_shop_list(Location areaLocation)
	{
		Shop shopObj = new Shop();
		shopObj.get_shop_list(this,areaLocation);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		if (rootView != null) {
	        ViewGroup parent = (ViewGroup) rootView.getParent();
	        if (parent != null)
	            parent.removeView(rootView);
	    }
		try{
			rootView = inflater.inflate(R.layout.map_fragment_layout, container, false);
		}catch(InflateException e){
			e.printStackTrace();
		}
		
		shopDetailsView = (LinearLayout)rootView.findViewById(R.id.shop_details_view);
    	shopDetailHeading = (TextView)rootView.findViewById(R.id.shop_details_heading);
    	deliveryAddressInput = (EditText) rootView.findViewById(R.id.delivery_address_input);
    	secondaryAddress = (TextView) rootView.findViewById(R.id.delivery_address_secondary);
    	shopDetailDescription = (TextView)rootView.findViewById(R.id.shop_details_description);
    	mapDialogView = (LinearLayout) rootView.findViewById(R.id.map_dialog);
    	deliveryAddressView = (LinearLayout)rootView.findViewById(R.id.delivery_address_container);
    	map_container = (FrameLayout)rootView.findViewById(R.id.map_container);
    	mMapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment));
    	mMap = mMapFragment.getMap();
    	userAddresses = (ImageButton)rootView.findViewById(R.id.address_selector);
    	userAddresses.setOnClickListener(userAddressFetch);
    	
    	setDeliveryLocation = (Button)rootView.findViewById(R.id.setDeliveryLocation);
    	setDeliveryLocation.setOnClickListener(setDeliveryAnchor);
    	//CaptureActivity.actionBar.setTitle(getResources().getText(R.string.pick_delivery_location));
        
    	startShop = (Button)rootView.findViewById(R.id.startShop);
    	startShop.setOnClickListener(mapShopContinue);
    	mMap.setOnCameraChangeListener(onCameraChange);
    	mMap.setOnMarkerClickListener(this);
    	mMap.setMyLocationEnabled(true);
    	View mapView = mMapFragment.getView();
    	if(mapView!= null){
    	Resources r = getResources();
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
    	View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
    	RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        rlp.setMargins(30, 30, 30, (int) px);
        
        
    	
    	View zoomControls = ((View) mapView.findViewById(1).getParent()).findViewById(1);
    	RelativeLayout.LayoutParams rlp1 = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();
        // position on right bottom
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        rlp1.setMargins(30, 30, 30, 250);
    	}
    	
    	mMap.getUiSettings().setZoomControlsEnabled(true);
    	setHasOptionsMenu(true);
        return rootView;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    
	}
	@Override
	public void onResume()
	{
		
		super.onResume();
		 if(mMap != null){
		        if(!location.made_use_of_location){
		        	location loc = new location();
					loc.getLocation(this,getActivity());
		        }
		        else{
		        	LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		    		zoomInDeliveryLocation(coordinate);
		    		
		        }
	      }
        else{
        	
        	map_container.setVisibility(View.GONE);
        }
		 
		 if(userAddress.size() <= 0){
	    		userAddresses.setVisibility(View.INVISIBLE);
	    	}
		 else{
			 userAddresses.setVisibility(View.VISIBLE);
		 }
	}
	
	@Override
	public void onPause(){
		super.onPause();
	
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		    MenuReference = menu;
	    	inflater.inflate(R.menu.places_search_menu, menu);
	    	
	        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
	        MenuItem searchMenuItem =  menu.findItem(R.id.search);
	        shopSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
	        
	        shopSearchView.setQueryHint("Search Your Delivery Location");
	        shopSearchView.setSubmitButtonEnabled(false);
	        shopSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
	        
	        shopSearchView.setIconifiedByDefault(true); //this line
	        String[] columnNames = {"_id","description"};
	         suggestionCursor  = new MatrixCursor(columnNames);
	         placesNameList = new ArrayList<String>();
	         searchAdapter = new PlacesSearchAdapter(Globals.ApplicationContext, suggestionCursor, placesNameList);
				shopSearchView.setSuggestionsAdapter(searchAdapter);
	        shopSearchView.setOnQueryTextListener(new OnQueryTextListener(){
	        	PlacesAutoComplete pl = new PlacesAutoComplete();
				@Override
				public boolean onQueryTextChange(String newText) {
					if(newText.length()<= 3){

				    	
				        
						pl.autocomplete(newText);
						
					}
					else{
						searchAdapter.getFilter().filter(newText);
					}
					
					return true;
				}

				@Override
				public boolean onQueryTextSubmit(String query) {
					
					return true;
				}
	        	
	        });
	        
	        shopSearchView.setOnSuggestionListener(new OnSuggestionListener(){

				@Override
				public boolean onSuggestionClick(int index) {
					
					if(Geocoder.isPresent()) {

					    Geocoder geocoder = new Geocoder(getActivity());
					    List<Address> addresses = null;
					    try {
					    	
					        addresses = geocoder.getFromLocationName(getSuggestion(index), 1);
					        if (!addresses.isEmpty()) {
					            Address address = addresses.get(0);
					            LatLng coordinate = new LatLng(address.getLatitude(),address.getLongitude());
					            
					            //using converted address to latlng to query for shops
					            move_map_camera(coordinate,null);
					            get_shop_list(new Location(coordinate.latitude,coordinate.longitude));
					           
					            //hide keyboard
					            InputMethodManager inputManager = (InputMethodManager)
					            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
					            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					            InputMethodManager.HIDE_NOT_ALWAYS);
					            shopSearchView.setIconifiedByDefault(true); //this line
					            
					        } else {
					            // No results for your location
					        }
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					}
					return true;
				}

				@Override
				public boolean onSuggestionSelect(int arg0) {
					//Toast.makeText(getApplicationContext(), Integer.toString(arg0), Toast.LENGTH_SHORT).show();
					return true;
				}});
			 
	        MenuItemCompat.setOnActionExpandListener(searchMenuItem,new OnActionExpandListener() {
				
				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {
//					map_container.setVisibility(View.VISIBLE);
//					mapVisible = true;
					return true;
				}
				
				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					
////						FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
////						map_container.setVisibility(View.INVISIBLE);
////						MenuItem CartMenuItem = (MenuItem) menu.findItem(R.id.shopping_cart);
////			            CartMenuItem.setVisible(true);
////						MapUI.mapVisible = false;
										
					
					return true;
				}
			});
	      
	        
	      
	        return;
	}
	private String getSuggestion(int position) {
	    String suggest1 = (String) shopSearchView.getSuggestionsAdapter().getItem(position);    
	    return suggest1;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (MapInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MapInterface");
        }
    }
	
	
	@Override
	public void make_use_of_location() {
		
//		ArrayList<Shop> connectedNearShoplist  = Globals.dbhelper.getNearConnectedShop(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
//		
//		if(connectedNearShoplist == null){
//			
//			//get_shop_list(Globals.current_location);
//		}
//		
//		else{
//			//get_shop_list(Globals.current_location);
//			//shop_list_success(Globals.current_location,connectedNearShoplist);
//			
//		}
		
		LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		zoomInDeliveryLocation(coordinate);
		location.made_use_of_location = true;
		get_shop_list(Globals.current_location);
	}
	public void toggle_map()
	{
    	
    	
    	
		FrameLayout map_container = (FrameLayout)rootView.findViewById(R.id.map_container);
		
		if(mapVisible){
			map_container.setVisibility(View.INVISIBLE);
			mapVisible = false;
		}
		else{
			map_container.setVisibility(View.VISIBLE);
			mapVisible = true;
		}
	    	
	}
	public void showFullShopDetails(View v)
	{
		
		if(Globals.connected_to_shop_success){
			move_map_camera(new LatLng(Globals.connectedShop.getLocation().getLatitude(),
					Globals.connectedShop.getLocation().getLongitude()),null);
		}
	}
	
	
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		
//		double lat = 	marker.getPosition().latitude;
//		double lng = 	marker.getPosition().longitude;
//		LatLng coordinate = new LatLng(lat, lng);
//		
//		Shop shopObject = Globals.get_shop_from_location(coordinate);
//		if(shopObject == null){
//			Toast.makeText(getActivity(), "Can not connect to this shop", Toast.LENGTH_SHORT).show();
//			
//			
//		}
//		else{
//			
//			if(Globals.connectedShop != null && shopObject.getName().equals(Globals.connectedShop.getName())){
//				Toast.makeText(getActivity(), ("You are Connected to " + Globals.connectedShop.getName()), Toast.LENGTH_SHORT).show();
//			}
//			else{
//				connect_to_shop(shopObject);
//				
//			}
//		}
		return true;
	}
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ShopInterface#shop_list_success(com.shoplite.models.Location, java.util.ArrayList)
	 */
	@Override
	public void shop_list_success(Location areaLocation,
			ArrayList<Shop> shoplist) {
		// TODO Auto-generated method stub
		//MapUI.mMap.clear();
		
		if( shoplist != null && shoplist.size()>0){
			double map_center_lat = areaLocation.getLatitude();     // current lat and lng to create a range for blue marker representing shop in the range of 200 mtr
			double map_center_lng = areaLocation.getLongitude();
			
			//building markers and storing them
			for(int i = 0; i < shoplist.size() ; i++ ){
				double shopLat =  shoplist.get(i).getLocation().getLatitude();
				double shopLng =  shoplist.get(i).getLocation().getLongitude();
				Globals.add_to_sd_matrix(shoplist.get(i),shopLat,shopLng,areaLocation);
				
//				
//				Marker shop_marker = null;
//				
//				if((Globals.current_location.getLatitude()<shopLat+0.0003) 
//						&& (Globals.current_location.getLatitude() > shopLat-0.0003))
//				{
//					if((Globals.current_location.getLongitude() < shopLng+0.0003) 
//							&& (Globals.current_location.getLongitude() > shopLng - 0.0003))
//					{
//						LatLng coordinate = new LatLng(shopLat, shopLng);
//						Globals.add_to_sd_matrix(shoplist.get(i),shopLat,shopLng);
//						
//						//add center  for shops
////						shop_marker = mMap.addMarker(new MarkerOptions().position(coordinate)
////								.draggable(false).title(shoplist.get(i).getName())
////								.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_small)));
////						
////						markerList.add(shop_marker);
//						continue;
//					}
//				}
//				
//				LatLng coordinate = new LatLng(shopLat, shopLng);
////				shop_marker = mMap.addMarker(new MarkerOptions().position(coordinate).snippet("Shop")
////						.draggable(false).title(shoplist.get(i).getName())
////						.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_small)));
////				markerList.add(shop_marker);
//				
	    
			}
			deliveryAddressView.setVisibility(View.VISIBLE);
			shopDetailsView.setVisibility(View.GONE);
			
//			if(!Globals.near_shop_distance_matrix.isEmpty()){
//				Shop shpObject = Globals.min_sd_matrix();				//find the shop which is very near to the location set by the camera
//					//inside the shop
//				connect_to_shop(shpObject);
////				Globals.isInsideShop = true;
////				shopDetailHeading.setText("Welcome to " + shpObject.getName());
////				shopDetailDescription.setText("Happy Shopping!");
////				startShop.setVisibility(View.VISIBLE);	
//			}
//				
//			else 
//			{
//				// nearby the shop  
//				Globals.isInsideShop = false;
//				//shopDetailHeading.setText("Select a shop near your delivery location");
//				if(!animationFlipClockWise){
//					deliveryAddressView.setVisibility(View.VISIBLE);
//					shopDetailsView.setVisibility(View.GONE);
//				}else{
//					if(shoplist.size()>1){
//					 shopDetailHeading.setText("");
//					 shopDetailDescription.setText("Multiple Shops found tap shop marker to connect or drag map to change delivery location");
//					 startShop.setVisibility(View.GONE);
//					}
//					else{
//						shopDetailHeading.setText("");
//						connect_to_shop( shoplist.get(0));
//						shopDetailDescription.setText("");
//						startShop.setVisibility(View.GONE);	
//					}
//				}
//				
//			}
						
		}
		else{
			// no shop found
			shopDetailHeading.setText("No Shops currently in this area, soon we are reaching");
			deliveryAddressView.setVisibility(View.GONE);
			shopDetailsView.setVisibility(View.VISIBLE);
			shopDetailDescription.setText("Drag map to change delivery Location");
			startShop.setVisibility(View.GONE);
		}
		
		   	       
	

	}
	
	
	public void connect_to_shop(Shop shopObj) {
		
		shopObj.connect_to_shop(this);
		//shopDetailHeading.setText("Connecting to " + shopObj.getName());
		
	}
	
	
	@Override
	public void shop_connected() {
		
		double lat =  Globals.connectedShop.getLocation().getLatitude();
		double lng =  Globals.connectedShop.getLocation().getLongitude();
		
		LatLng coordinate = new LatLng(lat, lng);
			
		shopDetailHeading.setText("Welcome to " + Globals.connectedShop.getName());
		
		shopDetailDescription.setText("You can shop various " +
				"products through your cam scanner " +
				"or through the list at an affordable prices.\nHappy Shopping!");
		
		location.removeLocationListener();
		mCallback.mapShopStart();
		//startShop.setVisibility(View.VISIBLE);
	}
	


public static class PlacesAutoComplete {

	

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyDYYRgTGk777pOLVGQgqyYA3QtFKF9BMbw";
	public ServiceProvider serviceProvider = null;
	public JsonArray placesPrediction = new JsonArray();
	public static ArrayList<PlacePrediction> placesList;
	public ArrayList<String> placesName ;
	private String[] columnNames = {"_id","description"};
	public MatrixCursor suggestionCursor  ;
	public ArrayList<String> placesNameList;
	public static ArrayList<PlacePrediction> getPlacesList()
	{
		return placesList;
	}
	
	public void autocomplete(String input) {
		
		placesList = new ArrayList<PlacePrediction>();
		placesNameList = new ArrayList<String>();
		suggestionCursor  = new MatrixCursor(columnNames);
		
	    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	    sb.append("?key=" + API_KEY);
	    sb.append("&types=(regions)");
	    sb.append("&location="+Globals.current_location.getLatitude().toString()+
	    		","+Globals.current_location.getLongitude().toString());
        sb.append("&components=country:in");
      
        try {
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sb.append("&offset=2");
        String url = sb.toString();
	    RestAdapter restAdapter = new RestAdapter.Builder()
		.setEndpoint(url)		
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();
		
	    serviceProvider = restAdapter.create(ServiceProvider.class);
		
		serviceProvider.getPlacesSuggestion(new Callback<JsonObject>(){

			@Override
			public void failure(RetrofitError response) {
				
				Log.e("Places API Error", response.toString());
			}

			@Override
			public void success(JsonObject result, Response response) {
				Log.e("Places API Success", response.toString());
				placesPrediction = result.getAsJsonArray("predictions");
				for (int i = 0; i < placesPrediction.size(); i++) {
					Gson gson = new Gson();
					PlacePrediction pl = gson.fromJson(placesPrediction.get(i), PlacePrediction.class);
					placesList.add(pl);
					suggestionCursor.addRow(new Object[]{i,pl.getDescription()});
		            placesNameList.add(pl.getDescription());
		            
		        }
				if(placesNameList.size() != 0){
					searchAdapter = new PlacesSearchAdapter( Globals.ApplicationContext,suggestionCursor, placesNameList);
					shopSearchView.setSuggestionsAdapter(searchAdapter);
				}

			}
			
		});
		
	   
	   
	    

	   
	}

	
}

	
}
