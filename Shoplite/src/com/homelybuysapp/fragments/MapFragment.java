/**
 * 
 */
package com.homelybuysapp.fragments;

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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.homelybuys.homelybuysApp.R;
import com.homelybuysapp.UI.ButteryProgressBar;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.UI.PlacesSearchAdapter;
import com.homelybuysapp.UI.UIUtil;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.Utils.HomelyBuysLocation;
import com.homelybuysapp.connection.ServiceProvider;
import com.homelybuysapp.interfaces.LocationInterface;
import com.homelybuysapp.interfaces.MapInterface;
import com.homelybuysapp.interfaces.ShopInterface;
import com.homelybuysapp.models.Location;
import com.homelybuysapp.models.PlacePrediction;
import com.homelybuysapp.models.Shop;

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
    private FrameLayout mapDialogView;
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
	private Button setDeliveryLocation;
	private ImageButton userAddresses;
	private ImageButton currentLocationBut;
	private FrameLayout map_container;
	MapInterface mCallback = null;
	ArrayList<com.homelybuysapp.models.Address> userAddress = Globals.dbhelper.getStoreAddress();
	public static ArrayList<Marker> markerList = new ArrayList<Marker>();
	private static PlacesSearchAdapter searchAdapter;	
	private static ButteryProgressBar mapSearchProgressbar ;
	private String primaryAddress;
	public static SimpleCursorAdapter  suggestionAdapter;
    private OnCameraChangeListener onCameraChange = new OnCameraChangeListener() {
		@Override
		public void onCameraChange(CameraPosition position) {
			
			mMap.clear();
			addressText = null;
			Geocoder geocoder =  new Geocoder(getActivity(), Locale.US);
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
	            
	            builderSingle.setTitle("Previous Delivery Addresses");
	            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                    getActivity(),
	                    android.R.layout.simple_selectable_list_item);
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
				HomelyBuysLocation.removeLocationListener();
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
				Controls.show_loading_dialog(getActivity(), "Setting Delivery Location");

				deliveryAddressInput.setError(null);
			
				//hide keyboard
				UIUtil.hideSoftKeyboard(getActivity());
			
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
			}
			else{
				deliveryMarker.remove();
				deliveryMarker = mMap.addMarker(new MarkerOptions().position(centerFromPoint)
						.draggable(false).title(getResources().getString(R.string.delivery_anchor_title)));
				
			}
			
			
			
		    
			Globals.deliveryAddress.setDeliveryLocation( new Location
					(mMap.getCameraPosition().target.latitude,
					mMap.getCameraPosition().target.longitude));
			
			Globals.usedPreviousAddress = false;
			if(deliveryAddressInput.getText().toString().length()>0){
				primaryAddress = (String) deliveryAddressInput.getText().toString();
				Globals.deliveryAddress.setAddressString(primaryAddress+ " " +addressText);
			}
			if(!Globals.near_shop_distance_matrix.isEmpty()){
				Shop shpObject = Globals.min_sd_matrix();				//find the shop which is very near to the HomelyBuysLocation set by the camera
					//inside the shop
				connect_to_shop(shpObject);
				
			}
			
			
		}
		
		}
	};
	private MatrixCursor suggestionCursor;
	private ArrayList<String> placesNameList;
	private boolean isWaitingForLocation = false;
	
	
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
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
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
	
	
	public void get_shop_list(Location location)
	{
		mapSearchProgressbar.setVisibility(View.VISIBLE);
		mapSearchProgressbar.start();
		Shop shopObj = new Shop();
		shopObj.get_shop_list(this,location);
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
    	mapDialogView = (FrameLayout) rootView.findViewById(R.id.map_dialog);
    	deliveryAddressView = (LinearLayout)rootView.findViewById(R.id.delivery_address_container);
    	map_container = (FrameLayout)rootView.findViewById(R.id.map_container);
    	mMapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment));
    	mMap = mMapFragment.getMap();
    	userAddresses = (ImageButton)rootView.findViewById(R.id.address_selector);
    	currentLocationBut = (ImageButton)rootView.findViewById(R.id.location_selector);
    	userAddresses.setOnClickListener(userAddressFetch);
    	currentLocationBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Globals.current_location != null){
					LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
					move_map_camera(coordinate,null);
				}
			}
		});
    	setDeliveryLocation = (Button)rootView.findViewById(R.id.setDeliveryLocation);
    	setDeliveryLocation.setOnClickListener(setDeliveryAnchor);
    	//HomeActivity.actionBar.setTitle(getResources().getText(R.string.pick_delivery_location));
    	mapSearchProgressbar = (ButteryProgressBar)rootView.findViewById(R.id.map_progress_bar);
    	startShop = (Button)rootView.findViewById(R.id.startShop);
    	startShop.setOnClickListener(mapShopContinue);
    	mMap.setOnCameraChangeListener(onCameraChange);
    	mMap.setOnMarkerClickListener(this);
    	mMap.setMyLocationEnabled(false);
    	View mapView = mMapFragment.getView();
    	mMap.getUiSettings().setZoomControlsEnabled(true);

       mMap.setOnMapClickListener(new OnMapClickListener() {
		
		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub
			//Toast.makeText(getActivity(), "touched", Toast.LENGTH_SHORT).show();
			UIUtil.hideSoftKeyboard(getActivity());
		}
       });
       mapSearchProgressbar.setVisibility(View.GONE);
		mapSearchProgressbar.stop();

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
			 	 
		        if(Globals.current_location == null){
		        	
					HomelyBuysLocation.getLocation(this,getActivity());

		        }
		        else{
		        	if(Globals.current_location != null){
		        		LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		        		zoomInDeliveryLocation(coordinate);
		        	}
		    		
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
	        
	        shopSearchView.setQueryHint("Search Your Delivery HomelyBuysLocation");
	        shopSearchView.setSubmitButtonEnabled(false);
	        shopSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
	        
	        shopSearchView.setIconifiedByDefault(true); //this line
	        String[] columnNames = {"_id","description"};
	         suggestionCursor  = new MatrixCursor(columnNames);
	         placesNameList = new ArrayList<String>();
	         searchAdapter = new PlacesSearchAdapter(Globals.ApplicationContext, suggestionCursor, placesNameList);
				shopSearchView.setSuggestionsAdapter(searchAdapter);
	        	final PlacesAutoComplete pl = new PlacesAutoComplete();

	        shopSearchView.setOnQueryTextListener(new OnQueryTextListener(){
				@Override
				public boolean onQueryTextChange(String newText) {
					if(newText.length()<= 3){
					       mapSearchProgressbar.setVisibility(View.VISIBLE);

						mapSearchProgressbar.start();

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
					            
					            mapSearchProgressbar.setVisibility(View.GONE);
					            mapSearchProgressbar.stop();

					        } else {
					            // No results for your HomelyBuysLocation
					        }
					    } catch (IOException e) {
					        e.printStackTrace();
					    }
					}
					return true;
				}

				@Override
				public boolean onSuggestionSelect(int arg0) {
					return true;
				}});
			 
	        MenuItemCompat.setOnActionExpandListener(searchMenuItem,new OnActionExpandListener() {
				
				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {

					return true;
				}
				
				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					
					
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
                    + "must implement MapInterface");
        }
    }
	
	
	@Override
	public void make_use_of_location() {
				
		LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		move_map_camera(coordinate, null);
		//zoomInDeliveryLocation(coordinate);
		get_shop_list(Globals.current_location);
		isWaitingForLocation = false;
		Log.e("waiting for location",String.valueOf(isWaitingForLocation));

		Controls.dismiss_progress_dialog();
		HomelyBuysLocation.setHasLocation(true);

	}
	
	@Override
	public void locationDenied(){
		Controls.dismiss_progress_dialog();

	}
	
	
	
	@Override
	public boolean onMarkerClick(Marker marker) {

		return true;
	}
	
	@Override
	public void shop_list_success(Location areaLocation,
			ArrayList<Shop> shoplist) {
		// TODO Auto-generated method stub
		//MapUI.mMap.clear();
        mapSearchProgressbar.setVisibility(View.GONE);

        mapSearchProgressbar.stop();

		if( shoplist != null && shoplist.size()>0){
			double map_center_lat = areaLocation.getLatitude();     // current lat and lng to create a range for blue marker representing shop in the range of 200 mtr
			double map_center_lng = areaLocation.getLongitude();
			
			//building markers and storing them
			for(int i = 0; i < shoplist.size() ; i++ ){
				double shopLat =  shoplist.get(i).getLocation().getLatitude();
				double shopLng =  shoplist.get(i).getLocation().getLongitude();
				Globals.add_to_sd_matrix(shoplist.get(i),shopLat,shopLng,areaLocation);
				
			
	    
			}
			deliveryAddressView.setVisibility(View.VISIBLE);
			shopDetailsView.setVisibility(View.GONE);
			
						
		}
		else{
			// no shop found
			shopDetailHeading.setText("No Shops currently in this area,We are trying our best to expand our services.");
			deliveryAddressView.setVisibility(View.GONE);
			shopDetailsView.setVisibility(View.VISIBLE);
			shopDetailDescription.setText("Drag map to choose delivery Location");
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
		deliveryAddressInput.setText(primaryAddress);
		HomelyBuysLocation.removeLocationListener();
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
	
	public  void autocomplete(String input) {
		
		placesList = new ArrayList<PlacePrediction>();
		placesNameList = new ArrayList<String>();
		suggestionCursor  = new MatrixCursor(columnNames);
		
	    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	    sb.append("?key=" + API_KEY);
	    sb.append("&types=(regions)");
	    sb.append("&HomelyBuysLocation="+Globals.current_location.getLatitude().toString()+
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
	            mapSearchProgressbar.setVisibility(View.GONE);

				mapSearchProgressbar.stop();

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
	            mapSearchProgressbar.setVisibility(View.GONE);

				mapSearchProgressbar.stop();
			}
			
		});
		
	   
	   
	    

	   
	}

	
}

	
}
