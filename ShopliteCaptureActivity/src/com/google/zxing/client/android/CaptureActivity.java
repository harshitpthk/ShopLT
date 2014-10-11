

package com.google.zxing.client.android;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.shoplite.UI.Controls;
import com.shoplite.UI.ItemThumbnail;
import com.shoplite.UI.MapUI;
import com.shoplite.UI.ViewAnimation;
import com.shoplite.Utils.Globals;
import com.shoplite.Utils.location;
import com.shoplite.connection.ConnectionInterface;
import com.shoplite.connection.ServiceProvider;
import com.shoplite.fragments.CameraFragment;
import com.shoplite.fragments.CartFragment;
import com.shoplite.fragments.ContainerFragment;
import com.shoplite.fragments.OrderFragment;
import com.shoplite.interfaces.ItemInterface;
import com.shoplite.interfaces.LocationInterface;
import com.shoplite.interfaces.ShopInterface;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.Shop;
import android.widget.FrameLayout;

import eu.livotov.zxscan.R;
import eu.livotov.zxscan.ZXScanHelper;


/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * QRCodeView to help the user place the QRCode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * @author Harshit Pathak
 */

public class CaptureActivity extends FragmentActivity  implements SurfaceHolder.Callback,ConnectionInterface,ShopInterface,LocationInterface,ItemInterface,OnMarkerClickListener, OnCameraChangeListener
{

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1L;
	public static AlertDialog AddDialog;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    //  private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    public ListView ldrawer;
    public DrawerLayout mDrawerLayout;
    public CartFragment cartFrag = new CartFragment();
    public OrderFragment orderFrag = new OrderFragment();
    public CameraFragment camFrag = new CameraFragment();
    public ContainerFragment conFrag = new ContainerFragment();
    private String[] main_action = {"Groceries","Fruits & Veg","Beverages/Health-Drinks","Dairy/Eggs","Ready to Eat/Packed Foods","Personnel Care","Household","Stationery"};
    SurfaceHolder.Callback cameraSurfaceCallback = this;
    private ActionBarDrawerToggle mDrawerToggle;
    private Window window;
	
   
    
    
       
   
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
        
