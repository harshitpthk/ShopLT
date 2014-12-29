

package com.google.zxing.client.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.shoplite.UI.AddItemCard;
import com.shoplite.UI.BaseCardView;
import com.shoplite.UI.BaseItemCard.OnClickActionButtonListener;
import com.shoplite.UI.ButteryProgressBar;
import com.shoplite.UI.Controls;
import com.shoplite.UI.DrawerItemAdapter;
import com.shoplite.UI.MapUI;
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.PlacesAutoComplete;
import com.shoplite.Utils.location;
import com.shoplite.activities.SettingsActivity;
import com.shoplite.fragments.CartFragment;
import com.shoplite.fragments.ContainerFragment;
import com.shoplite.fragments.MapFragment;
import com.shoplite.fragments.SavedListsFragment;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.interfaces.ItemInterface;
import com.shoplite.interfaces.LocationInterface;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.interfaces.ShopInterface;
import com.shoplite.models.Location;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;
import com.shoplite.models.Product;
import com.shoplite.models.ProductVariance;
import com.shoplite.models.SaveList;
import com.shoplite.models.Shop;

import eu.livotov.zxscan.R;
import eu.livotov.zxscan.ZXScanHelper;


/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * QRCodeView to help the user place the QRCode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * 
 * 
 * @author Harshit Pathak
 */

