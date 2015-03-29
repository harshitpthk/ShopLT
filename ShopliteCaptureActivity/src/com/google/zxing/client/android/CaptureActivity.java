

package com.google.zxing.client.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.homelybuysapp.UI.AddItemCard;
import com.homelybuysapp.UI.BaseCardView;
import com.homelybuysapp.UI.ButteryProgressBar;
import com.homelybuysapp.UI.Controls;
import com.homelybuysapp.UI.BaseItemCard.OnClickActionButtonListener;
import com.homelybuysapp.Utils.CartGlobals;
import com.homelybuysapp.Utils.Constants;
import com.homelybuysapp.Utils.Globals;
import com.homelybuysapp.Utils.Constants.DBState;
import com.homelybuysapp.fragments.CartFragment;
import com.homelybuysapp.fragments.ContainerFragment;
import com.homelybuysapp.fragments.OfflineShopFrag;
import com.homelybuysapp.interfaces.CategoryInterface;
import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.interfaces.ItemInterface;
import com.homelybuysapp.interfaces.MapInterface;
import com.homelybuysapp.interfaces.PackListInterface;
import com.homelybuysapp.interfaces.ShopInterface;
import com.homelybuysapp.models.Category;
import com.homelybuysapp.models.Input;
import com.homelybuysapp.models.Location;
import com.homelybuysapp.models.OrderItemDetail;
import com.homelybuysapp.models.PackList;
import com.homelybuysapp.models.Product;
import com.homelybuysapp.models.ProductVariance;
import com.homelybuysapp.models.Shop;
import com.homelybuysapp.zxscan.ZXScanHelper;

import eu.livotov.zxscan.R;



public class CaptureActivity extends ActionBarActivity  implements SurfaceHolder.Callback,CategoryInterface ,ItemInterface,MapInterface,ShopInterface,ControlsInterface,PackListInterface
{

    private static final String TAG = CaptureActivity.class.getSimpleName();  // Log tags
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1L;		  // Capture Activity Intent Result Duration
	
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
   // private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    SurfaceHolder.Callback cameraSurfaceCallback = this;
    
    private RelativeLayout drawerSearch ;
    public static ButteryProgressBar butteryProgressBar;
    public static FrameLayout decorView;
     
    private ListView ldrawer;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mainDrawerContainer;
    
    private ActionBarDrawerToggle mDrawerToggle;
        
    private Window window;
	public static  SearchView shopSearchView = null;
    public ProgressBar  prgBar;
    public static boolean isProgressBarAdded;
    private ImageButton shopByListButton;
   // private ImageButton shopAtStoreButton;
    private ImageButton orderListButton;
    private static AlertDialog AddDialog;
    public static AddItemCard addToItem;
    public static android.support.v7.app.ActionBar actionBar;
  
	protected static DialogFragment saveListDialog;
	public static int SAVE_LIST_REQUEST = 200;
	
	CartFragment cartFrag ;
    ContainerFragment conFrag ;
    
//    MenuItem CartMenuItem = null;
    ArrayList<String>  mainCategories = new ArrayList<String>();
	private ArrayList<Category> categoryMap;
    public static Toolbar toolbar ;
    static final int PICK_DELIVERY_REQUEST = 1;
	
    private TextView drawerHeaderName;
    private TextView drawerHeaderAddress;
    private TextView drawerHeaderShop;
    private ImageButton drawerChangeDelivery;
    private CaptureActivity currentActivityInstance;
    private ProgressBar pbChangeDelivery;
	public static TextView productsNumberView;
    //activity methods
   	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle icicle)
    {
		  
		super.onCreate(icicle);

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//Above setContentView, very important

	    int scanner_layout = R.layout.scanner_layout_capture;                 	// setting the custom layout on top of capture activity
	    
		setContentView(scanner_layout);
      
        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_app_color));
        }
       	
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //action Bar
		actionBar = getSupportActionBar();
		
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		currentActivityInstance = this;
	    setSupportProgressBarIndeterminateVisibility(true);

		 new LoadUI().execute();
		
		
