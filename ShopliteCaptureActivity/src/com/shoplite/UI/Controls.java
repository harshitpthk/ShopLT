package com.shoplite.UI;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.shoplite.Utils.Globals;
import com.shoplite.interfaces.ControlsInterface;
import com.shoplite.models.ItemCategory;

import eu.livotov.zxscan.R;
public class Controls {
	static int result = -1;
	static ProgressDialog pd = null ;
	private static ControlsInterface calling_class_object = null;
	private static ArrayList<String> measure_list_array;
	private static Spinner measure_spinner;
	private static ArrayAdapter<String> spinnerArrayAdapter;
	
	public static void show_alert_dialog(String Title,String Message,String positive_button_caption,String negative_button_caption,ControlsInterface calling_class_object,Context context,int LayoutName)
	{
		Controls.calling_class_object = calling_class_object;
		
		AlertDialog.Builder alert = new AlertDialog.Builder( context);
		if(LayoutName > 0){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		    XmlResourceParser custom_add_layout =  context.getResources().getLayout(LayoutName);
			View custom_add_view = inflater.inflate(custom_add_layout,null );
			alert.setView(custom_add_view);
			
		}
		
		
		alert.setPositiveButton(positive_button_caption, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			 
			  // Do something with value!
				Controls.calling_class_object.positive_button_alert_method();
			  }
		});

		alert.setNegativeButton(negative_button_caption, new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
				  
				  Controls.calling_class_object.negative_button_alert_method();
			  }
		});
		AlertDialog alertDialog = alert.create();
		
		alertDialog.show();
		alertDialog.setCancelable(false);
		//alertDialog.getWindow().setLayout(1000, 1300);
		calling_class_object.save_alert_dialog(alertDialog);
	}
	public static void show_add_item_dialog_spinner(Context context,AlertDialog AddDialog,ItemCategory itemFamily)
	{
		 measure_list_array = new ArrayList<String>();
		  measure_spinner = (Spinner)AddDialog.findViewById(R.id.item_measures);
		
		for(int i = 0 ; i <itemFamily.getItemList().size(); i++ )
			measure_list_array.add(itemFamily.getItemList().get(i).getName());
		
		spinnerArrayAdapter = new ArrayAdapter<String>(context,   android.R.layout.simple_spinner_item, measure_list_array);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		
		measure_spinner.setAdapter(spinnerArrayAdapter);
		
	}
	
	public static String get_spinner_selected_item(AlertDialog AddDialog)
	{
		Spinner spinner = (Spinner)AddDialog.findViewById(R.id.item_measures);
		String item = spinner.getSelectedItem().toString();
		if(item != null)
			return item;
		else 
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
}