public class CaptureActivity extends FragmentActivity  implements SurfaceHolder.Callback,ShopInterface
,LocationInterface,ItemInterface,OnMarkerClickListener,
ControlsInterface,PackListInterface
{

    private static final String TAG = CaptureActivity.class.getSimpleName();  // Log tags
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1L;		  // Capture Activity Intent Result Duration
	private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    SurfaceHolder.Callback cameraSurfaceCallback = this;
    
   
    public static ButteryProgressBar progressBar;
    public static FrameLayout decorView;
    
    private FrameLayout mainFragmentContainer;
	private View mainTabsView;
    
	CartFragment cartFrag = new CartFragment();
    ContainerFragment conFrag = new ContainerFragment();
    MapFragment mapFrag = new MapFragment();
   
    
    private ListView ldrawer;
    private DrawerLayout mDrawerLayout;
    private String[] main_action = {"Groceries","Fruits & Veg","Beverages/Health-Drinks","Dairy/Eggs","Ready to Eat/Packed Foods","Personnel Care","Household","Stationery"};
    private ActionBarDrawerToggle mDrawerToggle;
    
    
    private Window window;
	private Menu MenuReference;
	public static  SearchView shopSearchView = null;
    public ProgressBar  prgBar;
   
    private TextView shopDetailHeading;
	private LinearLayout shopDetailsView;
	private TextView shopDetailDescription;
    private Button startShop;
    
    
    public static boolean isProgressBarAdded;
    private ImageButton shopByListButton;
    private ImageButton shopAtStoreButton;
    private ImageButton orderListButton;
    private Marker deliveryMarker;
	//private TextView deliveryDialogText;
	private String addressText ;
	private Circle circle;
	private static AlertDialog AddDialog;
    public static AddItemCard addToItem;
    public static ActionBar actionBar;
    private EditText deliveryAddressInput;
    private TextView secondaryAddress;
	
	private LinearLayout deliveryAddressView;
	private LinearLayout mapDialogView;
	private boolean animationFlipClockWise;
	private MenuItem importToCart;
	
	protected static DialogFragment saveListDialog;
	public static int SAVE_LIST_REQUEST = 200;
		
	private OnCameraChangeListener onCameraChange = new OnCameraChangeListener() {
		
		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			if(animationFlipClockWise){
				ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.flip_anti_clockwise); 
				anim.setTarget(mapDialogView);
				anim.setDuration(500);
				anim.addListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						shopDetailsView.setRotationX(0);
						deliveryAddressView.setRotationX(0);
						deliveryAddressView.setVisibility(View.VISIBLE);
						shopDetailsView.setVisibility(View.GONE);
						
						animationFlipClockWise = false;
				
					}
					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						
							}
					@Override
					public void onAnimationCancel(Animator animation) {
						// TODO Auto-generated method stub
					}
				});
				anim.start();
			}
			else{
				
			}
			MapUI.mMap.clear();
			addressText = null;
			Geocoder geocoder =
	                new Geocoder(getApplicationContext(), Locale.getDefault());
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
	            // Get the first address
	            Address address = addresses.get(0);
	            /*
	             * Format the first line of address (if available),
	             * city, and country name.
	             */
	             addressText = String.format(
	                    "%s, %s",
	                    // If there's a street address, add it
	                    address.getMaxAddressLineIndex() > 0 ?
	                            address.getAddressLine(0) : "",
	                    // Locality is usually a city
	                    address.getLocality()
	                   
	                   );
	            // Return the text
	            
	        } else {
	        	  addressText = "No address found";
	        }
			//shopDetailDescription.setText(addressText);
			secondaryAddress.setText(addressText);
			
			
			LatLng coordinate = MapUI.mMap.getCameraPosition().target;
			get_shop_list(new Location(coordinate.latitude,coordinate.longitude));
			Log.e("camera change listener",coordinate.toString());
			
		}
	};
	    
   //activity methods
   
   
	
	@Override
    public void onCreate(Bundle icicle)
    {
		  
		super.onCreate(icicle);

	    if (android.os.Build.VERSION.SDK_INT < 8 || ZXScanHelper.isBlockCameraRotation())
	    {
	    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
       
        window = getWindow();
        int scanner_layout = R.layout.scanner_layout_capture;                 	// setting the custom layout on top of capture activity
		setContentView(scanner_layout);
        
		//action Bar
		actionBar = getActionBar();
		actionBar.setTitle(getResources().getText(R.string.pick_delivery_location));
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		
		
		
		progressBar = new ButteryProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 24)); // create new ProgressBar and style it
        decorView = (FrameLayout) getWindow().getDecorView();			// retrieve the top view of our application
        
        // Here we try to position the ProgressBar to the correct position by looking
        // at the position where content area starts. But during creating time, sizes 
        // of the components are not set yet, so we have to wait until the components
        // has been laid out
        // Also note that doing progressBar.setY(136) will not work, because of different
        // screen densities and different sizes of actionBar
        
       ViewTreeObserver observer = progressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View contentView = decorView.findViewById(android.R.id.content);
                progressBar.setY(contentView.getY() - 10);

                ViewTreeObserver observer = progressBar.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(this);
            }
        });
        
        
        
        mainFragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
        mainTabsView = (View) findViewById(R.id.main_tabs_view);
        shopByListButton = (ImageButton) findViewById(R.id.shop_outside_store);
    	shopAtStoreButton=(ImageButton) findViewById(R.id.shop_at_store);
    	orderListButton = (ImageButton) findViewById(R.id.orders_list);
    	
       
        
        mDrawerLayout = (DrawerLayout)this.findViewById(R.id.drawer_layout_capture);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.navigation_drawer,R.string.drawer_open,R.string.drawer_open)
        					{
					        	public void onDrawerClosed(View view) {
					                super.onDrawerClosed(view);
					                //getActionBar().setTitle(mTitle);
					            }
					
					            /** Called when a drawer has settled in a completely open state. */
					            public void onDrawerOpened(View drawerView) {
					                super.onDrawerOpened(drawerView);
					                //getActionBar().setTitle(mDrawerTitle);
					            }
        	
        				   };
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
        ldrawer = (ListView)this.findViewById(R.id.left_drawer_capture);
        ldrawer.setAdapter(new ArrayAdapter<String>(this.getBaseContext(),R.layout.drawer_list_item, main_action));
        ldrawer.setOnItemClickListener(new DrawerItemClickListener());// Set the list's click listener
    	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
    	
    	
    	
    	
    	
		
    	  
    	
    	mapDialogView = (LinearLayout) findViewById(R.id.map_dialog);
    	deliveryAddressView = (LinearLayout)findViewById(R.id.delivery_address_container);
    	deliveryAddressInput = (EditText) findViewById(R.id.delivery_address_input);
    	secondaryAddress = (TextView) findViewById(R.id.delivery_address_secondary);
    	
    	shopDetailsView = (LinearLayout)findViewById(R.id.shop_details_view);
    	shopDetailHeading = (TextView)findViewById(R.id.shop_details_heading);
    	shopDetailDescription = (TextView)findViewById(R.id.shop_details_description);
    	startShop = (Button)findViewById(R.id.startShop);
    	
    	MapUI.mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment));
    	MapUI.mMap = MapUI.mMapFragment.getMap();
    	View mapView = MapUI.mMapFragment.getView();
    	View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
    	RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        rlp.setMargins(30, 30, 30, 40);

    	MapUI.mMap.setOnCameraChangeListener(onCameraChange);
    	MapUI.mMap.setOnMarkerClickListener(this);
    	MapUI.mMap.setMyLocationEnabled(true);
    	
    	hasSurface = false;
       // inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        if (ZXScanHelper.getUserCallback() != null)
        {
            ZXScanHelper.getUserCallback().onScannerActivityCreated(this);
        }
        
        
       
		
		
        
    }
     
	@Override
    protected void onResume()
    {
        super.onResume();
       // inactivityTimer.onResume();
        if(MapUI.mMap != null){
	        if(!location.made_use_of_location){
	        	location loc = new location();
				loc.getLocation(this,this);
	        }
	        else{
	        	LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
	    		MapUI.zoomInDeliveryLocation(coordinate);
	    		
	        }
        }
        else{
        	FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
        	map_container.setVisibility(View.GONE);
        }
		
        
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        
		
		if(!conFrag.isAdded()){
        	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,conFrag ).commit();
        	
        }
		if(!cartFrag.isAdded()){
        	getSupportFragmentManager().beginTransaction().add(R.id.container,cartFrag ).detach(cartFrag).commit();
        	
        }
	
		
    }

    @Override
	public void onPause()
    {
    	super.onPause();
    	//inactivityTimer.onPause();
    	if(conFrag.isAdded()){
	        if (handler != null)
	        {
	            handler.quitSynchronously();
	            handler = null;
	        }
	        
	        cameraManager.closeDriver();
	        if (!hasSurface)
	        {
	            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
	            SurfaceHolder surfaceHolder = surfaceView.getHolder();
	            surfaceHolder.removeCallback(this);
	        }
    	}
        
    }

    @Override
    protected void onDestroy()
    {
    	if(conFrag.isAdded()){
	       // inactivityTimer.shutdown();
	        if (ZXScanHelper.getUserCallback() != null)
	        {
	            ZXScanHelper.getUserCallback().onScannerActivityDestroyed(this);
	        }
    	}
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
   
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
            	if(cartFrag.isAdded()){
            		showCart(null);
                	
            	}else{
            		
            		moveTaskToBack (true);
            	}
                return true;

            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
             return true;
           }
    	
    	 if(item.getItemId() == R.id.shopping_cart){
         
        	 showCart(null);
             return true;
    	}
    	 else if(item.getItemId() == R.id.settings){
    		 Intent i = new Intent(this, SettingsActivity.class);
    		 startActivity(i);
    		 return true;
    	 }
    	else
             return super.onOptionsItemSelected(item);
     }
          
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuReference = menu;
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_action_bar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        shopSearchView = (SearchView)menu.findItem(R.id.search).getActionView();
        shopSearchView.setQueryHint("Search Your Delivery Location");
        shopSearchView.setSubmitButtonEnabled(false);
        shopSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        shopSearchView.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String newText) {
				if(newText.length()>= 3){
					PlacesAutoComplete pl = new PlacesAutoComplete();
					pl.autocomplete(newText);
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

				    Geocoder geocoder = new Geocoder(getApplicationContext());
				    List<Address> addresses = null;
				    try {
				        addresses = geocoder.getFromLocationName(PlacesAutoComplete.placesList.get(index).getDescription(), 1);
				        if (!addresses.isEmpty()) {
				            Address address = addresses.get(0);
				            LatLng coordinate = new LatLng(address.getLatitude(),address.getLongitude());
				            
				            //using converted address to latlng to query for shops
				            MapUI.move_map_camera(coordinate);
				            get_shop_list(new Location(coordinate.latitude,coordinate.longitude));
				           
				            //hide keyboard
				            InputMethodManager inputManager = (InputMethodManager)
				            getSystemService(Context.INPUT_METHOD_SERVICE); 
				            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				            InputMethodManager.HIDE_NOT_ALWAYS);
	                   
				            
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
        MenuItem shopSearch = (MenuItem) menu.findItem(R.id.search);
        shopSearch.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				
				FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
				map_container.setVisibility(View.VISIBLE);
				MapUI.mapVisible = true;
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				if(conFrag.isAdded())
				{
//					FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
//					map_container.setVisibility(View.INVISIBLE);
//					MenuItem CartMenuItem = (MenuItem) menu.findItem(R.id.shopping_cart);
//		            CartMenuItem.setVisible(true);
//					MapUI.mapVisible = false;
									
				}
				return true;
			}
		});
       
        
        importToCart = menu.findItem(R.id.import_to_cart);
        importToCart.setVisible(false);
        importToCart.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				if(!cartFrag.isDetached()){
					ArrayList<SaveList> savedLists = Globals.dbhelper.getAllSavedShopList();
					if(savedLists.size()>0){
						saveListDialog = SavedListsFragment.newInstance();
						saveListDialog.show(getSupportFragmentManager(), "SaveListDialog"); 
						
					}
					else{
						Toast.makeText(getBaseContext(), "You Don't have any Saved List, Create One to Use Import",
								Toast.LENGTH_LONG).show();
					}
				}
				return true;
			}
		});
        return super.onCreateOptionsMenu(menu);
    }
    
    
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	
    		MenuItem CartMenuItem = (MenuItem) menu.findItem(R.id.shopping_cart);
            CartMenuItem.setVisible(false);
    	
        
		return super.onPrepareOptionsMenu(menu);
    	
    }
   
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
    	super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAVE_LIST_REQUEST) {
            if (resultCode == RESULT_OK) {
                if(saveListDialog != null)            	
                	saveListDialog.dismiss();
                // Do something with the contact here (bigger example below)
            }
        }
    }
    
    
    //Zxing Library Methods
    
    
    public void initCamera(SurfaceHolder surfaceHolder)
    {
        if (surfaceHolder == null)
        {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen())
        {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try
        {
            cameraManager.openDriver(surfaceHolder);
            
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null)
            {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe)
        {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit(ioe);
        } catch (RuntimeException e)
        {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit(e);
        }
    }
    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result)
    {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null)
        {
            savedResultToShow = result;
        } else
        {
            if (result != null)
            {
                savedResultToShow = result;
            }
            if (savedResultToShow != null)
            {
                Message message = Message.obtain(handler, R.id.zx_decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (holder == null)
        {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface)
        {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult The contents of the barcode.
     * @param barcode   A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode)
    {
       // inactivityTimer.onActivity();

        if (ZXScanHelper.isPlaySoundOnRead())
        {
            beepManager.playBeep();
        }

        if (ZXScanHelper.isVibrateOnRead())
        {
            beepManager.vibrate();
        }

        if (barcode != null)
        {
            drawResultPoints(barcode, rawResult);
        }

        boolean accept = rawResult != null;

        if (accept && ZXScanHelper.getUserCallback() != null)
        {
            accept = ZXScanHelper.getUserCallback().onCodeRead(rawResult.getText());
        }

        if (accept)
        {
            handleDecodeExternally(rawResult, barcode);
        }
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
     *
     * @param barcode   A bitmap of the captured image.
     * @param rawResult The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, Result rawResult)
    {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0)
        {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.zx_result_image_border));
            paint.setStrokeWidth(3.0f);
            paint.setStyle(Paint.Style.STROKE);
            Rect border = new Rect(2, 2, barcode.getWidth() - 2, barcode.getHeight() - 2);
            canvas.drawRect(border, paint);

            paint.setColor(getResources().getColor(R.color.zx_result_points));
            if (points.length == 2)
            {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1]);
            } else if (points.length == 4 &&
                    (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
                            rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13))
            {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[1]);
                drawLine(canvas, paint, points[2], points[3]);
            } else
            {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points)
                {
                    canvas.drawPoint(point.getX(), point.getY(), paint);
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b)
    {
        canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
    }

    // Briefly show the contents of the barcode, then handle the result outside Barcode Scanner.
    private void handleDecodeExternally(Result rawResult, Bitmap barcode)
    {
        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
        intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());

        byte[] rawBytes = rawResult.getRawBytes();

        if (rawBytes != null && rawBytes.length > 0)
        {
            intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
        }

        Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();

        if (metadata != null)
        {
            if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION))
            {
                intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
                        metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
            }

            Integer orientation = (Integer) metadata.get(ResultMetadataType.ORIENTATION);
            if (orientation != null)
            {
                intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
            }

            String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
            if (ecLevel != null)
            {
                intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
            }

            Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
            if (byteSegments != null)
            {
                int i = 0;
                for (byte[] byteSegment : byteSegments)
                {
                    intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
                    i++;
                }
            }
        }

        sendReplyMessage(R.id.zx_return_scan_result, intent);
    }

    private void sendReplyMessage(int id, Object arg)
    {
        Message message = Message.obtain(handler, id, arg);
        long resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, DEFAULT_INTENT_RESULT_DURATION_MS);

        if (resultDurationMS > 0L)
        {
            handler.sendMessageDelayed(message, resultDurationMS);
        } else
        {
            handler.sendMessage(message);
        }
    }

    private void displayFrameworkBugMessageAndExit(Throwable err)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.zx_error));
        builder.setMessage(getString(R.string.zx_unexpected_camera_error, err.getClass().getName() + ": " + err.getMessage()));
        builder.setPositiveButton(R.string.zx_close, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS)
    {
        if (handler != null)
        {
            handler.sendEmptyMessageDelayed(R.id.zx_restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView()
    {

    }

    public void drawViewfinder()
    {
    }

    public void onConfigurationChanged(final Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
        try
        {
            if (cameraManager.isOpen())
            {
                cameraManager.forceSetCameraOrientation();
            }
        } catch (Throwable err)
        {
            err.printStackTrace();
        }
    }

    public Handler getHandler()
    {
        return handler;
    }

    protected CameraManager getCameraManager()
    {
        return cameraManager;
    }

      
    
  
   
   
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
        }
    }
    
    
    
    public void startQRScanner(SurfaceView surfaceView)
    {
    	if(conFrag.isAdded()){
    		  										// 1 is kept for shopping at store
    		conFrag.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int position) {
					
				}

				@Override
				public void onPageScrolled(int position, float arg1, int arg2) {
					
				}

				@Override
				public void onPageSelected(int position) {
					MenuItem CartMenuItem = (MenuItem) MenuReference.findItem(R.id.shopping_cart);
					 MenuItem ShopMap = (MenuItem) MenuReference.findItem(R.id.search);
					if(position == 0){
						shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_grey));
				    	shopByListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_blue));
				    	orderListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.purchase_order_grey));
						CartMenuItem.setVisible(true);
			            ShopMap.setVisible(false);
						getActionBar().setDisplayHomeAsUpEnabled(true);
			            getActionBar().setHomeButtonEnabled(true);
						mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ldrawer);
						window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						 
						
					}
					else if(position == 1){
						shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_blue));
				    	shopByListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_grey));
				    	orderListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.purchase_order_grey));
						CartMenuItem.setVisible(true);
				        ShopMap.setVisible(false);
						getActionBar().setDisplayHomeAsUpEnabled(false);
						getActionBar().setHomeButtonEnabled(false);
					    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
					    handler.resumeDecodeThread();
					   
					}
					else{
						shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_grey));
				    	shopByListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_grey));
				    	orderListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.purchase_order_blue));
						CartMenuItem.setVisible(true);
				        ShopMap.setVisible(false);
						window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
						getActionBar().setDisplayHomeAsUpEnabled(false);
						getActionBar().setHomeButtonEnabled(false);
			    		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
			    		
			    		
					}
					
				}
    			
    			}
    		);
	        cameraManager = new CameraManager(getApplication());
	
	        handler = null;
	        resetStatusView();
	      
	        
	        SurfaceHolder surfaceHolder = surfaceView.getHolder();
	        if (hasSurface)
	        {
	            // The activity was paused but not stopped, so the surface still exists. Therefore
	            // surfaceCreated() won't be called, so init the camera here.
	            initCamera(surfaceHolder);
	        } else
	        {
	            // Install the callback and wait for surfaceCreated() to init the camera.
	        	
	            surfaceHolder.addCallback(cameraSurfaceCallback);
	            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        }
	
	        
	
	        decodeFormats = DecodeFormatManager.QR_CODE_FORMATS; //todo: read from helper
	        characterSet = "utf-8";
	
	        
	        if (ZXScanHelper.getUserCallback() != null)
	        {
	            ZXScanHelper.getUserCallback().onScannerActivityResumed(this);
	        }
	        
	        
	    	
	       
        }
    }

    private void setCurrentShopping(int i) {
		    	
    	conFrag.mViewPager.setCurrentItem(i);
	}
    
    
   
    //Google Map Related Methods
	
	
	public void toggle_map()
	{
    	
    	
    	
		FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
		
		if(MapUI.mapVisible){
			map_container.setVisibility(View.INVISIBLE);
			MapUI.mapVisible = false;
		}
		else{
			map_container.setVisibility(View.VISIBLE);
			MapUI.mapVisible = true;
		}
	    	
	}
	
	public void mapShopContinue(View v)
	{   
		getSupportFragmentManager().executePendingTransactions();
	        FrameLayout map_container = (FrameLayout)findViewById(R.id.map_container);
			
			if(MapUI.mapVisible){
				map_container.setVisibility(View.INVISIBLE);
				MapUI.mapVisible = false;
			}
			
			MenuItem CartMenuItem = (MenuItem) MenuReference.findItem(R.id.shopping_cart);
			CartMenuItem.setVisible(true);
			MenuItem shopSearch = (MenuItem) MenuReference.findItem(R.id.search);
			shopSearch.collapseActionView();
			shopSearch.setVisible(false);
			Globals.delivery_location = new Location(MapUI.mMap.getCameraPosition().target.latitude,
					MapUI.mMap.getCameraPosition().target.longitude);
			actionBar.setTitle(getResources().getText(R.string.app_name));
			
			if(deliveryAddressInput.getText().toString().length()>0){
				Globals.delivery_address = deliveryAddressInput.getText().toString() + addressText;
				
			}
			mainFragmentContainer.setVisibility(View.VISIBLE);
			mainTabsView.setVisibility(View.VISIBLE);
			shopAtStoreButton.setVisibility(View.VISIBLE);
			shopByListButton.setVisibility(View.VISIBLE);
			orderListButton.setVisibility(View.VISIBLE);
			
	    	if(Globals.isInsideShop)
	    		
				setCurrentShopping(1);
			else{
				getActionBar().setDisplayHomeAsUpEnabled(true);
	            getActionBar().setHomeButtonEnabled(true);
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ldrawer);
		        setCurrentShopping(0);
			}
	    	location.removeLocationListener();
	    	
	    	MapUI.mMap.setMyLocationEnabled(false);
	    	MapUI.markerList=null;
	    	MapUI.mMap = null;
	    	MapUI.mMapFragment = null;
	    	MapUI.mMapView = null;
	    	
		
		
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		
		double lat = 	marker.getPosition().latitude;
		double lng = 	marker.getPosition().longitude;
		LatLng coordinate = new LatLng(lat, lng);
		
		Shop shopObject = Globals.get_shop_from_location(coordinate);
		if(shopObject == null){
			Toast.makeText(this, "Can not connect to this shop", Toast.LENGTH_SHORT).show();
			
			
		}
		else{
			
			if(Globals.connectedShop != null && shopObject.getName().equals(Globals.connectedShop.getName())){
				Toast.makeText(this, ("You are Connected to " + Globals.connectedShop.getName()), Toast.LENGTH_SHORT).show();
			}
			else{
				connect_to_shop(shopObject);
				
			}
		}
		return true;
	}
	
	
	//UI Buttons Methods
	
	public void shopsNearLocation(View v)
    {
		final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.buttonscale);
    	v.startAnimation(animScale);
    	toggle_map();
    }
	public void showCart(View v)
    {
		MenuItem shopSearch = (MenuItem) MenuReference.findItem(R.id.search);
    	
			
			//hiding cart
			if(!cartFrag.isDetached()){
        		getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_out,R.anim.fade_out).detach(cartFrag).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		if(conFrag.mViewPager.getCurrentItem() == 0){
        			getActionBar().setDisplayHomeAsUpEnabled(true);
        			getActionBar().setHomeButtonEnabled(true);
        		}
        		else if(conFrag.mViewPager.getCurrentItem() == 1){
        			handler.resumeDecodeThread();
        		}
        		actionBar.setTitle(getResources().getText(R.string.app_name));
        		shopSearch.setVisible(false);
        		importToCart.setVisible(false);
        	}
			//showing cart
    		else{
    			
    			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_in).attach(cartFrag ).addToBackStack(null).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		getActionBar().setDisplayHomeAsUpEnabled(false);
    			getActionBar().setHomeButtonEnabled(false);
    			shopSearch.setVisible(false);
    			actionBar.setTitle(getResources().getText(R.string.shopping_cart)+
        				"    " + Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0)) + " " +getResources().getText(R.string.currency));
    			importToCart.setVisible(true);
    		}
    		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
        	
    	
		
    }
    public void delete_last_scanned(View v)
    {
    	final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.buttonscale);
    	v.startAnimation(animScale);
    	if(Globals.item_order_list != null && Globals.item_order_list.size() > 0){
    		Globals.cartTotalPrice -= Globals.item_order_list.get(Globals.item_order_list.size()-1).getTotalPrice();
	    	OrderItemDetail itemToDelete = new OrderItemDetail(Globals.item_order_list.get(
	    			Globals.item_order_list.size()-1).getCurrentItemId(),Globals.item_order_list.get(Globals.item_order_list.size()-1).getCurrentQty());
	    	
	    	CartGlobals.recentDeletedItems.add(Globals.item_order_list.get(Globals.item_order_list.size()-1));
	    	
	    	Globals.item_added_list.remove(Globals.item_added_list.indexOf(
	    			Globals.item_order_list.get(Globals.item_order_list.size()-1).getCurrentItemId()));
		 	
	    	Globals.item_order_list.remove(Globals.item_order_list.get(Globals.item_order_list.size()-1));
	    	
	    	deletePackList(itemToDelete);
			Toast.makeText(this, "Last Scanned Product Removed", Toast.LENGTH_SHORT).show();
			
    	}
    	else{
    		Toast.makeText(this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
    		
    	}
    		
    }
    public void shopAtStore(View v)
    {
    	
    	setCurrentShopping(1); 
    }
    public void shopOutsideStore(View v)
    {
    	
    	setCurrentShopping(0);   
    }
	public void showOrderHistory(View v)
	{
		
		setCurrentShopping(2);
		
	}
	
	
	public void showFullShopDetails(View v)
	{
		
		if(Globals.connected_to_shop_success){
			MapUI.move_map_camera(new LatLng(Globals.connectedShop.getLocation().getLatitude(),
					Globals.connectedShop.getLocation().getLongitude()));
		}
	}
	
	public  void show_item_siblings(View view,Product item)
	{
	    	
	    	DrawerLayout itemDrawerLayout = (DrawerLayout)AddDialog.findViewById(R.id.drawer_add_item);
	    	FrameLayout itemDrawer = (FrameLayout)AddDialog.findViewById(R.id.simmilar_item_container);
	    	prgBar = new ProgressBar(this);
	    	FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    	lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
	    	itemDrawer.addView(prgBar,lp);
	    	itemDrawerLayout.openDrawer(itemDrawer);
	    	getItemList(item);
	}

	// Shop Methods
	
   
	@Override
	public void make_use_of_location() {
		
		ArrayList<Shop> connectedNearShoplist  = Globals.dbhelper.getNearConnectedShop(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		
		if(connectedNearShoplist == null){
			
			//get_shop_list(Globals.current_location);
		}
		
		else{
			//get_shop_list(Globals.current_location);
			//shop_list_success(Globals.current_location,connectedNearShoplist);
			
		}
		LatLng coordinate = new LatLng(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		MapUI.zoomInDeliveryLocation(coordinate);
		location.made_use_of_location = true;
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
		//MapUI.mMap.addMarker(new MarkerOptions().position(coordinate).draggable(false).title(Globals.connected_shop_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		
		shopDetailHeading.setText("Welcome to " + Globals.connectedShop.getName());
		
		shopDetailDescription.setText("You can shop various " +
				"products through your cam scanner " +
				"or through the list at an affordable prices.\nHappy Shopping!");
		
		startShop.setVisibility(View.VISIBLE);
	}

	public void get_shop_list(Location areaLocation)
	{
		Shop shopObj = new Shop();
		shopObj.get_shop_list(this,areaLocation);
	}
	
	@Override
	public void shop_list_success(Location areaLocation,ArrayList<Shop> shopList) {
		
			//MapUI.mMap.clear();
			
			if( shopList != null && shopList.size()>0){
				double map_center_lat = areaLocation.getLatitude();     // current lat and lng to create a range for blue marker representing shop in the range of 200 mtr
				double map_center_lng = areaLocation.getLongitude();
				
				//building markers and storing them
				for(int i = 0; i < shopList.size() ; i++ ){
					double shopLat =  shopList.get(i).getLocation().getLatitude();
					double shopLng =  shopList.get(i).getLocation().getLongitude();
					Marker shop_marker = null;
					
					if((Globals.current_location.getLatitude()<shopLat+0.0003) 
							&& (Globals.current_location.getLatitude() > shopLat-0.0003))
					{
						if((Globals.current_location.getLongitude() < shopLng+0.0003) 
								&& (Globals.current_location.getLongitude() > shopLng - 0.0003))
						{
							LatLng coordinate = new LatLng(shopLat, shopLng);
							Globals.add_to_sd_matrix(shopList.get(i),shopLat,shopLng);
							
							//add center  for shops
							shop_marker = MapUI.mMap.addMarker(new MarkerOptions().position(coordinate)
									.draggable(false).title(shopList.get(i).getName())
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_small)));
							//add circle boundary for shops
//							Circle  shopCircle = MapUI.mMap.addCircle(new CircleOptions()
//						     .center(coordinate)
//						     .radius(2000)
//						     .strokeColor(getResources().getColor(R.color.app_color))
//						     .strokeWidth(5)
//						     .fillColor(getResources().getColor(R.color.transparent_white)));
							MapUI.markerList.add(shop_marker);
							continue;
						}
					}
					
					LatLng coordinate = new LatLng(shopLat, shopLng);
					shop_marker = MapUI.mMap.addMarker(new MarkerOptions().position(coordinate).snippet("Shop")
							.draggable(false).title(shopList.get(i).getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_small)));
					MapUI.markerList.add(shop_marker);
					//add circle boundary for shops
//					Circle  shopCircle = MapUI.mMap.addCircle(new CircleOptions()
//				     .center(coordinate)
//				     .radius(2000)
//				     .strokeColor(getResources().getColor(R.color.app_color))
//				     .strokeWidth(5)
//				     .fillColor(getResources().getColor(R.color.transparent_white)));
//    	    
				}
				if(!Globals.near_shop_distance_matrix.isEmpty()){
					Shop shpObject = Globals.min_sd_matrix();				//find the shop which is very near to the location set by the camera
						//inside the shop
					connect_to_shop(shpObject);
					Globals.isInsideShop = true;
					shopDetailHeading.setText("Welcome to " + shpObject.getName());
					shopDetailDescription.setText("Happy Shopping!");
					startShop.setVisibility(View.VISIBLE);	
				}
					
				else 
				{
					// nearby the shop  
					Globals.isInsideShop = false;
					//shopDetailHeading.setText("Select a shop near your delivery location");
					if(!animationFlipClockWise){
						deliveryAddressView.setVisibility(View.VISIBLE);
						shopDetailsView.setVisibility(View.GONE);
					}else{
						if(shopList.size()>1){
						 shopDetailHeading.setText("");
						 shopDetailDescription.setText("Multiple Shops found tap shop marker to connect or drag map to change delivery location");
						 startShop.setVisibility(View.GONE);
						}
						else{
							shopDetailHeading.setText("");
							connect_to_shop( shopList.get(0));
							shopDetailDescription.setText("");
							startShop.setVisibility(View.GONE);	
						}
					}
					
				}
							
			}
			else{
				// no shop found
				shopDetailHeading.setText("No Shops to serve in this area");
				deliveryAddressView.setVisibility(View.GONE);
				shopDetailsView.setVisibility(View.VISIBLE);
				shopDetailDescription.setText("Drag map to change delivery Location");
				startShop.setVisibility(View.GONE);
			}
			
			   	       
		