        if(!conFrag.isAdded()){
        	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,conFrag ).commit();
        	
        }
        
        getSupportFragmentManager().executePendingTransactions();
        conFrag.getChildFragmentManager().executePendingTransactions();
        
      
        if(!cartFrag.isAdded()){
        	getSupportFragmentManager().beginTransaction().add(R.id.container,cartFrag ).detach(cartFrag).commit();
        	
        }
        
        getSupportFragmentManager().executePendingTransactions();
        
        
        
        
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

        
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(false);
	   
        ldrawer = (ListView)this.findViewById(R.id.left_drawer_capture);
     
        ldrawer.setAdapter(new ArrayAdapter<String>(this.getBaseContext(),
                R.layout.drawer_list_item, main_action));
        // Set the list's click listener
    	ldrawer.setOnItemClickListener(new DrawerItemClickListener());
    	MapUI.mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment));
    	MapUI.mMap = MapUI.mMapFragment.getMap();
       
        hasSurface = false;
       // inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        if (ZXScanHelper.getUserCallback() != null)
        {
            ZXScanHelper.getUserCallback().onScannerActivityCreated(this);
        }
		
        
       // toggle_map();
		
		
        
    }
  
   

	@Override
    protected void onResume()
    {
        super.onResume();
       // inactivityTimer.onResume();
        location loc = new location();
		loc.getLocation(this,this);
		
        
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        
		// initially hide map;
		
		MapUI.mMap.setOnCameraChangeListener(this);
		
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
            		getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slidein_opposite,R.anim.slideout_opposite).detach(cartFrag).commit();
            		//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ldrawer);
                	
            	}else{
            		setResult(RESULT_CANCELED);
            		finish();
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
    	
    	 if(item.getItemId() == R.id.search) {
         
             toggle_map();
             return true;
    	}
    	else if(item.getItemId() == R.id.shopping_cart){
         
        	 showCart(null);
             return true;
    	}
    	else
             return super.onOptionsItemSelected(item);
     }
       
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_action_bar, menu);
 
        return super.onCreateOptionsMenu(menu);
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

    
    
    
    
    
    // custom methods added on top of capture activity
    public void left_drawer_open(View v){
    	    	
    	final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.navigation_drawer_button);
    	v.startAnimation(animScale);
    	
    //	mDrawerLayout.openDrawer(ldrawer);
    	
    	
    	//mDrawerLayout.closeDrawer(rdrawer);
    	
    }
   
   
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
        }
    }
    
    
    public void showCart(View v)
    {
    	
    		
    		
    		if(cartFrag.isAdded()){
        		getSupportFragmentManager().beginTransaction().detach(cartFrag).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ldrawer);
        	}
    		else{
    			getSupportFragmentManager().beginTransaction().attach(cartFrag ).addToBackStack(null).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
    		}
        	
    	
		
    }
    public void initiateCamera(SurfaceView surfaceView)
    {
    	if(conFrag.isAdded()){
    		setCurrentShopping(1);   										// 1 is kept for shopping at store
    		conFrag.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int position) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPageScrolled(int position, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					if(position == 0){
						getActionBar().setDisplayHomeAsUpEnabled(true);
			            getActionBar().setHomeButtonEnabled(true);
						mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, ldrawer);
						window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					}
					else if(position == 1){
						getActionBar().setDisplayHomeAsUpEnabled(false);
						getActionBar().setHomeButtonEnabled(false);
					    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, ldrawer);
						
					}
					else{
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
		// TODO Auto-generated method stub
    	
    	conFrag.mViewPager.setCurrentItem(i);
	}
    
    
    public void delete_last_scanned(View v)
    {
    	final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.buttonscale);
    	v.startAnimation(animScale);
    	cartFrag.remove_scanned_item("capture_activity",Globals.item_order_list.size()-1);
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
	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		LinearLayout connected_shop_details_view = (LinearLayout)findViewById(R.id.connected_shop_details);
		ViewAnimation.slideToBottom(connected_shop_details_view);
		
		
	}
	
	public void show_item_siblings(View view)
	{
	    	
	    	DrawerLayout itemDrawerLayout = (DrawerLayout)AddDialog.findViewById(R.id.drawer_add_item);
	    	ListView itemDrawer = (ListView)AddDialog.findViewById(R.id.left_drawer_add_item);
	    	itemDrawerLayout.openDrawer(itemDrawer);
			
	}
	public void shopsNearLocation(View v)
    {
		final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.buttonscale);
    	v.startAnimation(animScale);
    	toggle_map();
    }
	
	protected void connect_to_shop(Shop shopObj) {
		
		String shopURL =shopObj.getUrl();
		String shopName = shopObj.getName();
		com.shoplite.models.Location shopLoc = shopObj.getLocation();
		shopObj.connect_to_shop(this,shopURL,shopName,shopLoc);
		
		Toast.makeText(getBaseContext(), ("Connecting to " + shopName), Toast.LENGTH_SHORT).show();
    	
	}
	
	public void get_shop_list()
	{
		
		Shop shopObj = new Shop();
		shopObj.get_shop_list(this);
	}
		
    @Override
	public void sendRequest(ServiceProvider serviceProvider) {
				
		
	}


	
   
	@Override
	public void make_use_of_location() {
		
		ArrayList<Shop> connectedNearShoplist  = Globals.dbhelper.getNearConnectedShop(Globals.current_location.getLatitude(), Globals.current_location.getLongitude());
		
		if(connectedNearShoplist == null){
			
			get_shop_list();
		}
		
		else{
			Globals.shop_list = connectedNearShoplist;
			Globals.shop_list_near.clear();
			Globals.shop_list_fetch_success = true;
			
			shop_list_success();
			get_shop_list();
		}
		
		location.made_use_of_location = true;
	}


	@Override
	public void shop_list_success() {
		
		if(Globals.shop_list_fetch_success){
			if( Globals.shop_list != null && Globals.shop_list.size()>0){
				MapUI.mMap.setOnMarkerClickListener(this);
				for(int i = 0; i < Globals.shop_list.size() ; i++ ){
					double lat =  Globals.shop_list.get(i).getLocation().getLatitude();
					double lng =  Globals.shop_list.get(i).getLocation().getLongitude();
					double cur_lat = Globals.current_location.getLatitude();
					double cur_lng = Globals.current_location.getLongitude();
					Marker shop_marker = null;
					
					if((cur_lat<lat+0.0009) && (cur_lng > lat-0.0009)){
						if((cur_lng < lng+0.0009) && (cur_lng > lng - 0.0009)){
							LatLng coordinate = new LatLng(lat, lng);
							Globals.add_to_sd_matrix(Globals.shop_list.get(i),lat,lng);
							shop_marker = MapUI.mMap.addMarker(new MarkerOptions().position(coordinate).draggable(false).title(Globals.shop_list.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
							continue;
						}
					}
					LatLng coordinate = new LatLng(lat, lng);
					
					shop_marker = MapUI.mMap.addMarker(new MarkerOptions().position(coordinate).draggable(false).title(Globals.shop_list.get(i).getName()));
    	    
				}
				Shop shpObject = Globals.min_sd_matrix();
				if(!Globals.connected_to_shop_success)
					connect_to_shop(shpObject);
			}
			
			
			
    	       
		}
//		Shop shpObject =new Shop();
//		shpObject.setName("CIty FOod Center");
//		shpObject.setUrl("planetp1940097444trial.hanatrial.ondemand.com/shop-sys/");
//		shpObject.setLocation(Globals.current_location);
//		connect_to_shop(shpObject);
		
		double lat = Globals.current_location.getLatitude();
		double lng = Globals.current_location.getLongitude();
		LatLng coordinate = new LatLng(lat, lng);
		MapUI.move_map_camera(coordinate);
	}

	public void mapShopSearch(View v)
	{
		EditText ev = (EditText)findViewById(R.id.map_search_tv);
		
		MapUI.search_location(ev.getText().toString());
	}

	


	@Override
	public void shop_connected() {
		double lat =  Globals.connected_shop_location.getLatitude();
		double lng =  Globals.connected_shop_location.getLongitude();
		
		LatLng coordinate = new LatLng(lat, lng);
		MapUI.mMap.addMarker(new MarkerOptions().position(coordinate).draggable(false).title(Globals.connected_shop_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		//Toast.makeText(this.getApplicationContext(), Globals.connected_shop_name, Toast.LENGTH_SHORT).show();
   	 
	}


	@Override
	public boolean onMarkerClick(Marker marker) {
		
		double lat = 	marker.getPosition().latitude;
		double lng = marker.getPosition().longitude;
		LatLng coordinate = new LatLng(lat, lng);
		
		Shop shopObject = Globals.get_shop_from_location(coordinate,MapUI.mMap);
		if(shopObject == null){
			Toast.makeText(this, "This shop is out of range", Toast.LENGTH_LONG).show();
			//Options for off-line shopping after proper message
			
		}
		else{
			if(shopObject.getName().equals(Globals.connected_shop_name)){
				Toast.makeText(this, ("You are Connected to " + Globals.connected_shop_name), Toast.LENGTH_LONG).show();
			}
			else{
				connect_to_shop(shopObject);
				
			}
		}
		return false;
	}


	@Override
	public void ItemAdded() {
		
		
	}


	@Override
	public void ItemGetSuccess() {
		Card card = new Card(getBaseContext());
		CardHeader header = new CardHeader(getBaseContext());
		ItemThumbnail thumbnail = new ItemThumbnail(getBaseContext(),"Globals.fetched_item_category.getThumbnail()");
		thumbnail.setExternalUsage(true);
		header.setTitle(Globals.fetched_item_category.getName());
		card.addCardHeader(header);
		card.addCardThumbnail(thumbnail);
		card.setTitle("");
		CardView cardView = (CardView)AddDialog.findViewById(R.id.cardview);
		cardView.setCard(card);
		cardView.setVisibility(1);
		Controls.show_add_item_dialog_spinner(this,AddDialog,Globals.fetched_item_category);
	}


	@Override
	public void ItemListGetSuccess() {
			ArrayList<Card> cards = new ArrayList<Card>();
		
			for(int i = 0 ; i < Globals.simmilar_item_list.size();i++){
				final ItemCategory itemFamily = Globals.simmilar_item_list.get(i);
				Card card = new Card(getBaseContext());
				CardHeader header = new CardHeader(getBaseContext());
				
				header.setTitle(Globals.simmilar_item_list.get(i).getName());
				header.setOtherButtonVisible(true);
				final CardView cardView = (CardView)AddDialog.findViewById(R.id.cardview);
	                               
				header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener() {
		            @Override
		            public void onButtonItemClick(Card card, View view) {
		                Toast.makeText(getApplicationContext(), "Click on Other Button", Toast.LENGTH_LONG).show();
		                card.getCardHeader().setOtherButtonVisible(false);
		                
		                cardView.replaceCard(card);
		                Controls.show_add_item_dialog_spinner(getApplicationContext(),AddDialog,itemFamily);
		            }
		        });
				
				card.addCardHeader(header);
				ItemThumbnail thumbnail = new ItemThumbnail(getBaseContext(),"Globals.simmilar_item_list.get(i).getThumbnail()");
				thumbnail.setExternalUsage(true);
				card.addCardThumbnail(thumbnail);
				//card.setOnClickListener(onClickListener);
		        cards.add(card);
		        
			}
	      
			CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);

	        CardListView listView = (CardListView) AddDialog.findViewById(R.id.left_drawer_add_item);
	        if (listView!=null){
	            listView.setAdapter(mCardArrayAdapter);
	        }
		
		
	}



	
}