//        Shop shopObj = new Shop();
//		shopObj.get_shop_list(this,Globals.deliveryAddress.getDeliveryLocation());
               
    }
     
	
	

	@Override
    protected void onResume()
    {
        super.onResume();
        // inactivityTimer.onResume();
       		if(productsNumberView != null){
       			productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
       		}
		
		
		
    }

    @Override
	public void onPause()
    {
    	super.onPause();
    	//inactivityTimer.onPause();
    	/*if(conFrag.isAdded()){
	        if (handler != null)
	        {
	            handler.quitSynchronously();
	            handler = null;
	        }
	        if(cameraManager != null)
	        	cameraManager.closeDriver();
	        if (!hasSurface)
	        {
	            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
	            SurfaceHolder surfaceHolder = surfaceView.getHolder();
	            surfaceHolder.removeCallback(this);
	        }
    	}*/
        
    }

    @Override
    protected void onDestroy()
    {
    	/*if(conFrag.isAdded()){
	       // inactivityTimer.shutdown();
	        if (ZXScanHelper.getUserCallback() != null)
	        {
	            ZXScanHelper.getUserCallback().onScannerActivityDestroyed(this);
	        }
    	}*/
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        
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
                //cameraManager.setTorch(false);
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                //cameraManager.setTorch(true);
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
//    	 else if(item.getItemId() == R.id.settings){
//    		 Intent i = new Intent(this, SettingsActivity.class);
//    		 startActivity(i);
//    		 return true;
//    	 }
    	else
             return super.onOptionsItemSelected(item);
     }
          
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_action_bar, menu);
        MenuItem cart =  menu.findItem(R.id.shopping_cart);
        MenuItemCompat.setActionView(cart, R.layout.custom_cart_icon);
        RelativeLayout cartIconContainer = (RelativeLayout)MenuItemCompat.getActionView(cart);
        productsNumberView = (TextView) cartIconContainer.findViewById(R.id.actionbar_notifcation_textview);
        ImageButton cartButton = (ImageButton)cartIconContainer.findViewById(R.id.cart_icon);
        cartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showCart(v);
			}
		});
        productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
        return super.onCreateOptionsMenu(menu);
    }
       
    //map activity changes
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	    	
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
                // calledfor importList
            }
        }
        if (requestCode == PICK_DELIVERY_REQUEST) {
		      if (resultCode == RESULT_OK) {
		    	  Shop shopObj = new Shop();
		  		  shopObj.get_shop_list(this,Globals.deliveryAddress.getDeliveryLocation());
		    	  
		         
		      } else if (resultCode == RESULT_CANCELED) {
		         
		      }

				pbChangeDelivery.setVisibility(View.GONE);
				drawerChangeDelivery.setVisibility(View.VISIBLE);
		}
    }
    
    
    //Zxing Library Methods
    
    
    public void initCamera(SurfaceHolder surfaceHolder)
    {
    	 // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
    	
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
        	mDrawerLayout.closeDrawer(mainDrawerContainer);
        	showSubCategories(position);
        	
        }

		/**
		 * 
		 */
		
    }
    
    public void startQRScanner(SurfaceView surfaceView)
    {
    	// CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
    	if(conFrag.isAdded()){
    		  										// 1 is kept for shopping at store
    		
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
    
     	
	//UI Buttons Methods
    public void showCart(View v)
    {
    	
    	
			//hiding cart
			if(!cartFrag.isDetached()){
        		getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_out,R.anim.fade_out).detach(cartFrag).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		if(conFrag.mViewPager.getCurrentItem() == 0){
        			toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        			getSupportActionBar().setHomeButtonEnabled(true);
        			 mDrawerToggle.setDrawerIndicatorEnabled(true);
        			 mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mainDrawerContainer);
        		}
        		else if(conFrag.mViewPager.getCurrentItem() == 1){
        			if(handler != null){
        				handler.resumeDecodeThread();
        			}
        		}
        		actionBar.setTitle(getResources().getText(R.string.app_name));


        	}
			//showing cart
    		else{
    			
    			getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_in).attach(cartFrag ).addToBackStack(null).commit();
        		getSupportFragmentManager().executePendingTransactions();
        		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				getSupportActionBar().setHomeButtonEnabled(false);
				mDrawerToggle.setDrawerIndicatorEnabled(false);
				toolbar.setNavigationIcon(null);
    			 
    			 mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mainDrawerContainer);

    			actionBar.setTitle(getResources().getText(R.string.shopping_cart)+
        				"    " + Double.toString(Math.round(Globals.cartTotalPrice*100.0/100.0)) + " " +getResources().getText(R.string.currency));

    		}
    		
				
    	
		
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
    	
    	//setCurrentShopping(1); 
    }
    public void shopOutsideStore(View v)
    {
    	
    	setCurrentShopping(0);   
    }
	public void showOrderHistory(View v)
	{
		
		setCurrentShopping(1);
		
	}
	
	
	private void initDrawer(){
		drawerHeaderShop.setText(Globals.connectedShop.getName());
		drawerHeaderAddress.setText(Globals.deliveryAddress.getAddressString());
		drawerHeaderName.setText(Globals.dbhelper.getItem("name"));
		
		drawerChangeDelivery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Globals.item_added_list.size() >0){
					AlertDialog.Builder alert = new AlertDialog.Builder( currentActivityInstance);
					AlertDialog alertDialog = alert.create();
					
					
					alertDialog.setTitle("Are You Sure?");
					alertDialog.setMessage("Changing Delivery Location will Remove Products from the Cart.");
					alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							dialog.dismiss();
						}
					});
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "PROCEED", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
							Globals.resetCartData();
							productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
							Intent in = 	new Intent(currentActivityInstance,com.homelybuysapp.activities.MapActivity.class);
							in.putExtra("instantiator","captureactivity");

						 
							startActivityForResult(in, PICK_DELIVERY_REQUEST);						}
					});
					alertDialog.show();

							
					
				}
				else{

					drawerChangeDelivery.setVisibility(View.GONE);
					pbChangeDelivery.setVisibility(View.VISIBLE);
					Intent in = 	new Intent(currentActivityInstance,com.homelybuysapp.activities.MapActivity.class);
					in.putExtra("instantiator","captureactivity");

				 
					startActivityForResult(in, PICK_DELIVERY_REQUEST);					}
				
			}
		});
		
		if(mainCategories.size()<= 0)
    		loadCategories();
	}
	
	private void loadCategories() {
		prgBar = new ProgressBar(this);
    	FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
    	mainDrawerContainer.addView(prgBar,lp);
		Category cat = new Category();
		cat.getCategories(this);
		
	}
	private void showSubCategories(int position) {
		
		OfflineShopFrag offlineFrag = (OfflineShopFrag) conFrag.mContainerFragPager.getCurrentFragment();
		if(offlineFrag != null){
			Log.e("ItemClicked",categoryMap.get(position).getName());
			ArrayList<Category> shopSubCategoryList = new ArrayList<Category>();
			ArrayList<Category> subCategoryList = categoryMap.get(position).getChildList();
			Collections.sort(subCategoryList, new Category.CategoryComparator()); 
			Category cat;
			for(int i = 0 ; i < subCategoryList.size();i++){
				 cat = subCategoryList.get(i);
				 if(cat.getRank()>0)
					 shopSubCategoryList.add(cat);
			}
			offlineFrag.loadChildCategories(shopSubCategoryList,categoryMap.get(position).getName());
		}
		
	}
	protected void showSearchItems() {
		
		OfflineShopFrag offlineFrag = (OfflineShopFrag) conFrag.mContainerFragPager.getCurrentFragment();
		if(offlineFrag != null){
			offlineFrag.showSearchItems();
		}
	}
	//Item/ProductVariance Methods
	
	
	
	public  void show_item_siblings(View view,Product item)
	{
	    	
//	    	DrawerLayout itemDrawerLayout = (DrawerLayout)AddDialog.findViewById(R.id.drawer_add_item);
//	    	FrameLayout itemDrawer = (FrameLayout)AddDialog.findViewById(R.id.simmilar_item_container);
//	    	prgBar = new ProgressBar(this);
//	    	FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//	    	lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
//	    	itemDrawer.addView(prgBar,lp);
//	    	itemDrawerLayout.openDrawer(itemDrawer);
//	    	getItemList(item);
	}
	
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
			if(handler != null && conFrag.mViewPager.getCurrentItem() == 1)
				handler.restartPreviewAndDecode();
		}
		//itemCard.setActionButtonText("More");
		

	}

	@Override
	public void ItemListGetSuccess(ArrayList<Product> itemSimmilarFamily) {
		
//		FrameLayout drawerFrameLayout = (FrameLayout)AddDialog.findViewById(R.id.simmilar_item_container);
//		drawerFrameLayout.removeView(prgBar);
//		ListView itemDrawer = (ListView)AddDialog.findViewById(R.id.left_drawer_add_item);
//		DrawerItemAdapter drawerItemAdapter = new DrawerItemAdapter(this,itemSimmilarFamily);
//		drawerItemAdapter.setAddCardView(AddDialog);
//		if(itemDrawer != null){
//    		itemDrawer.setAdapter(drawerItemAdapter);
//    	}

		
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
			productsNumberView.setText(String.valueOf(Globals.item_order_list.size()));
			Toast.makeText(this, "Product added to your Cart", Toast.LENGTH_SHORT).show();

		}
		else{
			Toast.makeText(this, "Product already present in your Cart", Toast.LENGTH_SHORT).show();
		}
		//}
		