//		Shop shpObject =new Shop();
//		shpObject.setName("CIty FOod Center");
//		shpObject.setUrl("planetp1940097436trial.hanatrial.ondemand.com/shop-sys/");
//		shpObject.setLocation(Globals.current_location);
//		connect_to_shop(shpObject);
		
		
	}
	
	


	
	//ProductVariance Methods
	

	@Override
	public void getItemList(Product item) {
		
		ProductVariance itm = new ProductVariance(0, null, 0, 0);
    	itm.getItems(this,item.getBrandId());
	}

	
	@Override
	public void ItemGetSuccess(final Product itemFetched) {
		
		if(itemFetched.getItemList().size()>0){
			
			Controls.show_alert_dialog(this, CaptureActivity.this, R.layout.activity_add_item,450);
	        
			BaseCardView itemContainer = (BaseCardView) AddDialog.findViewById(R.id.itemView);
				 addToItem = new AddItemCard(this, itemFetched);
				 addToItem.setParentView(this, itemContainer);
				 addToItem.setActionButtonOnClick(new OnClickActionButtonListener() {
					
					@Override
					public void onClick(Product item, View view) {
						show_item_siblings(view,itemFetched);
					}
				});
		}
		else{
			Toast.makeText(this, "Our Apologies, Product is out of Stock", Toast.LENGTH_LONG).show();
			handler.restartPreviewAndDecode();
		}
		//itemCard.setActionButtonText("More");
		

	}

	@Override
	public void ItemListGetSuccess(ArrayList<Product> itemSimmilarFamily) {
		
		FrameLayout drawerFrameLayout = (FrameLayout)AddDialog.findViewById(R.id.simmilar_item_container);
		drawerFrameLayout.removeView(prgBar);
		ListView itemDrawer = (ListView)AddDialog.findViewById(R.id.left_drawer_add_item);
		DrawerItemAdapter drawerItemAdapter = new DrawerItemAdapter(this,itemSimmilarFamily);
		drawerItemAdapter.setAddCardView(AddDialog);
		if(itemDrawer != null){
    		itemDrawer.setAdapter(drawerItemAdapter);
    	}

		
	}

	


	//Controls Interface

	
	
	@Override
	public void positive_button_alert_method() {
		/*addToItem is the current item in the add dialog, we add this in the
		*the Globals item order list which is connected to the UI interface of the 
		*cart fragment
		*/
		AddDialog.dismiss();
		//for(int i = 0 ; i < 10 ; i++){
		if(!Globals.item_added_list.contains(addToItem.getItem().getCurrentItemId())){
			Globals.item_added_list.add(addToItem.getItem().getCurrentItemId());
			Globals.item_order_list.add(addToItem.getItem()); 
			Globals.cartTotalPrice += addToItem.getItem().getTotalPrice();
		}
		else{
			Toast.makeText(this, "Product already present in your Cart", Toast.LENGTH_SHORT).show();
		}
		//}
		
		sendPackList();
		handler.restartPreviewAndDecode();
		
	}



	@Override
	public void negative_button_alert_method() {
		AddDialog.dismiss();
		handler.restartPreviewAndDecode();
	}



	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
			CaptureActivity.AddDialog = alertDialog;
	}
	@Override
	public void neutral_button_alert_method() {
		handler.restartPreviewAndDecode();
	}

	
	
	
	
	//PackList Method
	
	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#sendPackList()
	 */
	@Override
	public void sendPackList() {
			int count = Product.countNotSent(Globals.item_order_list);
		
		if(count >= Constants.MAX_NOT_SENT_ITEMS){
					
			PackList pl = new PackList();
			pl.products = Product.getToSendList(Globals.item_order_list);
			pl.state = DBState.INSERT;
		
			Product.setSentList(Globals.item_order_list);
			
			if(CartGlobals.CartServerRequestQueue.size() == 0){
				CartGlobals.CartServerRequestQueue.add(pl);
				pl.sendPackList(this);
			}
			else{
				CartGlobals.CartServerRequestQueue.add(pl);
			}
				
		}
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#PackListSuccess(com.shoplite.models.PackList)
	 */
	@Override
	public void PackListSuccess(PackList obj) {
		// TODO Auto-generated method stub
		if(obj.state==DBState.DELETE){
			for(int i = 0 ;i < obj.products.size() ; i++){
				if(CartGlobals.cartList.contains(obj.products.get(i)))
					CartGlobals.cartList.remove(obj.products.get(i));
				if(CartGlobals.recentDeletedItems.contains(obj.products.get(i)))
					CartGlobals.recentDeletedItems.remove(obj.products.get(i));
			}
			
		}
		else if (obj.state == DBState.INSERT){
			for(int i = 0 ;i < obj.products.size() ; i++){
				CartGlobals.cartList.add(obj.products.get(i));
			}
		}
		else{
			
		}
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#editPackList()
	 */
	@Override
	public void editPackList() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.PackListInterface#deletePackList(com.shoplite.models.OrderItemDetail)
	 */
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		// TODO Auto-generated method stub
		PackList pl = new PackList();
		pl.products = new ArrayList<OrderItemDetail>();
		pl.products.add(itemToDelete);
		pl.state = DBState.DELETE;
		CartGlobals.CartServerRequestQueue.add(pl);
		pl.sendPackList(this);
	}
	
	
	public void setDeliveryAnchor(View v)
	{
		if(deliveryAddressInput.getText().toString().length() <=0 ){
			deliveryAddressInput.setError(getResources().getString(R.string.error_field_required));
		}
		else{
			deliveryAddressInput.setError(null);
			
			//hide keyboard
			InputMethodManager inputManager = (InputMethodManager)
		            getSystemService(Context.INPUT_METHOD_SERVICE); 
		            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
		            InputMethodManager.HIDE_NOT_ALWAYS);
		     
			
		    //get camera center
			VisibleRegion visibleRegion = MapUI.mMap.getProjection().getVisibleRegion();
			Point x = MapUI.mMap.getProjection().toScreenLocation(visibleRegion.farRight);
			Point y = MapUI.mMap.getProjection().toScreenLocation(visibleRegion.nearLeft);
			
			Point centerPoint = new Point(x.x / 2, y.y / 2);
	
			LatLng centerFromPoint = MapUI.mMap.getProjection().fromScreenLocation(
			                    centerPoint);
			if(deliveryMarker == null){
			 deliveryMarker = MapUI.mMap.addMarker(new MarkerOptions().position(centerFromPoint)
					.draggable(false).title(getResources().getString(R.string.delivery_anchor_title)));
					//.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_small)));
			}
			else{
				deliveryMarker.remove();
				deliveryMarker = MapUI.mMap.addMarker(new MarkerOptions().position(centerFromPoint)
						.draggable(false).title(getResources().getString(R.string.delivery_anchor_title)));
				
			}
			
			//Make circle
			MapUI.mMap.setOnCameraChangeListener(null);
		    
			MapUI.zoomOutDeliveryLocation(centerFromPoint, new CancelableCallback() {
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					      /* Do something… */
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
					  @Override
					  public void run() {
					   
						  MapUI.mMap.setOnCameraChangeListener(onCameraChange);
							//Do something after 100ms
					  }
					}, 500);  
							
					  
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			});
			
			if(circle == null){
			  circle = MapUI.mMap.addCircle(new CircleOptions()
		     .center(centerFromPoint)
		     .radius(2000)
		     .strokeColor(getResources().getColor(R.color.app_color))
		     .strokeWidth(5)
		     .fillColor(getResources().getColor(R.color.transparent_white)));
			}
			else{
				circle.remove();
				circle = MapUI.mMap.addCircle(new CircleOptions()
			     .center(centerFromPoint)
			     .radius(2000)
			     .strokeColor(getResources().getColor(R.color.app_color))
			     .strokeWidth(5)
			     .fillColor(getResources().getColor(R.color.transparent_white)));
			}
			
			ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.anim.flip_clockwise); 
			anim.setTarget(mapDialogView);
			anim.setDuration(500);

			
			
			anim.addListener(new AnimatorListener() {
				
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					shopDetailsView.setRotationX(180);
					deliveryAddressView.setRotationX(180);
					deliveryAddressView.setVisibility(View.GONE);
					shopDetailsView.setVisibility(View.VISIBLE);
					animationFlipClockWise = true;
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
						
					//MapUI.mMap.setOnCameraChangeListener(onCameraChange);
				    
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
						
				}
			});
			anim.start();
			
			
		    
			//v.setVisibility(View.GONE);
			//deliveryDialogText.setText("Delivery Address" + addressText + "\n Tap home to modify.");
			//shopDetailHeading.setText("Delivery Address set as");
			get_shop_list(new Location(centerFromPoint.latitude,centerFromPoint.longitude));
			animationFlipClockWise = true;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ItemInterface#ItemGetFailure()
	 */
	@Override
	public void ItemGetFailure() {
		// TODO Auto-generated method stub
		handler.restartPreviewAndDecode();
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ItemInterface#getItem()
	 */
	@Override
	public void getItem() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ItemInterface#updateItemSuccess(com.shoplite.models.Product)
	 */
	@Override
	public void updateItemSuccess(Product product) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.shoplite.interfaces.ItemInterface#updateItemFailure()
	 */
	@Override
	public void updateItemFailure() {
		// TODO Auto-generated method stub
		
	}
}
