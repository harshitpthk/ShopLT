/**
 * 
 */
package com.homelybuysapp.UI;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * @author I300291
 *
 */
public class UIUtil {
	
	public  static void setupUI(View view,final Activity activity) {
		
	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof SearchView)) {

	        view.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard(activity);
	                return false;
	            }

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView,activity);
	        }
	    }
	}
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(((Activity) activity).getCurrentFocus().getWindowToken(), 0);
	}
}