//		sendPackList();
//		if(handler != null && conFrag.mViewPager.getCurrentItem() == 1)
//			handler.restartPreviewAndDecode();
		
	}
	@Override
	public void negative_button_alert_method() {
		AddDialog.dismiss();
//		if(handler != null && conFrag.mViewPager.getCurrentItem() == 1)
//			handler.restartPreviewAndDecode();
	}
	@Override
	public void save_alert_dialog(AlertDialog alertDialog) {
			CaptureActivity.AddDialog = alertDialog;
	}
	@Override
	public void neutral_button_alert_method() {
//		if(handler != null && conFrag.mViewPager.getCurrentItem() == 1)
//			handler.restartPreviewAndDecode();
	}

	
	
	
	
	/*
	 * 
	 * PackList Interface Method
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

	
	@Override
	public void PackListSuccess(PackList obj) {
		
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

	
	@Override
	public void editPackList() {
		
		
	}

	
	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		
		PackList pl = new PackList();
		pl.products = new ArrayList<OrderItemDetail>();
		pl.products.add(itemToDelete);
		pl.state = DBState.DELETE;
		CartGlobals.CartServerRequestQueue.add(pl);
		pl.sendPackList(this);
	}
	
	

	
	@Override
	public void ItemGetFailure() {
		if(handler != null && conFrag.mViewPager.getCurrentItem()== 1)
			handler.restartPreviewAndDecode();
	}

	
	@Override
	public void getItem() {
		
	}

	
	@Override
	public void updateItemSuccess(Product product) {
		
	}

	
	@Override
	public void updateItemFailure() {
		
	}

	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.MapInterface#mapShopStart()
	 */
	@Override
	public void mapShopStart() {
		
		/*
		
				
    	if(Globals.isInsideShop)
    		
			setCurrentShopping(1);
		else{
			
	        setCurrentShopping(0);
		}
		*/
    	
    	
	}
	

	
	@Override
	public void shop_list_success(Location areaLocation,
			ArrayList<Shop> shoplist) {
		
		for(int i = 0 ; i < shoplist.size(); i++){
			Log.e("ShopNames",shoplist.get(i).getName());
		}
		
		if( shoplist != null && shoplist.size()>0){
			
			
			for(int i = 0; i < shoplist.size() ; i++ ){
				double shopLat =  shoplist.get(i).getLocation().getLatitude();
				double shopLng =  shoplist.get(i).getLocation().getLongitude();
				Globals.add_to_sd_matrix(shoplist.get(i),shopLat,shopLng,areaLocation);
			}
			 
			Shop mostNearByShop = Globals.min_sd_matrix();
			mostNearByShop.connect_to_shop(this);
		}
		else{
			Toast.makeText(this, "No Shops found at your delivery Location", Toast.LENGTH_LONG).show();
		}
	}

	
	@Override
	public void shop_connected() {
		
		initDrawer();
	}

	
	@Override
	public void getCategorySuccess(ArrayList<Category> categories) {
		 
		//{"Groceries","Fruits & Veg","Beverages/Health-Drinks","Dairy/Eggs","Ready to Eat/Packed Foods","Personnel Care","Household","Stationery"};
		categoryMap = categories;
		
		//Collections.sort(categoryMap, new Category.CategoryComparator()); 
		Category cat;
		for(int i = 0 ; i < categories.size();i++){
			 cat = categories.get(i);
			 if(cat.getRank()>0)
				mainCategories.add(cat.getName());
		}
		
		mainDrawerContainer.removeView(prgBar);
		ldrawer.setAdapter(new ArrayAdapter<String>(this.getBaseContext(),R.layout.drawer_list_item, mainCategories));
        ldrawer.setOnItemClickListener(new DrawerItemClickListener());// Set the list's click listener
	}

		
	@Override
	public void productsGetFailure() {
		
	}

	
	@Override
	public void productsGetSuccess(ArrayList<Product> productList) {
		
	}

	
	@Override
	public void getProducts(Input input) {
		
	}




	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#searchProductFailure()
	 */
	@Override
	public void searchProductFailure() {
		
	}




	/* (non-Javadoc)
	 * @see com.homelybuysapp.interfaces.ItemInterface#productSearchSuccess(java.util.ArrayList)
	 */
	@Override
	public void productSearchSuccess(ArrayList<Product> productList) {
		
	}
	
	private class LoadUI extends AsyncTask<String, Integer, String> {
		  

		@Override
		   protected void onPreExecute() {
		      super.onPreExecute();
		      
		   	Controls.show_loading_dialog(currentActivityInstance,getString(R.string.taking_to_shop_message));

		    	    
		     
		   }

		   @Override
		   protected String doInBackground(String... params) {
		     
			  
		        
		        
		        
//		        mainFragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
//		        
//		        mainTabsView = (View) findViewById(R.id.main_tabs_view);
		        butteryProgressBar = (ButteryProgressBar)findViewById(R.id.search_progress_bar);
		        shopByListButton  = (ImageButton) findViewById(R.id.shop_outside_store);
		    	//shopAtStoreButton = (ImageButton) findViewById(R.id.shop_at_store);
		    	orderListButton   = (ImageButton) findViewById(R.id.orders_list);
		    	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_capture);
		        mainDrawerContainer = (FrameLayout)findViewById(R.id.left_drawer_container);
		        mDrawerToggle = new ActionBarDrawerToggle(currentActivityInstance, mDrawerLayout,R.string.drawer_open,R.string.drawer_close)
		        					{
							        	public void onDrawerClosed(View view) {
							                super.onDrawerClosed(view);
							                //getSupportActionBar().setTitle(mTitle);
							            }
							
							            /** Called when a drawer has settled in a completely open state. */
							            public void onDrawerOpened(View drawerView) {
							                super.onDrawerOpened(drawerView);
							                //getSupportActionBar().setTitle(mDrawerTitle);
							            }
		        	
		        				   };
		        drawerSearch= (RelativeLayout)findViewById(R.id.navigation_search_box);
		        drawerSearch.setOnClickListener(new OnClickListener() {
		        						
					@Override
					public void onClick(View v) {
						
						mDrawerLayout.closeDrawer(mainDrawerContainer);
						showSearchItems();
					}
				});
			    mDrawerLayout.setDrawerListener(mDrawerToggle);
		        ldrawer = (ListView)findViewById(R.id.left_drawer_capture);
		        
		        drawerChangeDelivery = (ImageButton)findViewById(R.id.drawer_change_delivery);
		        drawerHeaderName = (TextView)findViewById(R.id.drawer_user_name);
		        drawerHeaderAddress = (TextView)findViewById(R.id.drawer_delivery_address);
		        drawerHeaderShop = (TextView)findViewById(R.id.drawer_shop);
		        
		        
		        
		    	hasSurface = false;
		        // inactivityTimer = new InactivityTimer(this);
		        beepManager = new BeepManager(currentActivityInstance);

		        if (ZXScanHelper.getUserCallback() != null)
		        {
		            ZXScanHelper.getUserCallback().onScannerActivityCreated(currentActivityInstance);
		        }
		        
		        cartFrag = new CartFragment();
		        conFrag = new ContainerFragment();
		        
					        if(!conFrag.isAdded()){
					        	getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,conFrag ).commit();
					        	
					        }	
							
							if(!cartFrag.isAdded()){
					    		getSupportFragmentManager().beginTransaction().add(R.id.container,cartFrag ).detach(cartFrag).commit();
					    	}
		        
		     return "ok";
		   }

		   @Override
		   protected void onProgressUpdate(Integer... values) {
		      super.onProgressUpdate(values);
		      
		   }

		   @Override
		   protected void onPostExecute(String result) {
		      super.onPostExecute(result);
		      if(Globals.connectedShop != null){
		        	initDrawer();
		        }
		        else{
		        	 Shop shopObj = new Shop();
		     		shopObj.get_shop_list(currentActivityInstance,Globals.deliveryAddress.getDeliveryLocation());
		        }
		     
				
				
				
				mDrawerToggle.syncState();
		      conFrag.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

					@Override
					public void onPageScrollStateChanged(int position) {
						
					}

					@Override
					public void onPageScrolled(int position, float arg1, int arg2) {
						
					}

					@Override
					public void onPageSelected(int position) {
						
						if(position == 0){
							//shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_grey));
					    	shopByListButton.setBackgroundResource(R.drawable.shop_active);
					    	orderListButton.setBackgroundResource(R.drawable.list_grey);
							

					    	toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
							getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		        			getSupportActionBar().setHomeButtonEnabled(true);
		        			mDrawerToggle.setDrawerIndicatorEnabled(true);
							mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mainDrawerContainer);
							window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							 
							
						}
//						else if(position == 1){
//							//shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_blue));
//					    	shopByListButton.setBackgroundResource(R.drawable.cart_grey);
//					    	orderListButton.setBackgroundResource(R.drawable.purchase_order_grey);
//							
////					        ShopMap.setVisible(false);
//							getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//							getSupportActionBar().setHomeButtonEnabled(false);
//							mDrawerToggle.setDrawerIndicatorEnabled(false);
//							toolbar.setNavigationIcon(null);
//						    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//						    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mainDrawerContainer);
//						    handler.resumeDecodeThread();
//						   
//						}
						else{
							//shopAtStoreButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.scan_grey));
					    	shopByListButton.setBackgroundResource(R.drawable.shop_grey);
					    	orderListButton.setBackgroundResource(R.drawable.list_active);
							window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							getSupportActionBar().setDisplayHomeAsUpEnabled(false);
							getSupportActionBar().setHomeButtonEnabled(false);
							 mDrawerToggle.setDrawerIndicatorEnabled(false);
							toolbar.setNavigationIcon(null);
				    		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mainDrawerContainer);
				    		
				    		
						}
						
					}
	    			
	    			});
		      shopByListButton.setBackgroundResource(R.drawable.shop_active);
		       pbChangeDelivery = (ProgressBar) findViewById(R.id.change_delivery_progress);
		       
			   	  Controls.dismiss_progress_dialog();

		   }
	}
}
