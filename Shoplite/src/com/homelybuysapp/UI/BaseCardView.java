/**
 * 
 */
package com.homelybuysapp.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sholite.R;

/**
 * @author I300291
 *
 */
public class BaseCardView extends LinearLayout {

	/**
	 * @param context
	 */
	public BaseCardView(Context context) {
		super(context);
		View.inflate(context, R.layout.drawer_item_container, this);
		
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BaseCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
			View.inflate(context, R.layout.drawer_item_container, this);
	}
	

}
