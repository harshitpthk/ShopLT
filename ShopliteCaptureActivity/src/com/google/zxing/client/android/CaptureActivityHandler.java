/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.shoplite.UI.Controls;
import com.shoplite.Utils.CartGlobals;
import com.shoplite.Utils.Constants.DBState;
import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.interfaces.ItemInterface;
import com.shoplite.interfaces.PackListInterface;
import com.shoplite.models.Item;
import com.shoplite.models.ItemCategory;
import com.shoplite.models.OrderItemDetail;
import com.shoplite.models.PackList;
import com.shoplite.models.Shop;

import eu.livotov.zxscan.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler implements ItemInterface,ControlsInterface,PackListInterface
{

    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State
    {
        PREVIEW,
        SUCCESS,
        DONE
    }

    CaptureActivityHandler(CaptureActivity activity,
                           Collection<BarcodeFormat> decodeFormats,
                           String characterSet,
                           CameraManager cameraManager)
    {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet,new ViewfinderResultPointCallback(null));
        decodeThread.start();
        state = State.SUCCESS;
        
        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }
    
    public void stopDecodeThread()
    {
    	
    }

    @Override
    public void handleMessage(Message message)
    {
        if (message.what == R.id.zx_restart_preview)
        {
            Log.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();

        } else if (message.what == R.id.zx_decode_succeeded)
        {
            Log.d(TAG, "Got decode succeeded message");
            state = State.SUCCESS;
            Bundle bundle = message.getData();
            Bitmap barcode = bundle == null ? null :
                                     (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
            activity.handleDecode((Result) message.obj, barcode);

        } else if (message.what == R.id.zx_decode_failed)
        {// We're decoding as fast as possible, so when one decode fails, start another.
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.zx_decode);

        } else if (message.what == R.id.zx_return_scan_result)
        {
            Log.d(TAG, "Got return scan result message");
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            
           // Result rawResult = (Result) message.obj;
             Intent result = (Intent) message.obj;
             Bundle extras = result.getExtras();
             String QRCValue = (String) extras.get("SCAN_RESULT");
             Gson gson =  new Gson();
             Item QRItem =  gson.fromJson(QRCValue, Item.class);

             
     		//Intent i = new Intent(activity,AddItem.class);
     		//activity.startActivity(i);
            Controls.show_alert_dialog("Add Item",QRItem.getName() ,"Add Item","Cancel" ,this,activity, R.layout.activity_add_item);
            getItem(QRItem.getId());
           
           
            
            
           // restartPreviewAndDecode();
            //activity.recreate();

        } else if (message.what == R.id.zx_launch_product_query)
        {
            Log.d(TAG, "Got product query message");
            String url = (String) message.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            activity.startActivity(intent);

        }
    }

    public void quitSynchronously()
    {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.zx_quit);
        quit.sendToTarget();
        try
        {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(1L);
        } catch (InterruptedException e)
        {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.zx_decode_succeeded);
        removeMessages(R.id.zx_decode_failed);
    }

    public  void restartPreviewAndDecode()
    {
        if (state == State.SUCCESS)
        {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.zx_decode);
            activity.drawViewfinder();
        }
    }
    // Custom methods
    
    public  void getItem(int ItemID)
    {
    	Item callingItem = new Item(0, null,0 , 0 );
    	callingItem.getItem(activity,ItemID);
    	
    	
    }
    
    public  void getItemList(int BrandId)
    {
    	
    }
    
    
    //interface methods
	@Override
	public void ItemAdded() {
		
		
	}

	

	@Override
	public void ItemGetSuccess() {
		
		
	}

	@Override
	public void ItemListGetSuccess() {
		
		
	}
	
	//Controls Interface

	@Override
	public void positive_button_alert_method() {
		String selected_measure = Controls.get_spinner_selected_item(activity.AddDialog);
		int variant_list_size = Globals.fetched_item_category.getItemList().size();
		for(int i = 0 ; i <variant_list_size ;i++ ){
			if(!Globals.fetched_item_category.getItemList().get(i).getName().equals(selected_measure)){
				Globals.fetched_item_category.getItemList().remove(i);
				i--;
				variant_list_size--;
			}
			
		}
		Globals.fetched_item_category.getItemList().trimToSize();
		if(!Globals.item_order_list.contains(Globals.fetched_item_category)){
			Globals.item_order_list.add(Globals.fetched_item_category);
			sendPackList();
			
		}
		else{
			Toast.makeText(activity.getApplicationContext(), "Item Already Added in your Cart", Toast.LENGTH_SHORT 	).show();
		}
		activity.cartFrag.add_scanned_items();
		restartPreviewAndDecode();
	}

	@Override
	public void negative_button_alert_method() {
		
		 restartPreviewAndDecode();
	}

	@Override
	public void save_alert_dialog(AlertDialog add_item_alert) {
		// TODO Auto-generated method stub
		activity.AddDialog = add_item_alert;
	}

	//Pack List Interface

	@Override
	public void sendPackList() {
		// TODO Auto-generated method stub
		int count = ItemCategory.countNotSent(Globals.item_order_list);
		
		if(count == 1){
			
		
			PackList pl = new PackList();
			pl.items = ItemCategory.getToSendList(Globals.item_order_list);
			pl.state = DBState.INSERT;
		
			ItemCategory.setSentList(Globals.item_order_list);
			
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
	public void editPackList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePackList(OrderItemDetail itemToDelete) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	@Override
	public void PackListSuccess(PackList obj) {
		// TODO Auto-generated method stub
		if(obj.state==DBState.DELETE){
			
		}
		else if (obj.state == DBState.INSERT){
			for(int i = 0 ;i < obj.items.size() ; i++){
				CartGlobals.cartList.add(obj.items.get(i));
			}
		}
		else{
			
		}
		
		
	}

	

	

}

