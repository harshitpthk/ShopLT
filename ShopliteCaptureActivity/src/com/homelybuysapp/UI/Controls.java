package com.homelybuysapp.UI;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.homelybuysapp.interfaces.ControlsInterface;
import com.homelybuysapp.models.Product;

import eu.livotov.zxscan.R;
public class Controls {
	static int result = -1;
	static ProgressDialog pd = null ;
	private static ControlsInterface calling_class_object = null;
	
	
	
	public static void show_alert_dialog(ControlsInterface calling_class_object,Context context,int LayoutName, float size)
	{
		Controls.calling_class_object = calling_class_object;
		
		AlertDialog.Builder alert = new AlertDialog.Builder( context);
		if(LayoutName > 0){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		    XmlResourceParser custom_add_layout =  context.getResources().getLayout(LayoutName);
			View custom_add_view = inflater.inflate(custom_add_layout,null );
			alert.setView(custom_add_view);
			
		}
		
		
		final AlertDialog alertDialog = alert.create();
		alertDialog.show();
		ImageButton positive_button = (ImageButton)alertDialog.findViewById(R.id.positive_button);
		positive_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//alertDialog.dismiss();      //should be taken care by the callee methods
				Controls.calling_class_object.positive_button_alert_method();
			}
		});
		ImageButton negative_button = (ImageButton)alertDialog.findViewById(R.id.negative_button);
		negative_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//alertDialog.dismiss();    //should be taken care by the callee methods
				Controls.calling_class_object.negative_button_alert_method();
			}
		});
		
		
		alertDialog.setCancelable(false);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,(int) (metrics.density * size));
		calling_class_object.save_alert_dialog(alertDialog);
		
	}

	public static void show_add_item_dialog_spinner(Context context,AlertDialog AddDialog,Product itemFamily)
	{
		 
		
	}
	
	public static String get_spinner_selected_item(AlertDialog AddDialog)
	{

			return null;
	}
	
	public static void show_loading_dialog(Context context,String Message)
	{
		pd = new ProgressDialog(context);
		pd.setMessage(Message);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}
	
	public static void dismiss_progress_dialog()
	{
		if(pd != null)
			pd.dismiss();
	}

	public static void show_single_action_dialog(String title, String message,String button_caption,ControlsInterface calling_class_object,Context context)
	{
		Controls.calling_class_object = calling_class_object;
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		//alert.setTitle(title);
		alert.setMessage(message);
		alert.setNeutralButton(button_caption,  new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				Controls.calling_class_object.neutral_button_alert_method();
			}
			
		});
		
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
		alertDialog.setCancelable(false);
	}

	
	
}
